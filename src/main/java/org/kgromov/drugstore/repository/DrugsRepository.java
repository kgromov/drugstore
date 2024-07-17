package org.kgromov.drugstore.repository;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.model.DrugsInfo;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DrugsRepository {
    private final JdbcClient jdbcClient;

    @Transactional(readOnly = true)
    public Optional<DrugsInfo> findById(int id) {
        return jdbcClient.sql("SELECT * FROM DrugsInfo WHERE id = :id")
                .param("id", id)
                .query(DrugsInfo.class)
                .optional();
    }

    @Transactional(readOnly = true)
    public List<DrugsInfo> findAll() {
        return jdbcClient.sql("SELECT * FROM DrugsInfo")
                .query(DrugsInfo.class)
                .list();
    }

    @Transactional(readOnly = true)
    public boolean hasDrugsByDigest(String digest) {
        return jdbcClient.sql("SELECT COUNT(*) FROM DrugsInfo WHERE md5= :digest")
                .param("digest", digest)
                .query(Long.class)
                .single() > 0;
    }

    @Transactional
    public void save(DrugsInfo drugsInfo) {
        var updated = jdbcClient.sql("INSERT INTO DrugsInfo(name, form, category, expiration_date, md5) values(?,?,?,?,?)")
                .params(List.of(
                                drugsInfo.getName(),
                                drugsInfo.getForm().name(),
                                drugsInfo.getCategory().name(),
                                drugsInfo.getExpirationDate(),
                                drugsInfo.getMd5()
                        )
                )
                .update();
        Assert.state(updated == 1, STR."Failed to insert drugs info \{drugsInfo.getName()}");
    }

    @Transactional
    public void update(DrugsInfo drugsInfo) {
        int updated = jdbcClient.sql("UPDATE DrugsInfo SET name = :name, form = :form, category = :category, expiration_date = :expiration_date WHERE id = :id")
                .params(List.of(drugsInfo.getName(), drugsInfo.getForm().name(), drugsInfo.getCategory().name(), drugsInfo.getExpirationDate(), drugsInfo.getId()))
                .update();
        Assert.state(updated == 1, STR."Failed to update drugs info \{drugsInfo.getName()}");
    }

    @Transactional
    public void deleteById(int id) {
        int updated = jdbcClient.sql("DELETE FROM DrugsInfo WHERE id = :id")
                .param("id", id)
                .update();
        Assert.state(updated == 1, STR."Failed to delete drugs info \{id}");
    }
}
