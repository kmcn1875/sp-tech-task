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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static test.utils.TestDataProvider.createMeterReadingEntity;
import static test.utils.TestDataProvider.createSubmittedMeterReading;

@ExtendWith(MockitoExtension.class)
class MeterReadingsServiceImplTest {

    private static final long ACCOUNT_ID = 12345654321L;
    private static final LocalDate TEN_DAYS_BACK = LocalDate.now().minusDays(10);
    private static final LocalDate FIVE_DAYS_BACK = LocalDate.now().minusDays(5);

    @Mock
    private MeterReadingsRepo meterReadingsRepo;

    @InjectMocks
    private MeterReadingsServiceImpl meterReadingsService;

    @Test
    void testGetMeterReadings_validAccountNumber() throws NoDataFoundException {
        when(meterReadingsRepo.findByAccountIdAndReadingType(ACCOUNT_ID, ReadingType.GAS))
                .thenReturn(
                        List.of(
                                createMeterReadingEntity(ACCOUNT_ID, ReadingType.GAS, 101L, 108976L, 202L, TEN_DAYS_BACK)
                        )
                );
        when(meterReadingsRepo.findByAccountIdAndReadingType(ACCOUNT_ID, ReadingType.ELECTRICITY))
                .thenReturn(
                        List.of(
                                createMeterReadingEntity(ACCOUNT_ID, ReadingType.ELECTRICITY, 105L, 24357L, 205L, FIVE_DAYS_BACK)
                        )
                );

        MeterReadingsResponseDTO result = meterReadingsService.getMeterReadings(ACCOUNT_ID);

        assertThat(result, equalTo(
                MeterReadingsResponseDTO.builder()
                        .accountId(ACCOUNT_ID)
                        .gasReadings(List.of(
                                SavedMeterReading.builder().id(101L).reading(108976L).meterId(202L).date(TEN_DAYS_BACK).build()
                        ))
                        .elecReadings(List.of(
                                SavedMeterReading.builder().id(105L).reading(24357L).meterId(205L).date(FIVE_DAYS_BACK).build()
                        ))
                        .build()
        ));
    }

    @Test
    void testGetMeterReadings_noDataFound() {
        assertThrows(NoDataFoundException.class, () -> {
            meterReadingsService.getMeterReadings(ACCOUNT_ID);
        });
    }

    @Test
    void testSubmitMeterReadings_happyPath() {
        when(meterReadingsRepo.findFirstByAccountIdAndReadingTypeOrderByDateDesc(ACCOUNT_ID, ReadingType.ELECTRICITY))
                .thenReturn(Optional.of(MeterReadingEntity.builder()
                        .readingType(ReadingType.ELECTRICITY).reading(1040L).date(LocalDate.now().minusDays(26L))
                        .build()));
        when(meterReadingsRepo.findFirstByAccountIdAndReadingTypeOrderByDateDesc(ACCOUNT_ID, ReadingType.GAS))
                .thenReturn(Optional.of(MeterReadingEntity.builder()
                        .readingType(ReadingType.GAS).reading(2000L).date(LocalDate.now().minusDays(40L))
                        .build()));

        meterReadingsService.submitMeterReadings(ACCOUNT_ID, MeterReadingsRequestDTO.builder()
                .elecReading(createSubmittedMeterReading(101L, 1050L, LocalDate.now().minusDays(25L)))
                .gasReading(createSubmittedMeterReading(102L, 2010L, LocalDate.now().minusDays(30L)))
                .build());

        verify(meterReadingsRepo, times(2)).save(any());
    }

    @ParameterizedTest
    @CsvSource({
            "1060, 26",
            "1040, 24",
    })
    void testSubmitMeterReadings_invalidElectrictyReading(Long previousReading, Integer previousReadingDate) {
        when(meterReadingsRepo.findFirstByAccountIdAndReadingTypeOrderByDateDesc(ACCOUNT_ID, ReadingType.ELECTRICITY))
                .thenReturn(Optional.of(MeterReadingEntity.builder()
                        .readingType(ReadingType.ELECTRICITY).reading(previousReading)
                        .date(LocalDate.now().minusDays(previousReadingDate))
                        .build()));

        assertThrows(InvalidMeterReadingException.class, () -> {
            meterReadingsService.submitMeterReadings(ACCOUNT_ID, MeterReadingsRequestDTO.builder()
                    .elecReading(createSubmittedMeterReading(101L, 1050L, LocalDate.now().minusDays(25)))
                    .build());
        });

        verify(meterReadingsRepo, times(0)).save(any());
    }

    @ParameterizedTest
    @CsvSource({
            "2020, 35",
            "2000, 25"
    })
    void testSubmitMeterReadings_invalidGasReading(Long previousReading, Integer previousReadingDate) {
        when(meterReadingsRepo.findFirstByAccountIdAndReadingTypeOrderByDateDesc(ACCOUNT_ID, ReadingType.GAS))
                .thenReturn(Optional.of(MeterReadingEntity.builder()
                        .readingType(ReadingType.GAS).reading(previousReading)
                        .date(LocalDate.now().minusDays(previousReadingDate))
                        .build()));

        assertThrows(InvalidMeterReadingException.class, () -> {
            meterReadingsService.submitMeterReadings(ACCOUNT_ID, MeterReadingsRequestDTO.builder()
                    .gasReading(createSubmittedMeterReading( 102L, 2010L, LocalDate.now().minusDays(30)))
                    .build());
        });

        verify(meterReadingsRepo, times(0)).save(any());
    }

}