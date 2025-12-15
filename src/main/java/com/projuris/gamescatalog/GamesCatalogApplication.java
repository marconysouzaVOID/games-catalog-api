package com.projuris.gamescatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class GamesCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamesCatalogApplication.class, args);
    }
}
