package ru.otus.pro.hw.serialization.models.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatSession {
      @JsonProperty("chat_id")
      private Integer chatId;
      @JsonProperty("chat_identifier")
      private String chatIdentifier;
      @JsonProperty("display_name")
      private String displayName;
      @JsonProperty("is_deleted")
      private Boolean isDeleted;
      private List<Member> members;
      private List<SmsMessage> messages;
//      "chat_id":946,
//              "chat_identifier":"Apple",
//              "display_name":"",
//              "is_deleted":0,
//              "members":[],
//              "messages":[]
}
