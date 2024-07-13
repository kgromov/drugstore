package org.kgromov.drugstore;

import org.kgromov.drugstore.model.Category;
import org.kgromov.drugstore.model.DrugsForm;
import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.repository.DrugsRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class DrugstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrugstoreApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(DrugsRepository drugsRepository) {
        return args -> {
            var antibiotics = new DrugsInfo(1, "Antibiotics", DrugsForm.TABLET, Category.CURES, LocalDate.now().plusDays(3));
            drugsRepository.save(antibiotics);
            drugsRepository.findAll().forEach(System.out::println);
        };
    }
}
