package com.pandrewkk.whiteorchard.controller;

import com.pandrewkk.whiteorchard.dto.LocationWeatherRecordDto;
import com.pandrewkk.whiteorchard.mapper.WeatherRecordMapper;
import com.pandrewkk.whiteorchard.service.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherRecordMapper weatherRecordMapper;

    @GetMapping("/history")
    public List<LocationWeatherRecordDto> getWeatherHistoryByLocation(@RequestParam("location") String location) {
        return weatherService.getWeatherHistoryByLocation(location).stream()
                .map(weatherRecordMapper::toLocationWeatherRecordDto)
                .collect(Collectors.toList());
    }
}
