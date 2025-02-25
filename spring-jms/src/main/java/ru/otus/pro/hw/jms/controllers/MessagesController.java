package ru.otus.pro.hw.jms.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pro.hw.jms.models.Message;
import ru.otus.pro.hw.jms.services.ProducerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessagesController {

    private final ProducerService producerService;

    @PostMapping
    public ResponseEntity<String> publish(@RequestBody Message message) {
        try {
            producerService.send(message);
            return new ResponseEntity<>("Message Sent", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
