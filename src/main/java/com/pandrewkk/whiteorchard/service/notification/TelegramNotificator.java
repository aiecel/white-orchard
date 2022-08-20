package com.pandrewkk.whiteorchard.service.notification;

import com.pandrewkk.whiteorchard.client.TelegramClient;
import com.pandrewkk.whiteorchard.configuration.property.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramNotificator implements Notificator {

    private final TelegramProperties properties;
    private final TelegramClient client;

    @Override
    public void sendNotification(final String notificationText) {
        client.sendMessage(properties.getToken(), properties.getChatId(), notificationText);
    }
}
