package com.mat.kitchencontroller.component;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

public abstract class LightButton implements DigitalStateChangeListener {

    private final String PROVIDER_ID = "pigpio-digital-input";
    protected final LedStrip ledStrip;

    public LightButton(Context pi4j, LedStrip ledStrip) {
        this.ledStrip = ledStrip;
        var config = DigitalInput.newConfigBuilder(pi4j)
                .id("BCM" + getPin())
                .name(getName())
                .address(getPin())
                .pull(PullResistance.PULL_DOWN)
                .debounce(DigitalInput.DEFAULT_DEBOUNCE)
                .build();

        DigitalInputProvider digitalInputProvider = pi4j.provider(PROVIDER_ID);
        var input = digitalInputProvider.create(config);
        input.addListener(this);
    }

    @Override
    public void onDigitalStateChange(DigitalStateChangeEvent event) {
        if (ledStrip.getBrightness() != getRequestedBrightness()) {
            ledStrip.turnOn(getRequestedBrightness());
        } else {
            ledStrip.turnOff();
        }
    }

    abstract protected int getRequestedBrightness();
    abstract protected String getName();
    abstract protected int getPin();
}

