package com.mcn.sp.tech.task.it.cucumber.steps;

import com.mcn.sp.tech.task.it.utils.SharedResponseData;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

public class RetrieveReadingsStep {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SharedResponseData sharedResponseData;

    @When("Endpoint is called for retrieving all meter readings for Account Number {string}")
    public void endpoint_is_called_for_retrieving_all_meter_readings_for_account_number(String accountNumber) {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/smart/reads/{accountNumber}", String.class, accountNumber);
        sharedResponseData.setResponse(response);
    }

}
