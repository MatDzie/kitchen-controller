package com.mat.kitchencontroller.controller;

import com.mat.kitchencontroller.service.TemperatureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemperatureController {

    private final TemperatureService temperatureService;

    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @GetMapping("/temperature")
    public String getCelsiusTemperature() {
        return String.format("Current temperature in Celsius : %.2f C %n", temperatureService.getCelsiusTemperature());
    }
}
