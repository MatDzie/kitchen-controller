package com.mat.kitchencontroller.controllers;

import com.mat.kitchencontroller.config.SettingNames;
import com.mat.kitchencontroller.domain.Setting;
import com.mat.kitchencontroller.repositories.SettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ButtonController.class)
class ButtonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettingRepository settingRepository;

    private Setting dimSetting;
    private Setting brightSetting;

    @BeforeEach
    void setUp() {
        dimSetting = new Setting(SettingNames.DIM_LIGHT_BUTTON + SettingNames.BRIGHTNESS_POSTFIX, "30");
        brightSetting = new Setting(SettingNames.BRIGHT_LIGHT_BUTTON + SettingNames.BRIGHTNESS_POSTFIX, "60");
    }

    @Test
    void updateDimLightButtonBrightness() throws Exception {
        when(settingRepository.findByName(anyString())).thenReturn(dimSetting);
        mockMvc.perform(put("/dim-button-brightness/50").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(settingRepository, times(1)).save(dimSetting);
    }

    @Test
    void updateBrightLightButtonBrightness() throws Exception {
        when(settingRepository.findByName(anyString())).thenReturn(brightSetting);
        mockMvc.perform(put("/bright-button-brightness/70").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(settingRepository, times(1)).save(brightSetting);
    }

    @Test
    void updateBrightnessNotFound() throws Exception {
        when(settingRepository.findByName(anyString())).thenReturn(null);
        mockMvc.perform(put("/dim-button-brightness/80").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(settingRepository, times(0)).save(any(Setting.class));
    }
}
