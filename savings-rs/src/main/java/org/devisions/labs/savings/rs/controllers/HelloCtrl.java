package org.devisions.labs.savings.rs.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/hello")
public class HelloCtrl {

    @GetMapping("/{name}")
    public Mono<String> hello(@PathVariable String name) {
        return Mono.fromSupplier( () -> String.format("Hello %s!\n", name));
    }

}
