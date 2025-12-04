package com.reliance.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RelianceGamesApplication {
    public static void main(String[] args) {
        SpringApplication.run(RelianceGamesApplication.class, args);
    }
}

