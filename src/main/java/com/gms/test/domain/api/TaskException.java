package com.gms.test.domain.api;

/**
 * Основное исключение при обработке задания. Чтобы не текла абстракция - заворачиваем в
 * данное исключение.
 */
public class TaskException extends Exception {

    /**
     * Конструктор.
     * 
     * @param cause причина
     */
    public TaskException(Throwable cause) {
        super(cause);
    }
}
