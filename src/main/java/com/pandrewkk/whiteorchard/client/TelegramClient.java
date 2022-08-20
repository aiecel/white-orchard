package com.pandrewkk.whiteorchard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "telegramClient", url = "${telegram.url}")
public interface TelegramClient {

    @PostMapping("/bot{token}/sendMessage")
    void sendMessage(
            @PathVariable("token") String token,
            @RequestParam("chat_id") String chatId,
            @RequestParam("text") String text
    );
}
