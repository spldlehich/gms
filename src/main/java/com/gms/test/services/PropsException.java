package com.gms.test.services;

/**
 * Исключение для работы с конфигурацией. Чтобы не текла абстракция - заворачиваем в
 * данное исключение.
 */
public class PropsException extends Exception {

    /**
     * Конструктор.
     * 
     * @param message сообщение
     * @param cause причина
     */
    public PropsException(String message, Throwable cause) {
        super(message, cause);
    }
}
