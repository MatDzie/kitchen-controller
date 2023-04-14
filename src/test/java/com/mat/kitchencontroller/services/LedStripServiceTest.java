package com.mat.kitchencontroller.services;

import com.mat.kitchencontroller.config.SettingNames;
import com.mat.kitchencontroller.domain.Setting;
import com.mat.kitchencontroller.repositories.SettingRepository;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmConfig;
import com.pi4j.io.pwm.PwmConfigBuilder;
import com.pi4j.io.pwm.PwmType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LedStripServiceTest {

    private Context pi4j;
    private SettingRepository settingRepository;
    private Pwm pwm;
    private LedStripService ledStripService;

    @BeforeEach
    public void setUp() {
        pi4j = mock(Context.class);
        settingRepository = mock(SettingRepository.class);
        pwm = mock(Pwm.class);
        setupRepositoryMock();
        setupPi4jMock();
        ledStripService = new LedStripService(pi4j, settingRepository);
    }

    @AfterEach
    public void tearDown() {
        Mockito.framework().clearInlineMocks();
    }

    private void setupRepositoryMock() {
        when(settingRepository.findByName(endsWith(SettingNames.PIN_POSTFIX)))
                .thenReturn(new Setting(SettingNames.LED_STRIP + SettingNames.PIN_POSTFIX, "1"));
        when(settingRepository.findByName(endsWith(SettingNames.FREQUENCY_POSTFIX)))
                .thenReturn(new Setting(SettingNames.LED_STRIP + SettingNames.FREQUENCY_POSTFIX, "100"));
        when(settingRepository.findByName(endsWith(SettingNames.BRIGHTNESS_CHANGE_DELAY_MS)))
                .thenReturn(new Setting(SettingNames.LED_STRIP + SettingNames.BRIGHTNESS_CHANGE_DELAY_MS, "10"));
    }

    private void setupPi4jMock() {
        mockStatic(Pwm.class);
        PwmConfigBuilder pwmConfigBuilder = mock(PwmConfigBuilder.class);
        when(Pwm.newConfigBuilder(pi4j)).thenReturn(pwmConfigBuilder);
        when(pwmConfigBuilder.id(anyString())).thenReturn(pwmConfigBuilder);
        when(pwmConfigBuilder.name(anyString())).thenReturn(pwmConfigBuilder);
        when(pwmConfigBuilder.address(anyInt())).thenReturn(pwmConfigBuilder);
        when(pwmConfigBuilder.pwmType(any(PwmType.class))).thenReturn(pwmConfigBuilder);
        when(pwmConfigBuilder.initial(anyInt())).thenReturn(pwmConfigBuilder);
        when(pwmConfigBuilder.shutdown(anyInt())).thenReturn(pwmConfigBuilder);
        when(pwmConfigBuilder.build()).thenReturn(mock(PwmConfig.class));
        when(pi4j.create((PwmConfig) any())).thenReturn(pwm);
        when(pwm.isOn()).thenReturn(true);
        when(pwm.getDutyCycle()).thenReturn(50f);
    }

    @Test
    void testTurnOn() throws InterruptedException {
        int brightness = 75;

        ledStripService.turnOn(brightness);

        ArgumentCaptor<Integer> brightnessCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> frequencyCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(pwm, atLeastOnce()).on(brightnessCaptor.capture(), frequencyCaptor.capture());

        assertTrue(brightnessCaptor.getAllValues().contains(brightness));
    }

    @Test
    void testTurnOff() throws InterruptedException {
        ledStripService.turnOff();

        ArgumentCaptor<Integer> brightnessCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> frequencyCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(pwm, atLeastOnce()).on(brightnessCaptor.capture(), frequencyCaptor.capture());

        assertTrue(brightnessCaptor.getAllValues().contains(0));
    }

    @Test
    void testGetBrightness() {
        int expectedBrightness = Math.round(pwm.getDutyCycle());
        int actualBrightness = ledStripService.getBrightness();

        assertEquals(expectedBrightness, actualBrightness);
    }
}
