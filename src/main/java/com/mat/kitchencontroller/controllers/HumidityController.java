package com.mat.kitchencontroller.controllers;

import com.mat.kitchencontroller.services.HumidityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HumidityController {

    private final HumidityService humidityService;

    public HumidityController(HumidityService humidityService) {
        this.humidityService = humidityService;
    }

    @GetMapping("/humidity")
    public String getRelativeHumidity() {
        return String.format("Current relative Humidity : %.2f %%RH %n", humidityService.getRelativeHumidity());
    }
}