package com.mat.kitchencontroller.component;

import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import org.springframework.stereotype.Component;

@Component
public class LedStrip {
    private static final int FREQUENCY = 15000;
    private static final int PIN = 18;
    private final Pwm pwm;

    public LedStrip(Context pi4j) {
        this.pwm = pi4j.create(Pwm.newConfigBuilder(pi4j)
                .id("PIN" + PIN)
                .name("LedStrip")
                .address(PIN)
                .pwmType(PwmType.HARDWARE)
                .initial(0)
                .shutdown(0)
                .build());
    }

    public synchronized void turnOn(Number brightness) {
        pwm.on(brightness, FREQUENCY);
    }

    public synchronized void turnOff() {
        pwm.off();
    }

    public synchronized float getBrightness() {
        return pwm.isOn() ? pwm.getDutyCycle() : 0;
    }
}
