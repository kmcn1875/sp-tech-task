package com.mcn.sp.tech.task.it.cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcn.sp.tech.task.entity.MeterReadingEntity;
import com.mcn.sp.tech.task.entity.ReadingType;
import com.mcn.sp.tech.task.it.utils.DatabaseUtils;
import com.mcn.sp.tech.task.it.utils.SharedResponseData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.MapUtils.getLong;
import static org.apache.commons.collections4.MapUtils.getString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class CommonSteps {

    @Autowired
    private DatabaseUtils databaseUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SharedResponseData sharedResponseData;

    @Given("Database contains the following data:")
    public void database_contains_the_following_data(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        rows.forEach(map -> {
            databaseUtils.insertData(MeterReadingEntity.builder()
                    .accountId(getLong(map, "account_id"))
                    .readingType(ReadingType.valueOf(getString(map, "reading_type")))
                    .reading(getLong(map, "reading"))
                    .meterId(getLong(map, "meter_id"))
                    .date(LocalDate.parse(getString(map, "date")))
                    .build());
        });
    }


    @Then("Response Status should be {string}")
    public void response_status_should_be(String expectedStatus) {
        ResponseEntity<?> response = sharedResponseData.getResponse();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.valueOf(expectedStatus)));
    }

    @Then("Response Body should be")
    public void response_body_should_be(String expectedJsonResponse) throws JSONException, JsonProcessingException {
        ResponseEntity<String> response = sharedResponseData.getResponse();
        JSONAssert.assertEquals(expectedJsonResponse, response.getBody(), true);
    }

}
