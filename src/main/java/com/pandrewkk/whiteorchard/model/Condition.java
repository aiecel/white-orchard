package com.pandrewkk.whiteorchard.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Condition {

    CLEAR("Ясно"),
    PARTLY_CLOUDY("Ясненько"),
    CLOUDY("Облачно"),
    OVERCAST("Пасмурно"),
    DRIZZLE("Грибной дождик"),
    LIGHT_RAIN("Дождик"),
    RAIN("Дождь"),
    MODERATE_RAIN("Дождевище"),
    HEAVY_RAIN("Ливень"),
    CONTINUOUS_HEAVY_RAIN("Плотный ливень"),
    SHOWERS("Водолей"),
    WET_SNOW("Снегодождь"),
    LIGHT_SNOW("Небольшой снегопад"),
    SNOW("Снегопад"),
    SNOW_SHOWERS("Снегопадище"),
    HAIL("Градобитие"),
    THUNDERSTORM("Гром"),
    THUNDERSTORM_WITH_RAIN("Гроза"),
    THUNDERSTORM_WITH_HAIL("Гнев богов");

    private static final Set<Condition> GOOD_CONDITIONS =
            Set.of(CLEAR, PARTLY_CLOUDY, CLOUDY, OVERCAST);

    private static final Set<Condition> EXTREME_CONDITIONS =
            Set.of(SHOWERS, SNOW_SHOWERS, HAIL, THUNDERSTORM, THUNDERSTORM_WITH_RAIN, THUNDERSTORM_WITH_HAIL);

    private final String description;

    public boolean isGood() {
        return GOOD_CONDITIONS.contains(this);
    }

    public boolean isExtreme() {
        return EXTREME_CONDITIONS.contains(this);
    }
}
