package online.automationintesting.api;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import online.automationintesting.config.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {

    protected static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = config.apiBaseUrl();
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }
}