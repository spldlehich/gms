package com.gms.test.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gms.test.domain.api.IAggregator;
import com.gms.test.domain.api.ResultTask;
import com.gms.test.domain.api.Task;
import com.gms.test.domain.api.TaskException;
import com.gms.test.internal.domain.TaskDelegate;
import com.gms.test.internal.domain.TimeValidator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Аггрегатор. В зависимости от условий можно реализовать проход по дереву с
 * задачами разными способами. Если потоков мало, а задач много - эффективнее
 * всего будет синхронизация по дереву при поиске задачи в списке задач с
 * одинаковым временем и модиикацией этого списка. Если потоков много, а задач в
 * одно и тоже время не очень много, то эффективнее всего сделать механизм без
 * синхронизации и модификаций с последующем сбором "мусора" из отработанных
 * заданий. На данный момент реализовано что-то среднее - проход по дереву и
 * списку задач, где список не модифицируется, а при выполнении всех задач
 * удаляется нода дерева. При этом происходит синхронизация только по задаче.
 */
public final class TaskAggregator implements IAggregator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Ключ - время события, значение - список событий.
     */
    private final Map<LocalDateTime, List<TaskDelegate>> tasks = Collections.synchronizedMap(new TreeMap<>());
    private ScheduledExecutorService service;

    public TaskAggregator(ScheduledExecutorService service) {
        this.service = service;
    }

    @Override
    public void close() {
        service.shutdownNow();
    }

    @Override
    public void add(LocalDateTime time, Task tsk) {
        checkArgument(time != null);
        checkArgument(tsk != null);
        List<TaskDelegate> tasksList = tasks.computeIfAbsent(time,
                v -> Collections.synchronizedList(new ArrayList<>()));
        tasksList.add(new TaskDelegate(tsk));
        long delay = Duration.between(LocalDateTime.now(), time).getSeconds();
        try {
            if (delay > 0)
                service.schedule(new Worker(), delay, TimeUnit.SECONDS);
            else
                service.schedule(new Worker(), 0, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            logger.warn("Cannot create schedule.", e);
        }
    }

    private void work() {
        boolean repeat = true;
        // Проходим снова и снова, если не успеваем обрабатывать события
        while (repeat && !Thread.currentThread().isInterrupted()) {
            TaskDelegate current = null;
            Map<LocalDateTime, List<TaskDelegate>> safeTasks = new TreeMap<>(tasks);
            // проходим по дереву
            for (Entry<LocalDateTime, List<TaskDelegate>> entry : safeTasks.entrySet()) {
                Iterator<TaskDelegate> it  = entry.getValue().iterator();
                // идем по списку задач(одинаковое время у задач)
                while (it.hasNext()) {
                    TaskDelegate task = it.next();
                    // Синхронизируем по задаче, чтобы несколько потоков не
                    // начало ее выполнять
                    synchronized (task) {
                        if (task.isFree()) {
                            // Если все задачи выполнены - удаляем элемент
                            // из дерева
                            if (!it.hasNext())
                                tasks.remove(entry.getKey());
                        }
                        // Если уже выполняется - игнорируем
                        if (task.isProcessing())
                            continue;
                        if (TimeValidator.checkRange(entry.getKey())) {
                            task.markProcessing();
                            current = task;
                            if (logger.isDebugEnabled()) {
                                logger.debug("Find task at time {}. Current time {}", entry.getKey(),
                                        LocalDateTime.now());
                            }
                        } else {
                            repeat = false;
                        }
                    }
                    break;
                }
                // Усли не null - нашли актуальную задачу.
                if (current != null)
                    break;
            }
            if (current != null) {
                try {
                    ResultTask result = current.call();
                    checkState(result != null);
                } catch (TaskException e) {
                    logger.warn("Cannot task apply.", e);
                }
                current.markForFree();
            }
        }
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            work();
        }
    }
}
