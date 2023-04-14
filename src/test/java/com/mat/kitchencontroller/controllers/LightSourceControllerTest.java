package com.mat.kitchencontroller.controllers;

import com.mat.kitchencontroller.services.LightSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LightSourceController.class)
class LightSourceControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private LightSourceService lightSourceService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testOn() throws Exception {
        int brightness = 75;

        mockMvc.perform(get("/light/on/{brightness}", brightness))
                .andExpect(status().isOk())
                .andExpect(content().string("Turning light ON with brightness of: " + brightness));

        verify(lightSourceService, times(1)).turnOn(brightness);
    }

    @Test
    void testOff() throws Exception {
        mockMvc.perform(get("/light/off"))
                .andExpect(status().isOk())
                .andExpect(content().string("Turning light OFF!"));

        verify(lightSourceService, times(1)).turnOff();
    }
}
