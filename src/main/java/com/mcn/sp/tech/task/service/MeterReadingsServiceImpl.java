package com.mcn.sp.tech.task.service;

import com.mcn.sp.tech.task.dto.SavedMeterReading;
import com.mcn.sp.tech.task.dto.SubmittedMeterReading;
import com.mcn.sp.tech.task.dto.MeterReadingsResponseDTO;
import com.mcn.sp.tech.task.dto.MeterReadingsRequestDTO;
import com.mcn.sp.tech.task.entity.MeterReadingEntity;
import com.mcn.sp.tech.task.entity.ReadingType;
import com.mcn.sp.tech.task.exception.InvalidMeterReadingException;
import com.mcn.sp.tech.task.exception.NoDataFoundException;
import com.mcn.sp.tech.task.repo.MeterReadingsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeterReadingsServiceImpl implements MeterReadingsService {

    private final MeterReadingsRepo meterReadingsRepo;

    /**
     * See @{@link MeterReadingsService#getMeterReadings}
     */
    @Override
    public MeterReadingsResponseDTO getMeterReadings(Long accountId) throws NoDataFoundException {

        List<MeterReadingEntity> gasReadings = meterReadingsRepo.findByAccountIdAndReadingType(accountId, ReadingType.GAS);
        List<MeterReadingEntity> elecReadings = meterReadingsRepo.findByAccountIdAndReadingType(accountId, ReadingType.ELECTRICITY);

        if (gasReadings.isEmpty() && elecReadings.isEmpty()) {
            log.warn("No data found for Account ID {}", accountId);
            throw new NoDataFoundException("No data found for accountId: " + accountId);
        }
        log.info("Returning saved readings for Account ID {}", accountId);
        return MeterReadingsResponseDTO.builder()
                .accountId(accountId)
                .elecReadings(mapReadings(elecReadings))
                .gasReadings(mapReadings(gasReadings))
                .build();
    }

    /**
     * See @{@link MeterReadingsService#submitMeterReadings}
     */
    @Override
    public void submitMeterReadings(Long accountId, MeterReadingsRequestDTO meterReadingsDTO) {
        if (meterReadingsDTO.getGasReading() != null) {
            log.info("Checking input Gas readings for Account ID {}...", accountId);
            checkInputMeterReading(accountId, ReadingType.GAS, meterReadingsDTO.getGasReading());
            log.info("Saving input Gas readings for Account ID {}...", accountId);
            meterReadingsRepo.save(getMeterReadingEntity(accountId, meterReadingsDTO.getGasReading(), ReadingType.GAS));
        }
        if (meterReadingsDTO.getElecReading() != null) {
            log.info("Checking input Electricity readings for Account ID {}...", accountId);
            checkInputMeterReading(accountId, ReadingType.ELECTRICITY, meterReadingsDTO.getElecReading());
            log.info("Saving input Electricity readings for Account ID {}...", accountId);
            meterReadingsRepo.save(getMeterReadingEntity(accountId, meterReadingsDTO.getElecReading(), ReadingType.ELECTRICITY));
        }
    }

    private void checkInputMeterReading(Long accountId, ReadingType readingType, SubmittedMeterReading submittedMeterReading) {
        String invalidDateMessageBase = "The submitted %s Reading, with date of %s, is not after the most recently submitted reading with date %s";
        String invalidReadingMessageBase = "The submitted %s Reading, with a value of %s, is not greater than the most recently submitted reading with value %s";
        meterReadingsRepo.findFirstByAccountIdAndReadingTypeOrderByDateDesc(accountId, readingType)
                .ifPresent(entity -> {
                    LocalDate entityDate = entity.getDate();
                    Long entityReading = entity.getReading();
                    if (!submittedMeterReading.getDate().isAfter(entityDate)) {
                        log.error("Input reading for Account ID {} has an invalid date", accountId);
                        throw new InvalidMeterReadingException(
                                String.format(invalidDateMessageBase, readingType,
                                        submittedMeterReading.getDate(), entityDate)
                        );
                    }
                    if (submittedMeterReading.getReading() <= entityReading) {
                        log.error("Input reading for Account ID {} has an invalid meter reading", accountId);
                        throw new InvalidMeterReadingException(
                                String.format(invalidReadingMessageBase, readingType,
                                        submittedMeterReading.getReading(), entityReading)
                        );
                    }
                });
    }

    private MeterReadingEntity getMeterReadingEntity(Long accountNumber, SubmittedMeterReading meterReading, ReadingType readingType) {
        return MeterReadingEntity.builder()
                .accountId(accountNumber)
                .readingType(readingType)
                .meterId(meterReading.getMeterId())
                .reading(meterReading.getReading())
                .date(meterReading.getDate())
                .build();
    }

    private List<SavedMeterReading> mapReadings(List<MeterReadingEntity> meterReadingEntities) {
        return meterReadingEntities.stream().map(entity ->
                SavedMeterReading.builder()
                        .date(entity.getDate())
                        .id(entity.getId())
                        .meterId(entity.getMeterId())
                        .reading(entity.getReading())
                        .build()
        ).collect(Collectors.toList());
    }

}
