package com.mcn.sp.tech.task.it.cucumber.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.mcn.sp.tech.task.ScottishPowerTechTaskApplication;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@CucumberContextConfiguration
@SpringBootTest(classes = ScottishPowerTechTaskApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {

    @Before
    void setUp() {
        WireMockConfiguration.wireMockConfig().usingFilesUnderClasspath("");
    }

}
