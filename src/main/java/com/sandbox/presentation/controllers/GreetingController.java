package com.sandbox.presentation.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class GreetingController {

    @GetMapping("api/v1/greeting")
    public Set<String> greeting() {
        return Set.of("Bonjour !",
                "Good Morning !");
    }
}
