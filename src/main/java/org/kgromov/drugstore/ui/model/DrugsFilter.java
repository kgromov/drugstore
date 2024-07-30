package org.kgromov.drugstore.ui.model;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import org.kgromov.drugstore.model.DrugsInfo;

import java.util.function.Predicate;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class DrugsFilter {
    private final GridListDataView<DrugsInfo> dataView;

    private String name;
    private String form;
    private String category;
    private String expirationDate;

    public DrugsFilter(GridListDataView<DrugsInfo> dataView) {
        this.dataView = dataView;
        this.dataView.addFilter(this::test);
    }

    public void setName(String name) {
        this.name = name;
        this.dataView.refreshAll();
    }

    public void setForm(String form) {
        this.form = form;
        this.dataView.refreshAll();
    }

    public void setCategory(String category) {
        this.category = category;
        this.dataView.refreshAll();
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        this.dataView.refreshAll();
    }

    private boolean test(DrugsInfo drugsInfo) {
        Predicate<DrugsInfo> matchesByName = i -> matches(i.getName(), this.name);
        Predicate<DrugsInfo> matchesByForm = i -> matches(i.getForm().name(), this.form);
        Predicate<DrugsInfo> matchesByCategory = i -> matches(i.getCategory().name(), this.category);
        Predicate<DrugsInfo> matchesByExpirationDate = i -> matches(i.getExpirationDate().format(ISO_DATE), this.expirationDate);
        return matchesByName
                .and(matchesByCategory)
                .and(matchesByForm)
                .and(matchesByExpirationDate)
                .test(drugsInfo);
    }

    private boolean matches(String value, String searchTerm) {
        return isBlank(searchTerm) || containsIgnoreCase(value, searchTerm);
    }
}
