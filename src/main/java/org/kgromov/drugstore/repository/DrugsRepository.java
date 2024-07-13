package org.kgromov.drugstore.repository;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.model.DrugsInfo;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DrugsRepository {
    private final JdbcClient jdbcClient;

    @Transactional(readOnly = true)
    public List<DrugsInfo> findAll() {
        return jdbcClient.sql("SELECT * FROM DrugsInfo")
                .query(DrugsInfo.class)
                .list();
    }

    @Transactional
    public void save(DrugsInfo drugsInfo) {
        var updated = jdbcClient.sql("INSERT INTO DrugsInfo(name, form, category, expiration_date) values(?,?,?,?)")
                .params(List.of(drugsInfo.name(), drugsInfo.form().name(), drugsInfo.category().name(), drugsInfo.expirationDate()))
                .update();
        Assert.state(updated == 1, STR."Failed to insert drugs info \{drugsInfo.name()}");
    }
}
