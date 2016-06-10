package com.gms.test.domain.api;

import java.io.Closeable;
import java.time.LocalDateTime;

/**
 * Интерфейс аггрегатора задач.
 */
public interface IAggregator extends Closeable {
    /**
     * Добавляет задачу.
     * 
     * @param time время
     * @param tsk задача
     */
    void add(LocalDateTime time, Task tsk);

    @Override
    void close();
}
