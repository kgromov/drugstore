package org.kgromov.drugstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrugsInfoScheduler {
    private final DrugsInfoService drugsInfoService;

    @Scheduled(cron = "0 0 0 * * *")
    public void notifyRecipientsOnExpired() {
        log.info("Notify recipients about expired drugs");
        drugsInfoService.notifyRecipientsOnExpired();
    }
}
