package ru.otus.pro.hw.serialization.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.pro.hw.serialization.models.in.ChatSession;
import ru.otus.pro.hw.serialization.models.in.SessionsContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImportService {
    private ObjectMapper mapper;
    private String smsImportPath;

    public ImportService(ObjectMapper mapper, @Value("${sms.import.path}") String smsImportPath) {
        this.mapper = mapper;
        this.smsImportPath = smsImportPath;
    }

    public File getResourceFile() throws IOException {
        Path uploadPath = Paths.get(smsImportPath).toAbsolutePath();
        return uploadPath.toFile();
    }

    public SessionsContainer getChatSessions() throws IOException {

        return mapper.readValue(getResourceFile(), SessionsContainer.class);
    }
}
