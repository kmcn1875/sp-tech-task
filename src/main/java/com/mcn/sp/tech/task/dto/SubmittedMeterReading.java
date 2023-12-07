package com.mcn.sp.tech.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Details of an individual (submitted) meter reading")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmittedMeterReading {

    @Schema(requiredMode = REQUIRED, minimum = "1")
    @NotNull
    @Positive
    private Long meterId;
    @Schema(requiredMode = REQUIRED, minimum = "1")
    @NotNull
    @Positive
    private Long reading;
    @Schema(requiredMode = REQUIRED, pattern = "yyyy-mm-dd")
    @NotNull
    @PastOrPresent
    private LocalDate date;

}
