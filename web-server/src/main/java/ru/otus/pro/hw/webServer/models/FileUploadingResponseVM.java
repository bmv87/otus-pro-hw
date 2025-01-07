package ru.otus.pro.hw.webServer.models;

import lombok.Data;

@Data
public class FileUploadingResponseVM {
    private String fileName;

    public FileUploadingResponseVM(String fileName) {
        this.fileName = fileName;
    }
}
