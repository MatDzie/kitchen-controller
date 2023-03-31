package com.mat.kitchencontroller.component;

import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import org.springframework.stereotype.Component;

@Component
public class LedStrip {
    // TODO: Move LedStrip configuration to SQL DB
    private static final int BRIGHTNESS_CHANGE_DELAY_MS = 7;
    private static final int FREQUENCY = 10000;
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

    public synchronized void turnOn(int brightnessToSet) {
        smoothlyChangeBrightness(brightnessToSet);
    }

    private void smoothlyChangeBrightness(int to) {
        var from = getBrightness();
        while (from != to) {
            if (from > to)
                --from;
            else
                ++from;

            pwm.on(from, FREQUENCY);

            try {
                Thread.sleep(BRIGHTNESS_CHANGE_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void turnOff() {
        smoothlyChangeBrightness(0);
    }

    public int getBrightness() {
        return pwm.isOn() ? Math.round(pwm.getDutyCycle()) : 0;
    }
}
