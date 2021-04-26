package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class R2dbcConnectionLeakApplication {

    public static void main(String[] args) {
        SpringApplication.run(R2dbcConnectionLeakApplication.class, args);
    }
}
