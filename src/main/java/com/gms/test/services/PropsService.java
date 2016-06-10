package com.gms.test.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * Сервис для загрузки настроек.
 */
public final class PropsService {
    /**
     * Количество различных(!) задач в секунду.
     */

    private static final float DEFAULT_TASK_COUNT_IN_SECOND = 200;
    /**
     * Количество потоков для выполнения задач.
     */
    private static final int DEFAULT_CURRENT_THREAD = 200;

    /**
     * Количество времени на задачу.
     */
    private static final long DEFAULT_TASK_TIME_MILLIS = 500;

    /**
     * Количество задач в одно и тоже время.
     */
    private static final int DEFAULT_COUNT_EQ_TIME_TASK = 2;

    private PropsService(){
    }

    /**
     * Возвращает объект-конфигураацию.
     * 
     * @param file файл с конфигурацией
     * @return объект конфигурации
     * @throws PropsException
     */
    public static Props get(File file) throws PropsException {
        checkArgument(file != null);
        Properties props = new Properties();
        float taskCountInSecond;
        int countThread;
        long taskTimeMillis;
        int countEqTimeTask;
        try (FileInputStream is = new FileInputStream(file)) {
            props.load(is);
            taskCountInSecond = Float.valueOf(props.getProperty("taskCountInSecond",
                    String.valueOf(DEFAULT_TASK_COUNT_IN_SECOND)));
            countThread = Integer.valueOf(props.getProperty("countThread",
                    String.valueOf(DEFAULT_CURRENT_THREAD)));
            taskTimeMillis = Long.valueOf(props.getProperty("taskTimeMillis",
                    String.valueOf(DEFAULT_TASK_TIME_MILLIS)));
            countEqTimeTask = Integer.valueOf(props.getProperty("countEqTimeTask",
                    String.valueOf(DEFAULT_COUNT_EQ_TIME_TASK)));
        } catch (IOException e) {
            // Парсинг не обрабатываем. IO переоборачиваем, чтобы не вредить
            // верхним слоям.
            throw new PropsException("Properties not loaded.", e);
        }
        return new Props(taskCountInSecond, countThread, taskTimeMillis, countEqTimeTask);
    }
}