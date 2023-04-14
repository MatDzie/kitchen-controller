package com.mat.kitchencontroller.controllers;

import com.mat.kitchencontroller.services.LightSourceService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class LightSourceController {

    private final LightSourceService lightSourceService;

    public LightSourceController(LightSourceService lightSourceService) {
        this.lightSourceService = lightSourceService;
    }

    @GetMapping("/light/on/{brightness}")
    public String on(@PathVariable("brightness") @Min(10) @Max(100) Integer brightness) {
        lightSourceService.turnOn(brightness);
        return "Turning light ON with brightness of: " + brightness;
    }

    @GetMapping("/light/off")
    public String off() {
        lightSourceService.turnOff();
        return "Turning light OFF!";
    }
}
