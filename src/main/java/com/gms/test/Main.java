package com.gms.test;

import java.util.concurrent.Executors;

import com.gms.test.domain.TaskAggregator;
import com.gms.test.services.FileException;
import com.gms.test.services.FileService;
import com.gms.test.services.Props;
import com.gms.test.services.PropsException;
import com.gms.test.services.PropsService;

/**
 * Main класс.
 */
public class Main {
    private final static String PROPS_FILE = "props.properties";

    public static void main(String[] args) throws PropsException, FileException {
        Props props = PropsService.get(new FileService(PROPS_FILE).createFileIfAbsent());
        TaskAggregator aggregator = new TaskAggregator(
                Executors.newScheduledThreadPool(props.getCountThread()));
        TaskGenerator generator = new TaskGenerator(aggregator, props.getTaskCountInSecond(),
                props.getTaskTimeMillis(), props.getCountEqTimeTask());
        generator.start();
        // Можно реализовать логику для стопа, на данный момент стопается по
        // interrupt, т.к. неясен жизненный цикл.
    }
}
