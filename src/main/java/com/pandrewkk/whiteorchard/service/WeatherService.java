package com.pandrewkk.whiteorchard.service;

import com.pandrewkk.whiteorchard.client.YandexWeatherClient;
import com.pandrewkk.whiteorchard.dto.YandexWeatherResponseDto;
import com.pandrewkk.whiteorchard.model.Location;
import com.pandrewkk.whiteorchard.model.WeatherRecord;
import com.pandrewkk.whiteorchard.model.YandexApiKey;
import com.pandrewkk.whiteorchard.repository.LocationRepository;
import com.pandrewkk.whiteorchard.repository.WeatherRecordRepository;
import com.pandrewkk.whiteorchard.repository.YandexApiKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {

    private final YandexWeatherClient yandexWeatherClient;
    private final LocationRepository locationRepository;
    private final WeatherRecordRepository weatherRecordRepository;
    private final YandexApiKeyRepository yandexApiKeyRepository;

    public void updateYandexApiKey(String newApiKey) {
        YandexApiKey yandexApiKey = new YandexApiKey();
        yandexApiKey.setId(0L);
        yandexApiKey.setKey(newApiKey);
        yandexApiKeyRepository.save(yandexApiKey);
        log.info("Api key has been changed: {}", newApiKey);
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.MINUTES)
    public void getAllLocationsWeatherRecords() {
        List<Location> locations = locationRepository.findAll();
        log.info("Starting to get weather");

        for (Location location : locations) {
            try {
                weatherRecordRepository.save(getWeatherRecord(location));
                log.info("Saved weather for location: {}", location.getName());
            } catch (RuntimeException e) {
                log.error("Couldn't get weather for location: {} - {}", location.getName(), e.getMessage());
            }
        }
    }

    public List<WeatherRecord> getWeatherHistoryByLocation(String locationName) {
        Optional<Location> locationByName = locationRepository.findLocationByNameIgnoreCase(locationName);
        if (locationByName.isPresent()) {
            return weatherRecordRepository.findAllByLocationOrderByDateTimeDesc(locationByName.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                format("Location with name %s not found", locationName));
    }

    private String getApiKey() {
        return yandexApiKeyRepository.findById(0L)
                .orElseThrow(() -> new RuntimeException("Api key not found"))
                .getKey();
    }

    private WeatherRecord getWeatherRecord(Location location) {
        YandexWeatherResponseDto weather =
                yandexWeatherClient.getWeather(location.getLatitude(), location.getLongitude(), getApiKey());

        WeatherRecord weatherRecord = new WeatherRecord();
        weatherRecord.setDateTime(ZonedDateTime.now());
        weatherRecord.setLocation(location);
        weatherRecord.setTemperature(weather.getFact().getTemperature());
        weatherRecord.setCondition(weather.getFact().getCondition());
        weatherRecord.setCloudness(weather.getFact().getCloudness());
        weatherRecord.setThunder(weather.getFact().isThunder());
        weatherRecord.setWindSpeed(weather.getFact().getWindSpeed());
        weatherRecord.setWindDir(weather.getFact().getWindDir());
        weatherRecord.setPressure(weather.getFact().getPressure());
        weatherRecord.setHumidity(weather.getFact().getHumidity());
        weatherRecord.setDaytime(weather.getFact().getDaytime());

        return weatherRecord;
    }
}
