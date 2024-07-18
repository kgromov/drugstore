package org.kgromov.drugstore.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DrugsInfo {
    private int id;
    private String name;
    private DrugsForm form;
    private Category category;
    private LocalDate expirationDate;
    private String md5;
    // private quantity;
}
