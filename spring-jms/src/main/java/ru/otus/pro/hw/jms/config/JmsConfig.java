package ru.otus.pro.hw.jms.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@EnableJms
@Configuration
public class JmsConfig {

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        var jmsListenerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerFactory.setConnectionFactory(connectionFactory);
        jmsListenerFactory.setConcurrency("2-4");
        return jmsListenerFactory;
    }
}
