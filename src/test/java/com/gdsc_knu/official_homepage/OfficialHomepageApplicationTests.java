package com.gdsc_knu.official_homepage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.config.location = src/main/resources/application.yml, src/main/resources/application-test.yml"
})
class OfficialHomepageApplicationTests {

    @Test
    void contextLoads() {
    }

}
