package com.pandrewkk.whiteorchard.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WindScale {

    CALM(0, "Безветрие"),
    LIGHT_AIR(0.3, "Едва дует"),
    LIGHT_BREEZE(1.6, "Ветерок"),
    GENTLE_BREEZE(3.4, "Легкий ветер"),
    MODERATE_BREEZE(5.5, "Умеренный ветер"),
    FRESH_BREEZE(8, "Крепкий ветер"),
    STRONG_BREEZE(10.8, "Сильный ветер"),
    HIGH_WIND(13.9, "Ветродуй"),
    GALE(17.2, "Мощный ветродуй"),
    SEVERE_GALE(20.8, "Буря"),
    STORM(24.5, "Мощная буря"),
    VIOLENT_STORM(28.5, "Свирепая буря"),
    TORNADO(32.7, "Смерч");

    private final double windSpeed;
    private final String description;

    public static WindScale fromWindSpeed(final double windSpeed) {
        WindScale windScale = CALM;
        for (int i = 0; i < WindScale.values().length; i++) {
            if (WindScale.values()[i].getWindSpeed() > windSpeed) return windScale;
            windScale = WindScale.values()[i];
        }
        return windScale;
    }
}
