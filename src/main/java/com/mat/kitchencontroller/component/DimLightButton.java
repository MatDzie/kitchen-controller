package com.mat.kitchencontroller.component;

import com.mat.kitchencontroller.configuration.SettingNames;
import com.mat.kitchencontroller.repositories.SettingRepository;
import com.pi4j.context.Context;
import org.springframework.stereotype.Component;

@Component
public class DimLightButton extends LightButton {

    public DimLightButton(Context pi4j, LedStrip ledStrip, SettingRepository settingRepository) {
        super(pi4j, ledStrip, settingRepository);
    }

    @Override
    protected String getName() {
        return SettingNames.DIM_LIGHT_BUTTON;
    }
}

