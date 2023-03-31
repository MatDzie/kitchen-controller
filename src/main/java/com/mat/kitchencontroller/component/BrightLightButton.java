package com.mat.kitchencontroller.component;

import com.pi4j.context.Context;
import org.springframework.stereotype.Component;

@Component
public class BrightLightButton extends LightButton {

    public BrightLightButton(Context pi4j, LedStrip ledStrip) {
        super(pi4j, ledStrip);
    }

    // TODO: Move button configuration to SQL DB
    @Override
    protected int getRequestedBrightness() {
        return 100;
    }

    @Override
    protected String getName() {
        return "bright-light-button";
    }

    @Override
    protected int getPin() {
        return 27;
    }
}

