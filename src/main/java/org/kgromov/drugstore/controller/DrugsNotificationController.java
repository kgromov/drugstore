package org.kgromov.drugstore.controller;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.service.DrugsInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drugs-notification")
@RequiredArgsConstructor
public class DrugsNotificationController {
    private final DrugsInfoService drugsInfoService;

    @GetMapping("/expired")
    public void notifyOnExpired() {
        drugsInfoService.notifyRecipientsOnExpired();
    }
}
