package com.bamboo;

import com.bamboo.service.fileAllowedService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.boot.CommandLineRunner;

@EnableJpaAuditing
@SpringBootApplication
public class BambooApplication {

    public static void main(String[] args) {


        SpringApplication.run(BambooApplication.class, args);}

    @Bean
    public CommandLineRunner run(fileAllowedService fileAllowedService) {
        return args -> {
            fileAllowedService.defaultFileAllowed();
        };
    }
}
