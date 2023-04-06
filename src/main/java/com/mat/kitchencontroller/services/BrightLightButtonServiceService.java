package com.mat.kitchencontroller.services;

import com.mat.kitchencontroller.config.SettingNames;
import com.mat.kitchencontroller.repositories.SettingRepository;
import com.pi4j.context.Context;
import org.springframework.stereotype.Service;

@Service
public class BrightLightButtonServiceService extends LightButtonService {
    public BrightLightButtonServiceService(Context pi4j, LightSourceService lightSourceService, SettingRepository settingRepository) {
        super(pi4j, lightSourceService, settingRepository);
    }

    @Override
    protected String getName() {
        return SettingNames.BRIGHT_LIGHT_BUTTON;
    }
}

