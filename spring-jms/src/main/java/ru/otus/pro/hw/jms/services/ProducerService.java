package ru.otus.pro.hw.jms.services;

import ru.otus.pro.hw.jms.models.Message;

public interface ProducerService {
    void send(Message message);
}
