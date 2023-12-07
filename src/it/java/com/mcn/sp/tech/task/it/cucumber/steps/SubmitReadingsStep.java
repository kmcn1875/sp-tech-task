package com.mcn.sp.tech.task.it.cucumber.steps;

import com.mcn.sp.tech.task.dto.SubmittedMeterReading;
import com.mcn.sp.tech.task.dto.MeterReadingsRequestDTO;
import com.mcn.sp.tech.task.entity.MeterReadingEntity;
import com.mcn.sp.tech.task.entity.ReadingType;
import com.mcn.sp.tech.task.it.utils.DatabaseUtils;
import com.mcn.sp.tech.task.it.utils.SharedResponseData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.MapUtils.getLong;
import static org.apache.commons.collections4.MapUtils.getString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

public class SubmitReadingsStep {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SharedResponseData sharedResponseData;

    @Autowired
    private DatabaseUtils databaseUtils;

    private String accountNumber;
    private SubmittedMeterReading gasReading;
    private SubmittedMeterReading electricityReading;

    @Given("Customer with Account Number {string}")
    public void customer_with_account_number(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Given("has gas reading")
    public void has_gas_reading(DataTable dataTable) {
        gasReading = createMeterReading(dataTable.asMap());
    }

    @Given("has electricity reading")
    public void has_electricity_reading(io.cucumber.datatable.DataTable dataTable) {
        electricityReading = createMeterReading(dataTable.asMap());
    }

    @When("Customer submits readings")
    public void customer_submits_readings() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/api/smart/reads/{accountNumber}",
                new HttpEntity<>(createPostBody(), headers), String.class, accountNumber);
        sharedResponseData.setResponse(response);
    }

    private MeterReadingsRequestDTO createPostBody() {
        return MeterReadingsRequestDTO.builder()
                .gasReading(gasReading)
                .elecReading(electricityReading)
                .build();
    }

    @Then("Database should now contain the following data")
    public void database_should_now_contain_the_following_data(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        List<MeterReadingEntity> expected = rows.stream().map(row ->
                MeterReadingEntity.builder()
                        .id(getLong(row, "id"))
                        .accountId(getLong(row, "account_id"))
                        .readingType(ReadingType.valueOf(getString(row, "reading_type")))
                        .reading(getLong(row, "reading"))
                        .meterId(getLong(row, "meter_id"))
                        .date(LocalDate.parse(getString(row, "date")))
                        .build()
        ).collect(Collectors.toList());
        List<MeterReadingEntity> savedMeterReadings = databaseUtils.findAllMeterReadings()
                .stream()
                .peek(reading -> reading.setId(null))
                .collect(Collectors.toList());

        assertThat(savedMeterReadings.size(), equalTo(expected.size()));
        assertThat(savedMeterReadings, containsInAnyOrder(expected.toArray(new MeterReadingEntity[]{})));
    }

    private SubmittedMeterReading createMeterReading(Map<String, String> map) {
        return SubmittedMeterReading.builder()
                .reading(getLong(map, "reading"))
                .meterId(getLong(map, "meterId"))
                .date(LocalDate.parse(getString(map, "date")))
                .build();
    }

}
