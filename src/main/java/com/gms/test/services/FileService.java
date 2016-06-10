package com.gms.test.services;

import java.io.File;
import java.io.IOException;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Сервис для работы с файлами. В данном случае для создания конфигурации.
 */
public class FileService {
    private String fileName;

    /**
     * Конструктор.
     * 
     * @param fileName имя файла
     */
    public FileService(String fileName) {
        checkArgument(!isNullOrEmpty(fileName));
        this.fileName = fileName;
    }

    /**
     * @return файл с настройками
     * @throws FileException
     */
    public File createFileIfAbsent() throws FileException {
        File fileProps = new File(fileName);
        try {
            if (!fileProps.exists())
                fileProps.createNewFile();
        } catch (IOException e) {
            throw new FileException("File exception.", e);
        }
        return fileProps;
    }
}
