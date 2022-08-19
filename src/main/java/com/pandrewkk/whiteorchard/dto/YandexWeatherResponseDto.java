package com.pandrewkk.whiteorchard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class YandexWeatherResponseDto {

    @JsonProperty("fact")
    private Fact fact;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fact {

        @JsonProperty("temp")
        private double temperature;

        @JsonProperty("condition")
        private String condition;

        @JsonProperty("cloudness")
        private double cloudness;

        @JsonProperty("is_thunder")
        private boolean isThunder;

        @JsonProperty("wind_speed")
        private double windSpeed;

        @JsonProperty("wind_dir")
        private String windDir;

        @JsonProperty("pressure_mm")
        private double pressure;

        @JsonProperty("humidity")
        private double humidity;

        @JsonProperty("daytime")
        private String daytime;
    }
}
