package com.gms.test;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gms.test.domain.api.IAggregator;
import com.gms.test.domain.api.Task;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Генератор для заданий.
 */
public final class TaskGenerator {
    /**
     * Стартовать задачу будем через TIMEOUT_TASK секунд
     */
    private static final int TIMEOUT_TASK = 1;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private final IAggregator aggregator;
    private final float delay;
    private final long taskTimeMillis;
    private final int countEqTimeTask;
    private volatile boolean active = true;

    /**
     * Конструктор.
     * 
     * @param aggregator аггрегатор задач.
     * @param taskCountInSecond количество задач в секунду.
     * @param countEqTimeTask количество задач в одно и тоже время
     */
    public TaskGenerator(IAggregator aggregator, float taskCountInSecond, long taskTimeMillis, int countEqTimeTask) {
        checkArgument(aggregator != null);
        checkArgument(taskCountInSecond != 0);
        this.aggregator = aggregator;
        this.delay = (float) 1000 / taskCountInSecond;
        this.taskTimeMillis = taskTimeMillis;
        this.countEqTimeTask = countEqTimeTask;
    }

    /**
     * Старт. Можно ввести интерфейс типа ILifeCycle, если нужен еще в других
     * местах.
     */
    public void start() {
        schedule();
    }

    /**
     * Стоп.
     */
    public void stop() {
        active = false;
        service.shutdown();
    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            if (Thread.currentThread().isInterrupted())
            {
                active = false;
                service.shutdown();
            }
            if (!active)
                return;
            LocalDateTime time = LocalDateTime.now();
            time.plusSeconds(TIMEOUT_TASK);
            for (int i = 0; i < countEqTimeTask; ++i)
            {
                aggregator.add(time, new Task(taskTimeMillis));
            }
            logger.debug("Generate {} task at time {}", countEqTimeTask, time);
            schedule();
        }
    }

    private void schedule() {
        try {
            service.schedule(new Worker(), (long) delay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            logger.warn("Cannot scheduled new task.", e);
        }
    }
}
