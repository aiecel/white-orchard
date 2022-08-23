package com.pandrewkk.whiteorchard.service.weather.listener;

import com.pandrewkk.whiteorchard.model.WeatherRecord;

public interface WeatherListener {
    void onWeatherUpdate(final WeatherRecord weatherRecord);
}
