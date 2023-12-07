package com.mcn.sp.tech.task.repo;

import com.mcn.sp.tech.task.entity.MeterReadingEntity;
import com.mcn.sp.tech.task.entity.ReadingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterReadingsRepo extends JpaRepository<MeterReadingEntity, Long> {

    List<MeterReadingEntity> findByAccountIdAndReadingType(Long accountId, ReadingType readingType);

    Optional<MeterReadingEntity>  findFirstByAccountIdAndReadingTypeOrderByDateDesc(Long accountId, ReadingType readingType);

}
