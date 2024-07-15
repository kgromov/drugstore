package org.kgromov.drugstore.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDate;

public record DrugsImageResponse(@JsonPropertyDescription("The drugs name") String name,
                                 @JsonPropertyDescription("The drugs form") DrugsForm form,
                                 @JsonPropertyDescription("The drugs category") Category category,
                                 @JsonPropertyDescription("The drugs expirationDate") LocalDate expirationDate) {
}
