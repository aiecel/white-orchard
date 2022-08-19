package com.pandrewkk.whiteorchard.client;

import com.pandrewkk.whiteorchard.dto.YandexWeatherResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "yandexWeatherClient", url = "${yandex.api.url}")
public interface YandexWeatherClient {

    @GetMapping
    YandexWeatherResponseDto getWeather(@RequestParam("lat") double latitude,
                                        @RequestParam("lon") double longitude,
                                        @RequestHeader("X-Yandex-API-Key") String key);
}
