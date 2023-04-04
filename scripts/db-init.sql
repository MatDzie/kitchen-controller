CREATE DATABASE kitchen;
CREATE USER 'kitchen-controller'@'localhost' IDENTIFIED BY 'kitchen-controller';
GRANT ALL PRIVILEGES ON kitchen.* TO 'kitchen-controller'@'localhost';
FLUSH PRIVILEGES;
USE kitchen;

create TABLE setting(
  setting_id int auto_increment,
  setting_name varchar(255) not null,
  setting_value varchar(255) not null,
  PRIMARY KEY (setting_id)
  );

INSERT INTO
    setting(setting_name, setting_value)
VALUE
    ('LED_STRIP_PIN', '18'),
    ('LED_STRIP_FREQUENCY', '10000'),
    ('LED_STRIP_BRIGHTNESS_CHANGE_DELAY_MS', '7'),
    ('LIGHT_BUTTON_DEBOUNCE', '5000'),
    ('DIM_LIGHT_BUTTON_PIN', '17'),
    ('DIM_LIGHT_BUTTON_BRIGHTNESS', '25'),
    ('BRIGHT_LIGHT_BUTTON_PIN', '27'),
    ('BRIGHT_LIGHT_BUTTON_BRIGHTNESS', '95');
