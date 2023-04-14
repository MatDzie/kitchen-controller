package com.mat.kitchencontroller.controllers;

import com.mat.kitchencontroller.config.SettingNames;
import com.mat.kitchencontroller.domain.Setting;
import com.mat.kitchencontroller.repositories.SettingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ButtonController {

    private final SettingRepository settingRepository;

    public ButtonController(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @PutMapping("/dim-button-brightness/{value}")
    public ResponseEntity<Void> updateDimLightButtonBrightness(@PathVariable String value) {
        return updateBrightness(SettingNames.DIM_LIGHT_BUTTON + SettingNames.BRIGHTNESS_POSTFIX, value);
    }

    @PutMapping("/bright-button-brightness/{value}")
    public ResponseEntity<Void> updateBrightLightButtonBrightness(@PathVariable String value) {
        return updateBrightness(SettingNames.BRIGHT_LIGHT_BUTTON + SettingNames.BRIGHTNESS_POSTFIX, value);
    }

    private ResponseEntity<Void> updateBrightness(String settingName, String value) {
        Setting setting = settingRepository.findByName(settingName);
        if (setting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        setting.setValue(value);
        settingRepository.save(setting);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
