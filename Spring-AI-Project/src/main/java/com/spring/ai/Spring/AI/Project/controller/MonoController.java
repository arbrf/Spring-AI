package com.spring.ai.Spring.AI.Project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Locale;

@RestController
@RequestMapping("/mono")
public class MonoController {

    @GetMapping("/user")
    public String getUser() {
        String user = findUserFromDatabase();
        threadisBlocked();
        System.out.println(user);
        return user;
    }

    private void threadisBlocked() {
        System.out.println("Thread is Blocked I cant do anything");
    }

    private String findUserFromDatabase() {
        // Simulating database delay (3 seconds)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ibrahim = "Mohammad Ibrahim";

        return ibrahim;
    }


    @GetMapping("/usermono")
    public Mono<String> getUserMono() {
        Mono<String> userFromDatabaseMono = findUserFromDatabaseMono();
        threadisNotBlocked();
        System.out.println(userFromDatabaseMono);
        return userFromDatabaseMono;
    }

    private Mono<String> findUserFromDatabaseMono() {
        // Simulate a 3-second database delay, but NON-BLOCKING
        Mono<String> mohammadIbrahimMono = Mono.just("Mohammad Ibrahim Mono")
                .delayElement(Duration.ofSeconds(3))
                .doOnNext(value->System.out.println(value));

        System.out.println(mohammadIbrahimMono.toString().toUpperCase(Locale.ROOT));
        return mohammadIbrahimMono;
    }

    private void threadisNotBlocked() {
        System.out.println("I can do some operations here");
    }
}
