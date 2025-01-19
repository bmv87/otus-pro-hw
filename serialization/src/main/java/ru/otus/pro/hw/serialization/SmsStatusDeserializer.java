package ru.otus.pro.hw.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.otus.pro.hw.serialization.models.in.SmsStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class SmsStatusDeserializer extends JsonDeserializer<SmsStatus> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    @Override
    public SmsStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Arrays.stream(SmsStatus.values()).filter(e-> {
            try {
                return jsonParser.getIntValue() == e.getValue();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).findFirst().orElse(null);

    }
}