package ru.otus.pro.hw.jms.consumers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @JmsListener(destination = "${spring.artemis.embedded.queues}")
    public void messageListener(String message) {
        LOGGER.info("Message received, {}", message);
    }
}