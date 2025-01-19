package ru.otus.pro.hw.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Log4j2
public class CustomOffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var timestamp = jsonParser.getText();
        try {
            return OffsetDateTime.ofInstant(Instant.ofEpochSecond(                          // Parse a count since epoch reference of 1970-01-01T00:00:00Z.
                    0L,                                 // Passing zero for the count of whole seconds, to let the class determine this number from the 2nd argument.
                    Long.parseLong(timestamp)), ZoneId.of("UTC"));
        } catch (NumberFormatException e) {
            log.warn("Unable to deserialize timestamp: " + timestamp, e);
            return null;
        }
    }
}