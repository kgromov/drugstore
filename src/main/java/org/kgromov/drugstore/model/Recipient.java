package org.kgromov.drugstore.model;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Recipient {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
