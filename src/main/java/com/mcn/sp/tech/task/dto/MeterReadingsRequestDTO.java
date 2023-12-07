package com.mcn.sp.tech.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Schema(description = "DTO for submitting Meter Readings. " +
        "The DTO must contain at least one of elecReading or gasReading, but can also contain both")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterReadingsRequestDTO {

    @Valid
    private SubmittedMeterReading elecReading;
    @Valid
    private SubmittedMeterReading gasReading;

}
