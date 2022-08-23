package com.pandrewkk.whiteorchard.service.analysis;

import com.pandrewkk.whiteorchard.model.WeatherRecord;
import com.pandrewkk.whiteorchard.repository.WeatherRecordRepository;
import com.pandrewkk.whiteorchard.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyWeatherStatisticsService {

    private final WeatherRecordRepository weatherRecordRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 00 22 * * *")
    public void analyseDailyWeather() {
        log.info("Starting to analyze daily weather");

        final List<WeatherRecord> todayWeatherRecords = getTodayWeatherRecords();

        if (todayWeatherRecords.size() == 0) {
            notificationService.sendNotification("За сегодня нет записей о погоде :()");
            log.warn("No weather records found for today");
        }

        final WeatherRecord coldestWeather = todayWeatherRecords.stream()
                .min(Comparator.comparingDouble(WeatherRecord::getTemperature)).orElse(new WeatherRecord());

        final WeatherRecord hottestWeather = todayWeatherRecords.stream()
                .max(Comparator.comparingDouble(WeatherRecord::getTemperature)).orElse(new WeatherRecord());

        final double averageTemperature = todayWeatherRecords.stream()
                .mapToDouble(WeatherRecord::getTemperature)
                .average().orElse(0);

        final Set<String> rainLocations = todayWeatherRecords.stream()
                .filter(weather -> !weather.getCondition().isGood())
                .map(rainWeather -> rainWeather.getLocation().getName())
                .collect(Collectors.toSet());

        notificationService.sendNotification(
                constructNotificationText(
                        coldestWeather,
                        hottestWeather,
                        averageTemperature,
                        rainLocations
                )
        );

        log.info("Notifications about daily weather were sent");
    }

    private String constructNotificationText(final WeatherRecord coldestWeather,
                                             final WeatherRecord hottestWeather,
                                             final double averageTemperature,
                                             final Set<String> rainLocations) {

        final StringBuilder text = new StringBuilder("Сводка погоды за ").append(LocalDate.now()).append(":\n\n")

                .append("Самый мороз был в локации ").append(coldestWeather.getLocation().getName())
                .append(" (").append(coldestWeather.getTemperature()).append("C) в ")
                .append(coldestWeather.getDateTime().toLocalTime()).append("\n")

                .append("Самая жара была в локации ").append(hottestWeather.getLocation().getName())
                .append(" (").append(hottestWeather.getTemperature()).append("C) в ")
                .append(hottestWeather.getDateTime().toLocalTime()).append("\n")

                .append("Средняя температура по больнице: ").append(averageTemperature).append("C\n\n");

        if (rainLocations.isEmpty()) {
            text.append("Дождя нигде не было");
        } else {
            text.append("Дождь был в: ").append(String.join(", ", rainLocations));
        }

        return text.toString();
    }

    private List<WeatherRecord> getTodayWeatherRecords() {
        return weatherRecordRepository.getAllByDateTimeBetween(
                ZonedDateTime.of(LocalDate.now(), LocalTime.MIN, ZoneId.systemDefault()),
                ZonedDateTime.of(LocalDate.now(), LocalTime.MAX, ZoneId.systemDefault())
        );
    }
}
