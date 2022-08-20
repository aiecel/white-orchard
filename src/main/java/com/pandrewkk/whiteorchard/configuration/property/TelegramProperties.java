package com.pandrewkk.whiteorchard.configuration.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {

    private final String token;
    private final String chatId;
}
