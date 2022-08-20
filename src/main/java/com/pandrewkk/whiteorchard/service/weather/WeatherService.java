package com.pandrewkk.whiteorchard.service.weather;

import com.pandrewkk.whiteorchard.model.Location;
import com.pandrewkk.whiteorchard.model.WeatherRecord;
import com.pandrewkk.whiteorchard.repository.LocationRepository;
import com.pandrewkk.whiteorchard.repository.WeatherRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {

    private final LocationRepository locationRepository;
    private final WeatherRecordRepository weatherRecordRepository;
    private final WeatherProvider weatherProvider;

    @Scheduled(fixedRateString = "${weather.rateMinutes}", timeUnit = TimeUnit.MINUTES)
    public void fetchWeatherForAllLocations() {
        List<Location> locations = locationRepository.findAll();
        List<WeatherRecord> weatherRecords = new ArrayList<>();
        log.info("Starting to get weather");

        for (Location location : locations) {
            try {
                weatherRecords.add(weatherProvider.getWeather(location));
                log.info("Received weather for location: {}", location);
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

    public List<WeatherRecord> getWeatherRecordsForDate(final LocalDate date) {
        return weatherRecordRepository.getAllByDateTimeBetween(
                ZonedDateTime.of(date, LocalTime.MIN, ZoneId.systemDefault()),
                ZonedDateTime.of(date, LocalTime.MAX, ZoneId.systemDefault())
        );
    }
}
