package base;

import endpoints.Routes;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import models.Auth;
import org.testng.annotations.BeforeSuite;
import utilities.ConfigReader;

import static io.restassured.RestAssured.given;

public class BaseAPITest {
    public static RequestSpecification requestSpecification;
    public static ResponseSpecification responseSpecification;
    public static String token;
    protected static String BASE_URL;

    @BeforeSuite
    public void setup() {
        BASE_URL = ConfigReader.get("base.url");

        RestAssured.baseURI = BASE_URL;

        token = given()
                .contentType(ContentType.JSON)
                .body(new Auth(ConfigReader.get("username"),ConfigReader.get("password")))
                .when().post(Routes.auth())
                .then().extract().path("token");

        requestSpecification = new RequestSpecBuilder()
                .addHeader("Cookie","token="+token)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();


    }
}
