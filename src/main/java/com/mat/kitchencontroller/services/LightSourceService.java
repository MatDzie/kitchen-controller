package com.mat.kitchencontroller.services;

public interface LightSourceService {
    void turnOn(int brightnessToSet);
    void turnOff();
    int getBrightness();
}
