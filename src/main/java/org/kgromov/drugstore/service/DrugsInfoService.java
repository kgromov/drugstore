package org.kgromov.drugstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.repository.DrugsRepository;
import org.kgromov.drugstore.repository.RecipientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrugsInfoService {
    private final NotificationService notificationService;
    private final DrugsRepository drugsRepository;
    private final RecipientRepository recipientRepository;

    public void notifyRecipientsOnExpired() {
        var recipients = recipientRepository.findAll();
        drugsRepository.findAll()
                .stream()
                .filter(this::isExpired)
                .forEach(drugsInfo -> {
                    log.info("Drug {} expired on {}. Inform recipients.", drugsInfo.getName(), drugsInfo.getExpirationDate());
                    recipients.forEach(recipient -> notificationService.onDrugExpired(recipient, drugsInfo));
                });
    }

    private boolean isExpired(DrugsInfo drugsInfo) {
        return drugsInfo.getExpirationDate().isBefore(LocalDate.now().plusDays(1));
    }
}
