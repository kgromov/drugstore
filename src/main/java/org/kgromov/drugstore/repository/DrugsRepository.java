package org.kgromov.drugstore.repository;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.model.DrugsImageResponse;
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
    public List<DrugsImageResponse> findAll() {
        return jdbcClient.sql("SELECT * FROM DrugsInfo")
                .query(DrugsImageResponse.class)
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
}
