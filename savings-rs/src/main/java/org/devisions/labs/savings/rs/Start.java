package org.devisions.labs.savings.rs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;


@SpringBootApplication
@EnableWebFlux
public class Start {

    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }

}
