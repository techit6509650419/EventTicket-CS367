package com.eventticket.organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventOrganizerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventOrganizerServiceApplication.class, args);
    }
}