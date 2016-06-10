package com.gms.test.services;

/**
 * Исключение для работы с файлами. Чтобы не текла абстракция - заворачиваем в
 * данное исключение.
 */
public class FileException extends Exception {

    /**
     * Конструктор.
     * 
     * @param message сообщение
     * @param cause причина
     */
    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
