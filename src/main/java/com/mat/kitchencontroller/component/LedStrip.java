package com.mat.kitchencontroller.component;

import com.mat.kitchencontroller.configuration.SettingNames;
import com.mat.kitchencontroller.repositories.SettingRepository;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import org.springframework.stereotype.Component;

@Component
public class LedStrip {
    private final Pwm pwm;
    private final SettingRepository settingRepository;

    public LedStrip(Context pi4j, SettingRepository settingRepository) {
        this.settingRepository = settingRepository;

        this.pwm = pi4j.create(Pwm.newConfigBuilder(pi4j)
                .id("PIN" + getPin())
                .name(SettingNames.LED_STRIP)
                .address(getPin())
                .pwmType(PwmType.HARDWARE)
                .initial(0)
                .shutdown(0)
                .build());
    }

    public synchronized void turnOn(int brightnessToSet) {
        smoothlyChangeBrightness(brightnessToSet);
    }

    public synchronized void turnOff() {
        smoothlyChangeBrightness(0);
    }

    private void smoothlyChangeBrightness(int to) {
        var from = getBrightness();
        while (from != to) {
            if (from > to)
                --from;
            else
                ++from;

            pwm.on(from, getFrequency());

            try {
                Thread.sleep(getDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getBrightness() {
        return pwm.isOn() ? Math.round(pwm.getDutyCycle()) : 0;
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
