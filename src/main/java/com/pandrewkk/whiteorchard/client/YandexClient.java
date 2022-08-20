package com.pandrewkk.whiteorchard.client;

import com.pandrewkk.whiteorchard.dto.YandexWeatherResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "yandexClient", url = "${yandex.url}")
public interface YandexClient {

    @GetMapping("/forecast")
    YandexWeatherResponseDto getWeather(@RequestParam("lat") double latitude,
                                        @RequestParam("lon") double longitude,
                                        @RequestHeader("X-Yandex-API-Key") String key);
}
