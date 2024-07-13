package org.kgromov.drugstore.model;

import java.time.LocalDate;

// TODO: decorate with @JsonPropertyDescription;
// More likely change PC to code-generated (e.g. UUID)
public record DrugsInfo(long id, String name, DrugsForm form, Category category, LocalDate expirationDate) {
}
