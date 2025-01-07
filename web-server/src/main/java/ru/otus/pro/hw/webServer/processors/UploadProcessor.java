package ru.otus.pro.hw.webServer.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pro.hw.webServer.exceptions.ResponseException;
import ru.otus.pro.hw.webServer.helpers.ApplicationPropertiesHelper;
import ru.otus.pro.hw.webServer.http.HttpMethod;
import ru.otus.pro.hw.webServer.models.FileContent;
import ru.otus.pro.hw.webServer.models.FileUploadingResponseVM;
import ru.otus.pro.hw.webServer.routing.File;
import ru.otus.pro.hw.webServer.routing.Processor;
import ru.otus.pro.hw.webServer.routing.RoutePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Processor()
public class UploadProcessor {
    private String filesPath;
    private static final Logger logger = LoggerFactory.getLogger(UploadProcessor.class);

    public UploadProcessor() {
        try {
            this.filesPath = ApplicationPropertiesHelper.tryGet(ApplicationPropertiesHelper.FILES_STORE_DIRECTORY_PARAM_NANE, String.class);
        } catch (RuntimeException ex) {
            logger.info(ex.getMessage());
        }
        if (this.filesPath == null || this.filesPath.isBlank()) {
            this.filesPath = "files";
        }
    }


    @RoutePath(method = HttpMethod.POST, path = "files")
    public FileUploadingResponseVM upload(@File FileContent model) {
        return upload(model.getContent(), model.getFileName());
    }

    private FileUploadingResponseVM upload(byte[] content, String fileName) {
        try {
            Path uploadPath = Paths.get(filesPath);
            logger.info(uploadPath.toString());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            var nameParts = fileName.split("\\.");
            var extension = nameParts[nameParts.length - 1].toLowerCase();
            String fileNameForFS = UUID.randomUUID() + "." + extension;
            Path uploadFilePath = Paths.get(filesPath, fileNameForFS);
            if (Files.exists(uploadFilePath)) {
                throw new IOException("File already exists: " + uploadFilePath);
            }
            logger.debug("File content length for saving {}: ", content.length);

            Files.write(uploadFilePath, content, StandardOpenOption.CREATE);
            return new FileUploadingResponseVM(fileNameForFS);

        } catch (IOException e) {
            throw new ResponseException("Ошибка сохранения файла в файловой системе.", e);
        }
    }
}
