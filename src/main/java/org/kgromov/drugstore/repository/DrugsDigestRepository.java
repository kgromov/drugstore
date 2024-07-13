package org.kgromov.drugstore.repository;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.model.DrugImageDigest;
import org.kgromov.drugstore.model.DrugsInfo;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DrugsDigestRepository {
    private final JdbcClient jdbcClient;

    @Transactional(readOnly = true)
    public boolean hasDigestByDragsInfo(DrugsInfo drugsInfo) {
        return jdbcClient.sql("SELECT COUNT(*) FROM DrugsSourceDigest WHERE drug_id= :id")
                .param("id", drugsInfo.id())
                .query(Long.class)
                .single() > 0;
    }

    @Transactional(readOnly = true)
    public boolean hasDigestByDigest(String digest) {
        return jdbcClient.sql("SELECT COUNT(*) FROM DrugsSourceDigest WHERE md5= :digest")
                .param("digest", digest)
                .query(Long.class)
                .single() > 0;
    }
    @Transactional
    public void save(DrugImageDigest drugsDigest) {
        var updated = jdbcClient.sql("INSERT INTO DrugsSourceDigest(md5, drug_id) values(?,?)")
                .params(List.of(drugsDigest.md5(), drugsDigest.drug().id()))
                .update();
        Assert.state(updated == 1, STR."Failed to insert drugs info digest\{drugsDigest.drug().name()}");
    }
}
