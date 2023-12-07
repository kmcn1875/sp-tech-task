package com.mcn.sp.tech.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "The full list of meter readings found for the provided Account ID")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterReadingsResponseDTO {

    private Long accountId;
    private List<SavedMeterReading> gasReadings;
    private List<SavedMeterReading> elecReadings;

}
