package com.mat.kitchencontroller.controller;

import com.mat.kitchencontroller.service.LightSourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LightSourceController {

    private final LightSourceService lightSourceService;

    public LightSourceController(LightSourceService lightSourceService) {
        this.lightSourceService = lightSourceService;
    }

    // TODO: Add validation 0-100
    // TODO: Change API to be compatible with Google Home
    @GetMapping("/light/on/{brightness}")
    public String on(@PathVariable("brightness") Integer brightness) {
        lightSourceService.turnOn(brightness);
        return "Turning light ON with brightness of: " + brightness;
    }

    @GetMapping("/light/off")
    public String off() {
        lightSourceService.turnOff();
        return "Turning light OFF!";
    }
}
