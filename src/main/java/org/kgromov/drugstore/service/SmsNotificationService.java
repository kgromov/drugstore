package org.kgromov.drugstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.model.Recipient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsNotificationService implements NotificationService {
    private final SmsService smsService;

    @Override
    public void onDrugExpired(Recipient recipient, DrugsInfo drugsInfo) {
        log.info("Drug {} expired on {}", drugsInfo.getName(), drugsInfo.getExpirationDate());
        String message =  STR."{name =  \{drugsInfo.getName()} , category = \{drugsInfo.getCategory()} }";
        smsService.sendMessage(recipient.getPhoneNumber(), message);
    }
}
