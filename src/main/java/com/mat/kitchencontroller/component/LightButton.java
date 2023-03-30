package com.mat.kitchencontroller.component;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

public abstract class LightButton implements DigitalStateChangeListener {

    private final LedStrip ledStrip;

    protected LightButton(Context pi4j, LedStrip ledStrip) {
        this.ledStrip = ledStrip;
        var config = DigitalInput.newConfigBuilder(pi4j)
                .id("BCM" + getPin())
                .name(getName())
                .address(getPin())
                .pull(PullResistance.PULL_DOWN)
                .debounce(DigitalInput.DEFAULT_DEBOUNCE)
                .build();

        DigitalInputProvider digitalInputProvider = pi4j.provider("pigpio-digital-input");
        var input = digitalInputProvider.create(config);
        input.addListener(this);
    }

    @Override
    public void onDigitalStateChange(DigitalStateChangeEvent event) {
        if (event.state().isHigh()) {
            if (brightnessIsAlreadySet()) {
                ledStrip.turnOff();
            } else {
                ledStrip.turnOn(getRequestedBrightness());
            }
        }
    }

    boolean brightnessIsAlreadySet() {
       return ledStrip.getBrightness() == getRequestedBrightness();
    }

    protected abstract int getRequestedBrightness();
    protected abstract String getName();
    protected abstract int getPin();
}

