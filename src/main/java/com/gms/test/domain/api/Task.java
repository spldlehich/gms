package com.gms.test.domain.api;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * VO задачи. В методе call реализован механизм, позволяющий задать время на
 * которое занимаются ресурсы(для правдоподобия). hashCode и equals не
 * переопределен.
 */
public class Task implements Callable<ResultTask> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final long taskTimeMillis;

    /**
     * Конструктор.
     * 
     * @param taskTimeMillis время выполнения задачи
     */
    public Task(long taskTimeMillis) {
        checkArgument(taskTimeMillis >= 0);
        this.taskTimeMillis = taskTimeMillis;
    }

    @Override
    public ResultTask call() throws TaskException {
        if (taskTimeMillis > 0) {
            try {
                Thread.sleep(taskTimeMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new TaskException(e);
            }
        }
        logger.debug("Task done");
        return new ResultTask();
    }

}
