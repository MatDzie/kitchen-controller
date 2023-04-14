#!/bin/bash
sudo apt update
sudo apt upgrade -y
sudo apt install openjdk-17-jdk -y
sudo apt install mariadb-server -y
sudo raspi-config nonint do_i2c 0
sudo apt-get install pigpio -y
sudo mysql_secure_installation
