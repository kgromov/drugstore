package org.kgromov.drugstore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sms.settings")
public record SmsSettings(String sid, String token, String phoneNumber) {
}
