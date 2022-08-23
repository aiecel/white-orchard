package com.pandrewkk.whiteorchard.service.weather;

import com.pandrewkk.whiteorchard.client.YandexClient;
import com.pandrewkk.whiteorchard.dto.YandexWeatherResponseDto;
import com.pandrewkk.whiteorchard.model.Condition;
import com.pandrewkk.whiteorchard.model.Location;
import com.pandrewkk.whiteorchard.model.WeatherRecord;
import com.pandrewkk.whiteorchard.model.YandexApiKey;
import com.pandrewkk.whiteorchard.repository.YandexApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class YandexWeatherProvider implements WeatherProvider {

    private final YandexClient yandexClient;
    private final YandexApiKeyRepository yandexApiKeyRepository;

    @Cacheable("yandexApiKey")
    public String getApiKey() {
        return yandexApiKeyRepository.findById(0L)
                .orElseThrow(() -> new RuntimeException("Api key not found"))
                .getKey();
    }

    @CacheEvict("yandexApiKey")
    public void updateApiKey(final String newApiKey) {
        YandexApiKey yandexApiKey = new YandexApiKey();
        yandexApiKey.setId(0L);
        yandexApiKey.setKey(newApiKey);
        yandexApiKeyRepository.save(yandexApiKey);
        log.info("Api key has been changed to {}", newApiKey);
    }

    @Override
    public WeatherRecord getWeather(Location location) {
        YandexWeatherResponseDto weatherResponse = yandexClient.getWeather(
                location.getLatitude(),
                location.getLongitude(),
                getApiKey()
        );
        return toWeatherRecord(location, weatherResponse);
    }

    private WeatherRecord toWeatherRecord(final Location location, final YandexWeatherResponseDto weatherResponse) {
        WeatherRecord weatherRecord = new WeatherRecord();
        weatherRecord.setDateTime(ZonedDateTime.now());
        weatherRecord.setLocation(location);
        weatherRecord.setTemperature(weatherResponse.getFact().getTemperature());
        weatherRecord.setCondition(mapCondition(weatherResponse.getFact().getCondition()));
        weatherRecord.setCloudness(weatherResponse.getFact().getCloudness());
        weatherRecord.setThunder(weatherResponse.getFact().isThunder());
        weatherRecord.setWindSpeed(weatherResponse.getFact().getWindSpeed());
        weatherRecord.setWindDir(weatherResponse.getFact().getWindDir());
        weatherRecord.setPressure(weatherResponse.getFact().getPressure());
        weatherRecord.setHumidity(weatherResponse.getFact().getHumidity());
        weatherRecord.setDaytime(weatherResponse.getFact().getDaytime());
        return weatherRecord;
    }

    private Condition mapCondition(final String condition) {
        return Condition.valueOf(condition.toUpperCase().replace("-", "_"));
    }
}
