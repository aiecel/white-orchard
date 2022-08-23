package com.pandrewkk.whiteorchard.service.weather;

import com.pandrewkk.whiteorchard.model.Location;
import com.pandrewkk.whiteorchard.model.WeatherRecord;
import com.pandrewkk.whiteorchard.repository.LocationRepository;
import com.pandrewkk.whiteorchard.repository.WeatherRecordRepository;
import com.pandrewkk.whiteorchard.service.weather.listener.WeatherListener;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {

    private final LocationRepository locationRepository;
    private final WeatherRecordRepository weatherRecordRepository;
    private final WeatherProvider weatherProvider;
    private final Set<WeatherListener> weatherListeners;

    @Scheduled(fixedRateString = "${weather.rateMinutes}", timeUnit = TimeUnit.MINUTES)
    public void fetchWeatherForAllLocations() {
        List<Location> locations = locationRepository.findAll();
        List<WeatherRecord> weatherRecords = new ArrayList<>();
        log.info("Starting to get weather");

        for (Location location : locations) {
            try {
                final WeatherRecord weather = weatherProvider.getWeather(location);
                log.info("Received weather for location: {}", location);
                weatherRecords.add(weather);
                weatherListeners.forEach(listener -> listener.onWeatherUpdate(weather));
            } catch (final RuntimeException e) {
                log.error("Couldn't get weather for location: {} - {}", location, e.getMessage());
            }
        }

        weatherRecordRepository.saveAll(weatherRecords);
        log.info("Saved {} weather records", weatherRecords.size());
    }

    public List<WeatherRecord> getWeatherHistoryByLocation(final String locationName) {
        Optional<Location> locationByName = locationRepository.findLocationByNameIgnoreCase(locationName);
        if (locationByName.isPresent()) {
            return weatherRecordRepository.findAllByLocationOrderByDateTimeDesc(locationByName.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                format("Location with name %s not found", locationName));
    }
}
