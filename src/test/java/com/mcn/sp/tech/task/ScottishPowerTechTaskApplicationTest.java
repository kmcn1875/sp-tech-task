package com.mcn.sp.tech.task;

import com.mcn.sp.tech.task.entity.WalletPassEntity;
import com.mcn.sp.tech.task.repo.WalletPassRepo;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles({"test"})
class ScottishPowerTechTaskApplicationTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    public static final String ACCOUNT_NUMBER = "654054432";
    public static final String PASS_TYPE_ID = "somepasstype";

    @Autowired
    private WalletPassRepo walletPassRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void insertData() throws IOException {
        ClassPathResource resource = new ClassPathResource("pass.json");
        String json = IOUtils.readLines(resource.getInputStream(), Charset.defaultCharset())
                .stream().map(String::trim).collect(Collectors.joining(" "));

        String sql = "INSERT INTO wallet_pass (account_number, pass_type_id, date, pass) VALUES (?, ?, ?, ?::json)";

        jdbcTemplate.update(sql, ACCOUNT_NUMBER, PASS_TYPE_ID, LocalDate.now(), json);
    }

    @Test
    void testWrite() {
        Optional<WalletPassEntity> found = walletPassRepo.findById(1L);

        assertTrue(found.isPresent());

        assertThat(found.get().getPass().getBackgroundColor()).isEqualTo("#FAFAFA");
    }

}