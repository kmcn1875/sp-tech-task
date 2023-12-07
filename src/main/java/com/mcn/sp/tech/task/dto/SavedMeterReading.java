package com.mcn.sp.tech.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Details of an individual saved meter reading")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SavedMeterReading {

    private Long id;
    private Long meterId;
    private Long reading;
    private LocalDate date;
}
