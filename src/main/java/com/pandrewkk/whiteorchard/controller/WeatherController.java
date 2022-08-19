package com.pandrewkk.whiteorchard.controller;

import com.pandrewkk.whiteorchard.dto.LocationWeatherRecordDto;
import com.pandrewkk.whiteorchard.mapper.WeatherRecordMapper;
import com.pandrewkk.whiteorchard.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherRecordMapper weatherRecordMapper;

    @GetMapping("/history")
    public List<LocationWeatherRecordDto> getWeatherHistoryByLocation(
            @RequestParam("locationName") String locationName
    ) {
        return weatherService.getWeatherHistoryByLocation(locationName).stream()
                .map(weatherRecordMapper::toLocationWeatherRecordDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/key")
    public ResponseEntity<String> updateYandexApiKey(@RequestBody String key) {
        weatherService.updateYandexApiKey(key);
        return ResponseEntity.ok("Api key has been changed");
    }
}
