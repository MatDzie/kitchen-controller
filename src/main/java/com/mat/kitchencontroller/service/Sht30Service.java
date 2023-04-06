package com.mat.kitchencontroller.service;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import org.springframework.stereotype.Service;

@Service
public class Sht30Service implements HumidityService, TemperatureService {
    private final I2C sht30;

    public Sht30Service(Context pi4j) {
        this.sht30 = createSht30I2cInterface(pi4j);
        printTempAndHumidity();
    }

    I2C createSht30I2cInterface(Context pi4j) {
        I2CProvider i2CProvider = pi4j.provider("linuxfs-i2c");
        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j).id("SHT30").bus(1).device(0x44).build();
        return i2CProvider.create(i2cConfig);
    }

    @Override
    public double getRelativeHumidity() {
        var buffer = performMeasurement();
        return decodeCelsiusTemperature(buffer);
    }

    @Override
    public double getCelsiusTemperature() {
        var buffer = performMeasurement();
        return decodeRelativeHumidity(buffer);
    }

    public void printTempAndHumidity() {
        var buffer = performMeasurement();
        System.out.printf("SHT30: Relative Humidity : %.2f %%RH %n", decodeRelativeHumidity(buffer));
        System.out.printf("SHT30: Temperature in Celsius : %.2f C %n", decodeCelsiusTemperature(buffer));
    }

    private byte[] performMeasurement() {
        sendMeasurementCommand();
        waitForMeasurement();
        return readMeasurementBuffers();
    }

    private void sendMeasurementCommand() {
        // 0x2C06 - high repeatability measurement with clock stretching enabled
        byte[] config = new byte[2];
        config[0] = 0x2C;
        config[1] = 0x06;
        sht30.write(config, 0, 2);
    }

    private void waitForMeasurement() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to sleep thread during performing measurement!");
        }
    }

    private byte[] readMeasurementBuffers() {
        // Read 6 bytes of data: Temp msb, Temp lsb, Temp CRC, Humidity msb, Humidity lsb, Humidity CRC
        byte[] buffer = new byte[6];
        sht30.read(buffer, 0, 6);
        return buffer;
    }

    private double decodeCelsiusTemperature(byte[] buffer) {
        // Formula comes from SHT3x datasheet
        return (-45 + (175 * (((buffer[0] & 0xFF) * 256) + (buffer[1] & 0xFF)) / 65535.0));
    }

    private double decodeRelativeHumidity(byte[] buffer) {
        // Formula comes from SHT3x datasheet
        return (100 * (((buffer[3] & 0xFF) * 256) + (buffer[4] & 0xFF)) / 65535.0);
    }
}
