package com.mat.kitchencontroller.component;

import com.mat.kitchencontroller.configuration.SettingNames;
import com.mat.kitchencontroller.repositories.SettingRepository;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

public abstract class LightButton implements DigitalStateChangeListener {
    private final LedStrip ledStrip;
    protected SettingRepository settingRepository;

    protected LightButton(Context pi4j, LedStrip ledStrip, SettingRepository settingRepository) {
        this.ledStrip = ledStrip;
        this.settingRepository = settingRepository;

        var config = buildConfig(pi4j);
        subscribeForStateChange(pi4j, config);
    }

    DigitalInputConfig buildConfig(Context pi4j) {
        return DigitalInput.newConfigBuilder(pi4j)
                .id("BCM" + getPin())
                .name(getName())
                .address(getPin())
                .pull(PullResistance.PULL_DOWN)
                .debounce(getDebounce())
                .build();
    }

    void subscribeForStateChange(Context pi4j, DigitalInputConfig config) {
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
                ledStrip.turnOn(getBrightness());
            }
        }
    }

    boolean brightnessIsAlreadySet() {
        return ledStrip.getBrightness() == getBrightness();
    }

    private long getDebounce() {
        return Long.parseLong(settingRepository.findValueForName(SettingNames.LIGHT_BUTTON + SettingNames.DEBOUNCE_POSTFIX));
    }
    protected int getPin() {
        return Integer.parseInt(settingRepository.findValueForName(getName() + SettingNames.PIN_POSTFIX));
    }

    protected int getBrightness() {
        return Integer.parseInt(settingRepository.findValueForName(getName() + SettingNames.BRIGHTNESS_POSTFIX));
    }

    protected abstract String getName();
}

