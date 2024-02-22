package com.example.warehouse2;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class Warehouse2Application {

    public static void main(String[] args) {
        SpringApplication.run(Warehouse2Application.class, args);
    }

}
