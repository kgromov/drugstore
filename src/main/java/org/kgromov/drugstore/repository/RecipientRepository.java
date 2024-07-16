package org.kgromov.drugstore.repository;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.model.Recipient;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecipientRepository {
    private final JdbcClient jdbcClient;

    public List<Recipient> findAll() {
        return jdbcClient.sql("SELECT * FROM Recipient")
                .query(Recipient.class)
                .list();
    }
}
