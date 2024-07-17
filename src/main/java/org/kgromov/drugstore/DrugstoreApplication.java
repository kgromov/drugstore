package org.kgromov.drugstore;

import org.kgromov.drugstore.config.SmsSettings;
import org.kgromov.drugstore.repository.DrugsRepository;
import org.kgromov.drugstore.repository.RecipientRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties({SmsSettings.class})
@SpringBootApplication
public class DrugstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrugstoreApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(DrugsRepository drugsRepository, RecipientRepository recipientRepository) {
        return _ -> {
            drugsRepository.findAll().forEach(System.out::println);
            recipientRepository.findAll().forEach(System.out::println);
        };
    }
}
