package com.pandrewkk.whiteorchard.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class LocationWeatherRecordDto {

    private ZonedDateTime dateTime;

    private double temperature;

    private String condition;

    private double cloudness;

    private boolean isThunder;

    private double windSpeed;

    private String windDir;

    private double pressure;

    private double humidity;

    private String daytime;
}
