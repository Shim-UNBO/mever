package com.mever.api;

import com.mever.api.domain.schedule.ScheduledTasks;
import org.springframework.beans.factory.annotation.Autowired;
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
