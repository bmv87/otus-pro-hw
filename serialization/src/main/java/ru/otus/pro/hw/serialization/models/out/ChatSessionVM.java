package ru.otus.pro.hw.serialization.models.out;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "chat_session")
public class ChatSessionVM {
    @JacksonXmlProperty(localName = "chat_identifier")
    private String chatIdentifier;
    @JacksonXmlElementWrapper(localName = "members")
    @JacksonXmlProperty(localName = "member")
    private List<MemberVM> members;
    @JacksonXmlElementWrapper(localName = "numbers")
    @JacksonXmlProperty(localName = "number")
    private List<NumberHistoryMV> numbers;
}
