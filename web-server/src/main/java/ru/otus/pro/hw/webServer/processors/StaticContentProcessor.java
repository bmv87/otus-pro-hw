package ru.otus.pro.hw.webServer.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pro.hw.webServer.exceptions.NotFoundException;
import ru.otus.pro.hw.webServer.exceptions.ResponseException;
import ru.otus.pro.hw.webServer.helpers.ApplicationPropertiesHelper;
import ru.otus.pro.hw.webServer.http.HttpMethod;
import ru.otus.pro.hw.webServer.models.FileContent;
import ru.otus.pro.hw.webServer.routing.File;
import ru.otus.pro.hw.webServer.routing.PathVariable;
import ru.otus.pro.hw.webServer.routing.Processor;
import ru.otus.pro.hw.webServer.routing.RoutePath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Processor
public class StaticContentProcessor {
    private String staticContentPath;
    private static final Logger logger = LoggerFactory.getLogger(StaticContentProcessor.class);

    public StaticContentProcessor() {
        try {
            this.staticContentPath = ApplicationPropertiesHelper.tryGet(ApplicationPropertiesHelper.STATIC_STORE_DIRECTORY_PARAM_NANE, String.class);
        } catch (RuntimeException ex) {
            logger.info(ex.getMessage());
        }
        if (this.staticContentPath == null || this.staticContentPath.isBlank()) {
            this.staticContentPath = "static";
        }
    }

    @RoutePath(method = HttpMethod.GET, path = "static/index.html")
    public byte[] downloadFile() {
        try {
            Path filePath = Paths.get(staticContentPath, "index.html");
            logger.info(filePath.toString());
            if (!filePath.toFile().exists()) {
                throw new NotFoundException("Файл не найден!");
            }
            var content = Files.readAllBytes(filePath);
            return content;
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    @RoutePath(method = HttpMethod.GET, path = "static/{fileName}")
    @File
    public FileContent downloadFile(@PathVariable(name = "fileName") String fileName) {
        try {
            Path filePath = Paths.get(staticContentPath, fileName);
            logger.info(filePath.toString());
            if (!filePath.toFile().exists()) {
                throw new NotFoundException("Файл не найден!");
            }

            var content = Files.readAllBytes(filePath);
            return new FileContent(fileName, Files.probeContentType(filePath), content.length, content);
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }
}
