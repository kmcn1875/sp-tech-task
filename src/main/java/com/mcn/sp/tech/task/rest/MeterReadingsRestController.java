package com.mcn.sp.tech.task.rest;

import com.mcn.sp.tech.task.dto.MeterReadingsResponseDTO;
import com.mcn.sp.tech.task.dto.MeterReadingsRequestDTO;
import com.mcn.sp.tech.task.exception.NoDataFoundException;
import com.mcn.sp.tech.task.service.MeterReadingsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping(path = "/api/smart")
@RequiredArgsConstructor
@Validated
@Slf4j
public class MeterReadingsRestController {

    private final MeterReadingsService meterReadingsService;

    @Operation(summary = "Return all meter readings for the provided Account ID",
            description = "Where no meter readings are found for the provided Account ID, a response status of 'NO_CONTENT' will be returned")
    @GetMapping(path = "/reads/{accountId}", produces = "application/json")
    public ResponseEntity<MeterReadingsResponseDTO> getMeterReadings(@PathVariable(value = "accountId") @Positive Long accountId) {
        log.info("Request made for Meter Readings for Account ID {}", accountId);
        try {
            return ResponseEntity.ok(meterReadingsService.getMeterReadings(accountId));
        } catch (NoDataFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Submit meter readings for the provided Account ID",
            description = "A Meter reading can be submitted for Electricity, Gas or both")
    @PostMapping(path = "/reads/{accountId}", consumes = "application/json")
    public ResponseEntity<String> submitMeterReadings(@PathVariable(value = "accountId") @Positive Long accountId,
                                                 @RequestBody @NotNull @Valid MeterReadingsRequestDTO newMeterReadingsDTO) {
        log.info("Meter Readings submitted for Account ID {}", accountId);
        meterReadingsService.submitMeterReadings(accountId, newMeterReadingsDTO);
        return ResponseEntity.accepted().body("Readings Accepted");
    }
}
