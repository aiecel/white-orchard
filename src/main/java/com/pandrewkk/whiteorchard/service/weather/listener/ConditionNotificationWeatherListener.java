package com.pandrewkk.whiteorchard.service.weather.listener;

import com.pandrewkk.whiteorchard.model.Condition;
import com.pandrewkk.whiteorchard.model.Location;
import com.pandrewkk.whiteorchard.model.WeatherRecord;
import com.pandrewkk.whiteorchard.model.WindScale;
import com.pandrewkk.whiteorchard.repository.WeatherRecordRepository;
import com.pandrewkk.whiteorchard.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConditionNotificationWeatherListener implements WeatherListener {

    private final WeatherRecordRepository weatherRecordRepository;
    private final NotificationService notificationService;

    @Override
    public void onWeatherUpdate(final WeatherRecord weatherRecord) {
        final List<WeatherRecord> lastHourWeatherInLocation = getLastHourWeatherInLocation(weatherRecord.getLocation());

        if (!weatherRecord.getCondition().isGood()) {
            final boolean isConditionRepeating = lastHourWeatherInLocation
                    .stream()
                    .anyMatch(weather -> weather.getCondition() == weatherRecord.getCondition());

            if (!isConditionRepeating) sendConditionNotification(weatherRecord);
        }

        if (weatherRecord.getWindSpeed() >= WindScale.HIGH_WIND.getWindSpeed()) {
            final boolean isWindSpeedRepeating = lastHourWeatherInLocation
                    .stream()
                    .anyMatch(weather -> {
                        final WindScale weatherWind = WindScale.fromWindSpeed(weather.getWindSpeed());
                        final WindScale currentWind = WindScale.fromWindSpeed(weatherRecord.getWindSpeed());
                        return weatherWind.ordinal() >= currentWind.ordinal();
                    });

            if (!isWindSpeedRepeating) sendWindNotification(weatherRecord);
        }
    }

    private List<WeatherRecord> getLastHourWeatherInLocation(final Location location) {
        return weatherRecordRepository.getAllByLocationAndDateTimeBetween(
                location,
                ZonedDateTime.now().minusHours(1),
                ZonedDateTime.now()
        );
    }

    private void sendConditionNotification(final WeatherRecord weatherRecord) {
        final Condition condition = weatherRecord.getCondition();
        final StringBuilder text = new StringBuilder();

        if (condition.isExtreme()) text.append("ВНИМАНИЕ!!! D: ВСЕМ! ");

        text.append("В локации ").append(weatherRecord.getLocation().getName())
                .append(" начинается ").append(condition.getDescription().toLowerCase());

        if (condition.isExtreme()) text.append("! БУДЬТЕ ОСТОРОЖНЫ!!!");

        notificationService.sendNotification(text.toString());
    }

    private void sendWindNotification(final WeatherRecord weatherRecord) {
        final double windSpeed = weatherRecord.getWindSpeed();
        final boolean isExtreme = windSpeed >= WindScale.STORM.getWindSpeed();
        final String description = WindScale.fromWindSpeed(windSpeed).getDescription();
        final StringBuilder text = new StringBuilder();

        if (isExtreme) text.append("ДЕРЖИТЕСЬ!!! ");

        text.append("В локации ").append(weatherRecord.getLocation().getName())
                .append(" начинается ").append(isExtreme ? description.toUpperCase() : description.toLowerCase());

        text.append("! БУДЬТЕ ОСТОРОЖНЫ!!!");

        notificationService.sendNotification(text.toString());
    }
}
