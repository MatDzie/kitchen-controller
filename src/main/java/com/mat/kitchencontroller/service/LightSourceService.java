package com.mat.kitchencontroller.service;

public interface LightSourceService {
    void turnOn(int brightnessToSet);
    void turnOff();
    int getBrightness();
}
