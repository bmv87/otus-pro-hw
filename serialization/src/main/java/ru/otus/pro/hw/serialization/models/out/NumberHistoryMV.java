package ru.otus.pro.hw.serialization.models.out;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumberHistoryMV {

    @JacksonXmlProperty(localName = "belong_number", isAttribute = true)
    private String belongNumber;

    @JacksonXmlElementWrapper(localName = "messages")
    @JacksonXmlProperty(localName = "message")
    private List<SmsMessageVM> messages;
}
