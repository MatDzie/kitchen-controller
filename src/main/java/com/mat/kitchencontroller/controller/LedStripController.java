package com.mat.kitchencontroller.controller;

import com.mat.kitchencontroller.component.LedStrip;
import com.pi4j.context.Context;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LedStripController {

    private final LedStrip ledStrip;

    public LedStripController(Context pi4j, LedStrip ledStrip) {
        this.ledStrip = ledStrip;
    }

    @GetMapping("/on/{brightness}")
    public String on(@PathVariable("brightness") Integer brightness) {
        ledStrip.turnOn(brightness);
        return "Turning LED on with brightness of: " + brightness;
    }

    @GetMapping("/off")
    public String off() {
        ledStrip.turnOff();
        return "Turning LED off!";
    }
}
