package com.mat.kitchencontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LedController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
