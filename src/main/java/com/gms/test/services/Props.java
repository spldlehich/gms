package com.gms.test.services;

/**
 * VO для хранения конфигурации. Для полноценного VO нужно определить equals и
 * hashCode, но на данный момент не используетсяя в коллекциях.
 */
public final class Props {
    private final float taskCountInSecond;
    private final int countThread;
    private final long taskTimeMillis;
    private final int countEqTimeTask;

    /**
     * Конструктор.
     * 
     * @param taskCountInSecond количество генерируемых задач в секунду
     * @param countThread количество потоков для обработки задач
     * @param taskTimeMillis время в мс для выполнения задачи
     * @param countEqTimeTask количество задач в одно и тоже время
     */
    public Props(float taskCountInSecond, int countThread, long taskTimeMillis, int countEqTimeTask) {
        this.taskCountInSecond = taskCountInSecond;
        this.countThread = countThread;
        this.taskTimeMillis = taskTimeMillis;
        this.countEqTimeTask = countEqTimeTask;
    }

    /**
     * @return количество генерируемых задач в секунду
     */
    public float getTaskCountInSecond() {
        return taskCountInSecond;
    }

    /**
     * @return количество потоков для обработки задач
     */
    public int getCountThread() {
        return countThread;
    }

    /**
     * @return время в мс для выполнения задачи
     */
    public long getTaskTimeMillis() {
        return taskTimeMillis;
    }

    /**
     * @return количество задач в одно и тоже время
     */
    public int getCountEqTimeTask() {
        return countEqTimeTask;
    }

}
