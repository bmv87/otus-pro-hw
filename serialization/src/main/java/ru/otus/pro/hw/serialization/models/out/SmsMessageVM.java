package ru.otus.pro.hw.serialization.models.out;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessageVM {
    @JacksonXmlProperty(localName = "belong_number")
    private String belongNumber;
    @JacksonXmlProperty(localName = "send_date")
    private LocalDateTime sendDate;
    @JacksonXmlProperty(localName = "text")
    private String text;
}
