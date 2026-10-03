package online.automationintesting.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import online.automationintesting.config.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;

public class Specs {

    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification responseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification requestSpecWithToken(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .addCookie("token", token)
                .log(LogDetail.ALL)
                .build();
    }
}