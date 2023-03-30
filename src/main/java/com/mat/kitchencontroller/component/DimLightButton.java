package com.mat.kitchencontroller.component;

import com.pi4j.context.Context;
import org.springframework.stereotype.Component;

@Component
public class DimLightButton extends LightButton {

    public DimLightButton(Context pi4j, LedStrip ledStrip) {
        super(pi4j, ledStrip);
    }

    @Override
    protected int getRequestedBrightness() {
        return 30;
    }

    @Override
    protected String getName() {
        return "dim-light-button";
    }

    @Override
    protected int getPin() {
        return 27;
    }
}

