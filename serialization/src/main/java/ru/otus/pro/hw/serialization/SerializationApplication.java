package ru.otus.pro.hw.serialization;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class SerializationApplication implements CommandLineRunner {
    public static void main(String[] args) {
        try {
            SpringApplication.run(SerializationApplication.class, args);
        } catch (Throwable ex) {
            log.error(ex);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inside run function.."); // сделать что-то
    }
}
