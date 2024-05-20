package com.codehows.sbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Article 의 created_at 와 updated_at 자동 업데이트
@SpringBootApplication
public class SbdApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbdApplication.class, args);
    }

}
