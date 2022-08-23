package com.pandrewkk.whiteorchard.service.weather;

import com.pandrewkk.whiteorchard.model.Location;
import com.pandrewkk.whiteorchard.model.WeatherRecord;

public interface WeatherProvider {
    WeatherRecord getWeather(final Location location);
}
