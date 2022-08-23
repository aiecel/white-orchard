package com.pandrewkk.whiteorchard.mapper;

import com.pandrewkk.whiteorchard.dto.LocationWeatherRecordDto;
import com.pandrewkk.whiteorchard.model.WeatherRecord;
import org.springframework.stereotype.Component;

@Component
public class WeatherRecordMapper {

    public LocationWeatherRecordDto toLocationWeatherRecordDto(WeatherRecord entity) {
        LocationWeatherRecordDto dto = new LocationWeatherRecordDto();
        dto.setDateTime(entity.getDateTime());
        dto.setTemperature(entity.getTemperature());
        dto.setCondition(entity.getCondition().name());
        dto.setCloudness(entity.getCloudness());
        dto.setThunder(entity.isThunder());
        dto.setWindSpeed(entity.getWindSpeed());
        dto.setWindDir(entity.getWindDir());
        dto.setPressure(entity.getPressure());
        dto.setHumidity(entity.getHumidity());
        dto.setDaytime(entity.getDaytime());
        return dto;
    }
}
