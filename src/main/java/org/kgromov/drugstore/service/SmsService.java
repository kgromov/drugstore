package org.kgromov.drugstore.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kgromov.drugstore.config.SmsSettings;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {
    private final SmsSettings smsSettings;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(smsSettings.sid(), smsSettings.token());
    }

    @Async
    public void sendMessage(String toPhoneNumber, String message) {
        Message.creator(new PhoneNumber(toPhoneNumber),
                        new PhoneNumber(smsSettings.phoneNumber()),
                        message)
                .create();
        log.debug("Message {} was sent to {} number", message, toPhoneNumber);
    }
}
