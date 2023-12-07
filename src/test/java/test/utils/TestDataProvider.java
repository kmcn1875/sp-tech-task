package test.utils;

import com.mcn.sp.tech.task.dto.SavedMeterReading;
import com.mcn.sp.tech.task.dto.SubmittedMeterReading;
import com.mcn.sp.tech.task.entity.MeterReadingEntity;
import com.mcn.sp.tech.task.entity.ReadingType;

import java.time.LocalDate;

public class TestDataProvider {

    public static SavedMeterReading createSavedMeterReading(Long id, Long meterId, Long reading, LocalDate date) {
        return SavedMeterReading.builder()
                .id(id)
                .meterId(meterId)
                .reading(reading)
                .date(date)
                .build();
    }

    public static SubmittedMeterReading createSubmittedMeterReading(Long meterId, Long reading, LocalDate date) {
        return SubmittedMeterReading.builder()
                .meterId(meterId)
                .reading(reading)
                .date(date)
                .build();
    }

    public static MeterReadingEntity createMeterReadingEntity(long accountId, ReadingType gas, Long id, Long reading,
                                                        Long meterId, LocalDate date) {
        return MeterReadingEntity.builder()
                .accountId(accountId)
                .readingType(gas)
                .id(id)
                .reading(reading)
                .meterId(meterId)
                .date(date)
                .build();
    }
}
