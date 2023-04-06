package com.mat.kitchencontroller.service;

import com.mat.kitchencontroller.configuration.SettingNames;
import com.mat.kitchencontroller.repositories.SettingRepository;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import org.springframework.stereotype.Service;

@Service
public class LedStripService implements LightSourceService {
    private final Pwm pwm;
    private final SettingRepository settingRepository;

    public LedStripService(Context pi4j, SettingRepository settingRepository) {
        this.settingRepository = settingRepository;

        var pin = getPin();
        this.pwm = pi4j.create(Pwm.newConfigBuilder(pi4j)
                .id("BCM" + pin)
                .name(SettingNames.LED_STRIP)
                .address(pin)
                .pwmType(PwmType.HARDWARE)
                .initial(0)
                .shutdown(0)
                .build());
    }

    @Override
    public synchronized void turnOn(int brightnessToSet) {
        smoothlyChangeBrightness(brightnessToSet);
    }

    @Override
    public synchronized void turnOff() {
        smoothlyChangeBrightness(0);
    }

    @Override
    public int getBrightness() {
        return pwm.isOn() ? Math.round(pwm.getDutyCycle()) : 0;
    }

    private void smoothlyChangeBrightness(int to) {
        var from = getBrightness();
        var frequency = getFrequency();
        var delay = getDelay();

        while (from != to) {
            if (from > to)
                --from;
            else
                ++from;

            pwm.on(from, frequency);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private int getPin() {
        return getSettingValue(SettingNames.PIN_POSTFIX);
    }

    private int getFrequency() {
        return getSettingValue(SettingNames.FREQUENCY_POSTFIX);
    }

    private int getDelay() {
        return getSettingValue(SettingNames.BRIGHTNESS_CHANGE_DELAY_MS);
    }

    private int getSettingValue(String postfix) {
        return Integer.parseInt(settingRepository.findByName(SettingNames.LED_STRIP + postfix).getValue());
    }
}
