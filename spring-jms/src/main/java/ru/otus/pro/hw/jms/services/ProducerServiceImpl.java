package ru.otus.pro.hw.jms.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.pro.hw.jms.models.Message;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);

    private final ObjectMapper mapper;
    private final JmsTemplate jmsTemplate;

    @Value("${spring.artemis.embedded.queues}")
    private String artemisQueue;

    @Override
    public void send(Message message) {
        try {
            String jmsMessage = mapper.writeValueAsString(message);
            logger.info("Sending Message :: {}", jmsMessage);
            jmsTemplate.convertAndSend(artemisQueue, jmsMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
