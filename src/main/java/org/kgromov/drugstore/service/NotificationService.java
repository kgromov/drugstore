package org.kgromov.drugstore.service;

import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.model.Recipient;

public interface NotificationService {
    void onDrugExpired(Recipient recipient, DrugsInfo drugsInfo);
}
