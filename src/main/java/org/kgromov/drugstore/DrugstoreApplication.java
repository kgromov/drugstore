package org.kgromov.drugstore;

import org.kgromov.drugstore.config.SmsSettings;
import org.kgromov.drugstore.model.Category;
import org.kgromov.drugstore.model.DrugsForm;
import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.repository.DrugsRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
@EnableConfigurationProperties({SmsSettings.class})
public class DrugstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrugstoreApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(DrugsRepository drugsRepository) {
        return args -> {
            var antibiotics = DrugsInfo.builder()
                    .name("Antibiotics")
                    .category(Category.CURES)
                    .form(DrugsForm.TABLET)
                    .expirationDate(LocalDate.now().plusDays(3))
                    .md5(UUID.randomUUID().toString().substring(0, 32))
                    .build();
            drugsRepository.save(antibiotics);
            drugsRepository.findAll().forEach(System.out::println);
        };
    }
}
