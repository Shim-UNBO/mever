package com.mever.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MeverApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeverApplication.class, args);
    }

}
