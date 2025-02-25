package ru.otus.pro.hw.jms.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {

    private String name;
    private String email;
    private String website;

}