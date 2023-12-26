package com.mcn.sp.tech.task;

import com.mcn.sp.tech.task.repo.MeterReadingsRepo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
//@ActiveProfiles("test")
class ScottishPowerTechTaskApplicationTest {


    @Test
    void contextLoads() {
//        assertThat(meterReadingsRepo.count()).isEqualTo(0L);
    }

}
