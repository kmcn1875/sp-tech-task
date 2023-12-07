package com.mcn.sp.tech.task.it.utils;

import com.mcn.sp.tech.task.entity.MeterReadingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class DatabaseUtils {

    private final EntityManager entityManager;

    @Transactional
    public <T> void insertData(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
    }

    public List<MeterReadingEntity> findAllMeterReadings() {
        return entityManager.createQuery("select m from MeterReadingEntity m", MeterReadingEntity.class).getResultList();
    }

    @Transactional
    public void clearMeterReadings() {
        entityManager.createQuery("DELETE FROM MeterReadingEntity").executeUpdate();
        entityManager.flush();
    }
}
