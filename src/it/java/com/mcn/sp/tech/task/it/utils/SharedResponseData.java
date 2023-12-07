package com.mcn.sp.tech.task.it.utils;

import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
@ScenarioScope
@Getter
@Setter
public class SharedResponseData {

    private ResponseEntity<String> response;

}
