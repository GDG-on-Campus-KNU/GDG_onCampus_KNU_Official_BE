package com.gdsc_knu.official_homepage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@TestComponent
public class ClearDatabase {
    @Autowired private EntityManager em;

    @Transactional
    public void all() {
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        List<String> tables = em.getMetamodel().getEntities().stream()
                .map(EntityType::getName)
                .toList();
        tables.forEach(
                table -> em.createNativeQuery(String.format("TRUNCATE TABLE %s", table)).executeUpdate()
        );
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void each(String table) {
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        em.createNativeQuery(String.format("TRUNCATE TABLE %s", table)).executeUpdate();
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
