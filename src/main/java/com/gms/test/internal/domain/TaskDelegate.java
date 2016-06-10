package com.gms.test.internal.domain;

import java.util.concurrent.Callable;

import com.gms.test.domain.api.ResultTask;
import com.gms.test.domain.api.Task;
import com.gms.test.domain.api.TaskException;

/**
 * VO. Делегат задачи. Нужен для отслеживания выполняемых и выполненных задач.
 * Не используется в Hash коллекциях, equals и hashCode не переопределен.
 */
public final class TaskDelegate implements Callable<ResultTask> {
    private Task task;
    private boolean processing;
    private boolean free;

    /**
     * Конструктор.
     * 
     * @param task задача
     */
    public TaskDelegate(Task task) {
        this.task = task;
    }

    @Override
    public ResultTask call() throws TaskException {
        return task.call();
    }

    /**
     * Возвращает флаг выполнения
     * 
     * @return флаг
     */
    public boolean isProcessing() {
        return processing;
    }

    /**
     * Ставит отметку о выполнении
     */
    public void markProcessing() {
        processing = true;
    }

    /**
     * Отмечает задачу как выполненную.
     */
    public void markForFree() {
        free = true;
    }

    /**
     * Возвращает флаг о выполненности задачи.
     * 
     * @return флаг
     */
    public boolean isFree() {
        return free;
    }
}
