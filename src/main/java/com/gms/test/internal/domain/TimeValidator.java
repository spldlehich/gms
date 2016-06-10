package com.gms.test.internal.domain;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Валидирует актуальность выполнения задачи в данный момент.
 */
public final class TimeValidator {

    private TimeValidator() {
    }

    /**
     * Проверка.
     * 
     * @param timeTask время задачи
     * @return флаг о необходимости выполнить задачу. {@code true} - пора.
     */
    public static boolean checkRange(LocalDateTime timeTask)
    {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, timeTask).getSeconds() <= 0;
    }
}
