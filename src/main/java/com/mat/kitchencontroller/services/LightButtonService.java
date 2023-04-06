package com.mat.kitchencontroller.services;

import com.mat.kitchencontroller.config.SettingNames;
import com.mat.kitchencontroller.repositories.SettingRepository;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

public abstract class LightButtonService implements DigitalStateChangeListener {
    private final LightSourceService lightSourceService;
    protected SettingRepository settingRepository;

    protected LightButtonService(Context pi4j, LightSourceService lightSourceService, SettingRepository settingRepository) {
        this.lightSourceService = lightSourceService;
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
                lightSourceService.turnOff();
            } else {
                lightSourceService.turnOn(getBrightness());
            }
        }
    }

    boolean brightnessIsAlreadySet() {
        return lightSourceService.getBrightness() == getBrightness();
    }

    private long getDebounce() {
        return Long.parseLong(settingRepository.findByName(SettingNames.LIGHT_BUTTON + SettingNames.DEBOUNCE_POSTFIX).getValue());
    }
    protected int getPin() {
        return Integer.parseInt(settingRepository.findByName(getName() + SettingNames.PIN_POSTFIX).getValue());
    }

    protected int getBrightness() {
        return Integer.parseInt(settingRepository.findByName(getName() + SettingNames.BRIGHTNESS_POSTFIX).getValue());
    }

    protected abstract String getName();
}

