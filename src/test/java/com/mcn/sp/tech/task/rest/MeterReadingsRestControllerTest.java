package com.mcn.sp.tech.task.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcn.sp.tech.task.dto.SavedMeterReading;
import com.mcn.sp.tech.task.dto.MeterReadingsResponseDTO;
import com.mcn.sp.tech.task.dto.MeterReadingsRequestDTO;
import com.mcn.sp.tech.task.service.MeterReadingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static test.utils.TestDataProvider.createSavedMeterReading;
import static test.utils.TestDataProvider.createSubmittedMeterReading;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MeterReadingsRestController.class)
class MeterReadingsRestControllerTest {

    private static final String PATH = "/api/smart/reads/{accountId}";
    private static final long ACCOUNT_ID = 12345654321L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeterReadingsService meterReadingsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetMeterReadings_HappyPath() throws Exception {
        MeterReadingsResponseDTO readingsDTO = createSampleResponse();
        when(meterReadingsService.getMeterReadings(ACCOUNT_ID))
                .thenReturn(readingsDTO);

        mockMvc.perform(get(PATH, ACCOUNT_ID)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(readingsDTO)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-1024"})
    void testGetMeterReadings_InvalidParameter(String accountId) {
        NestedServletException nestedServletException = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(get(PATH, accountId)
                            .contentType("application/json"))
                    .andExpect(status().isBadRequest());
        });
        ConstraintViolationException exception = (ConstraintViolationException) nestedServletException.getCause();
        assertThat(exception.getConstraintViolations().size(), equalTo(1));
        ConstraintViolation<?> constraintViolation = exception.getConstraintViolations().iterator().next();
        assertAll(
                () -> assertThat(constraintViolation.getPropertyPath().toString(), equalTo("getMeterReadings.accountId")),
                () -> assertThat(exception.getConstraintViolations().iterator().next().getMessage(),
                        equalTo("must be greater than 0"))
        );
    }

    @ParameterizedTest
    @MethodSource("testSubmitMeterReadingsSources")
    void testSubmitMeterReadings_HappyPath(MeterReadingsRequestDTO meterReadingsDTO) throws Exception {
        String json = objectMapper.writeValueAsString(meterReadingsDTO);
        mockMvc.perform(post(PATH, ACCOUNT_ID)
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isAccepted());
    }

    private static Stream<Arguments> testSubmitMeterReadingsSources() {
        return Stream.of(
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .elecReading(createSubmittedMeterReading(101L,1050L,LocalDate.now().minusDays(25L)))
                        .build()),
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .gasReading(createSubmittedMeterReading(102L, 2010L, LocalDate.now().minusDays(30L)))
                        .build()),
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .elecReading(createSubmittedMeterReading(101L,1050L,LocalDate.now().minusDays(25L)))
                        .gasReading(createSubmittedMeterReading(102L, 2010L, LocalDate.now().minusDays(30L)))
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("submitMeterReadingsInvalidDataSources")
    void testSubmitMeterReadings_invalidData(MeterReadingsRequestDTO meterReadingsDTO) throws Exception {
        mockMvc.perform(post(PATH, ACCOUNT_ID)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(meterReadingsDTO)))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> submitMeterReadingsInvalidDataSources() {
        return Stream.of(
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .elecReading(createSubmittedMeterReading(null, 2010L, LocalDate.now().minusDays(30L)))
                        .build()),
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .elecReading(createSubmittedMeterReading(102L, null, LocalDate.now().minusDays(30L)))
                        .build()),
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .elecReading(createSubmittedMeterReading(102L, 2010L, null))
                        .build()),
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .elecReading(createSubmittedMeterReading(0L, 2010L, LocalDate.now().minusDays(30L)))
                        .build()),
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .elecReading(createSubmittedMeterReading(2L,0L, LocalDate.now().minusDays(30L)))
                        .build()),
                Arguments.of(MeterReadingsRequestDTO.builder()
                        .elecReading(createSubmittedMeterReading(102L, 2010L, LocalDate.now().plusDays(1L)))
                        .build())
        );
    }

    private MeterReadingsResponseDTO createSampleResponse() {
        return MeterReadingsResponseDTO.builder()
                .accountId(ACCOUNT_ID)
                .elecReadings(List.of(createSavedMeterReading(2L,102L, 2010L, LocalDate.now().minusDays(30L))))
                .gasReadings(List.of(createSavedMeterReading(2L,102L, 2010L, LocalDate.now().minusDays(30L))))
                .build();
    }

}