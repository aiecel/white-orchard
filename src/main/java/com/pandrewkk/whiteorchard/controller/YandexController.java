package com.pandrewkk.whiteorchard.controller;

import com.pandrewkk.whiteorchard.service.weather.YandexWeatherProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yandex")
@RequiredArgsConstructor
public class YandexController {

    private final YandexWeatherProvider yandexWeatherProvider;

    @PostMapping("/key")
    public ResponseEntity<String> updateYandexApiKey(@RequestBody String key) {
        yandexWeatherProvider.updateApiKey(key);
        return ResponseEntity.ok("Api key has been changed");
    }
}
