package com.mcn.sp.tech.task.service;

import com.mcn.sp.tech.task.dto.MeterReadingsResponseDTO;
import com.mcn.sp.tech.task.dto.MeterReadingsRequestDTO;
import com.mcn.sp.tech.task.exception.NoDataFoundException;

public interface MeterReadingsService {

    /**
     * Returns full meter readings (Gas and Electric) for the given Account ID.
     *
     * @param accountId The account id used to fetch the relevant meter readings
     * @return An overview of all meter readings for given account
     */
    MeterReadingsResponseDTO getMeterReadings(Long accountId) throws NoDataFoundException;

    /**
     * Submit meter readings for given account
     *
     * @param accountId The account id against which to submit meter readings
     * @param newMeterReadingsDTO The meter readings to submit
     */
    void submitMeterReadings(Long accountId, MeterReadingsRequestDTO newMeterReadingsDTO);
}
