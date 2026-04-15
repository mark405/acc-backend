package com.traffgun.acc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AccApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccApplication.class, args);
    }
}
