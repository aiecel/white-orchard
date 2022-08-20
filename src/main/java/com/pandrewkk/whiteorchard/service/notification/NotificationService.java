package com.pandrewkk.whiteorchard.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Set<Notificator> notificators;

    public void sendNotification(final String notificationText) {
        notificators.forEach(notificator -> {
            try {
                notificator.sendNotification(notificationText);
                log.info(
                        "Sent notification via {}: {}",
                        notificator.getClass().getSimpleName(),
                        notificationText.replace("\n", "|")
                );
            } catch (final RuntimeException e) {
                log.error(
                        "Couldn't send notification via {}: {}",
                        notificator.getClass().getSimpleName(),
                        e.getMessage()
                );
            }
        });
    }
}
