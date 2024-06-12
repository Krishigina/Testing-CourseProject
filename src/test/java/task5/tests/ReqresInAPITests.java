package task5.tests;

import base.BaseSeleniumTest;
import base.TestListener;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task5.models.*;

import io.restassured.module.jsv.JsonSchemaValidator;

import java.util.List;

import static io.restassured.RestAssured.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;


@ExtendWith(TestListener.class)
@Feature("ReqresIn APITests")
public class ReqresInAPITests extends BaseSeleniumTest {

    private static final Logger logger = LoggerFactory.getLogger(ReqresInAPITests.class);

    @Test
    @DisplayName("[GET] Get Users from page 2")
    public void getListUsers() {
        List<UserData> users = given().
                when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UsersListSchema.json"))
                .body("page", equalTo(2))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .extract().jsonPath().getList("data", UserData.class);
        assertThat(users).extracting(UserData::getId).isNotNull();
        assertThat(users).extracting(UserData::getFirst_name).contains("Tobias");
        assertThat(users).extracting(UserData::getLast_name).contains("Funke");
        logger.info("Test done");
    }

    @Test
    @DisplayName("[GET] Get Single User with id = 2")
    public void getSingleUser() {
        UserData user = given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UserSingleSchema.json"))
                .extract().jsonPath().getObject("data", UserData.class);
        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getEmail()).isEqualTo("janet.weaver@reqres.in");
        assertThat(user.getFirst_name()).isEqualTo("Janet");
        assertThat(user.getLast_name()).isEqualTo("Weaver");
        assertThat(user.getAvatar()).isEqualTo("https://reqres.in/img/faces/2-image.jpg");
        logger.info("Test done");
    }


    @Test
    @DisplayName("[GET] Get user with bad id")
    public void getUserNotFound() {
        given().
                when()
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
        logger.info("Test done");
    }


    @Test
    @DisplayName("[GET] Get Resources List")
    public void getListResourses() {
        List<ResourseData> resourses = given().
                when()
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/ResourcesListSchema.json"))
                .body("page", equalTo(1))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .extract().jsonPath().getList("data", ResourseData.class);

        assertThat(resourses).extracting(ResourseData::getId).isNotNull();
        assertThat(resourses).extracting(ResourseData::getName).contains("cerulean");
        assertThat(resourses).extracting(ResourseData::getYear).contains(2000);
        assertThat(resourses).extracting(ResourseData::getColor).contains("#98B2D1");
        assertThat(resourses).extracting(ResourseData::getPantone_value).contains("15-4020");
        logger.info("Test done");
    }

    @Test
    @DisplayName("[GET] Get Single Resource with id = 2")
    public void getSingleResourse() {
        ResourseData resourse = given().
                when()
                .get("https://reqres.in/api/unknown/2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/ResourceSingleSchema.json"))
                .extract().jsonPath().getObject("data", ResourseData.class);

        assertThat(resourse).isNotNull();
        assertThat(resourse.getId()).isEqualTo(2);
        assertThat(resourse.getName()).isEqualTo("fuchsia rose");
        assertThat(resourse.getYear()).isEqualTo(2001);
        assertThat(resourse.getColor()).isEqualTo("#C74375");
        assertThat(resourse.getPantone_value()).isEqualTo("17-2031");
        logger.info("Test done");
    }

    @Test
    @DisplayName("[GET] Get Single Resource with bad id")
    public void getResourseNotFound() {
        given().
                when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
    }
    @Test
    @DisplayName("[POST] Create User with name: morpheus | job: leader")
    public void createUser() {
        UserRequest userRequest =
                UserRequest.builder()
                        .name("morpheus")
                        .job("leader")
                        .build();
        UserResponse userResponse = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UserCreateResponseSchema.json"))
                .extract().as(UserResponse.class);
        assertThat(userResponse.getName()).isEqualTo(userRequest.getName());
        assertThat(userResponse.getJob()).isEqualTo(userRequest.getJob());

    }

    @Test
    @DisplayName("[PUT] User Update")
    public void updateUserPut() {
        UserRequest userRequest =
                UserRequest.builder()
                        .name("morpheus")
                        .job("zion resident")
                        .build();

        UserResponse userResponse = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UserUpdateResponseSchema.json"))
                .extract().as(UserResponse.class);

        assertThat(userResponse.getName()).isEqualTo(userRequest.getName());
        assertThat(userResponse.getJob()).isEqualTo(userRequest.getJob());
    }

    @Test
    @DisplayName("[PATCH] User Update")
    public void updateUserPatch() {
        UserRequest userRequest =
                UserRequest.builder()
                        .name("morpheus")
                        .job("zion resident")
                        .build();

        UserResponse userResponse = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UserUpdateResponseSchema.json"))
                .extract().as(UserResponse.class);

        assertThat(userResponse.getName()).isEqualTo(userRequest.getName());
        assertThat(userResponse.getJob()).isEqualTo(userRequest.getJob());
    }

    @Test
    @DisplayName("[DELETE] User Update")
    public void deleteUser() {
        given().
                when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204)
                .body(equalTo(""));
    }

    @Test
    @DisplayName("[POST] Successful register")
    public void registerSuccessful() {
        RegisterAndLoginRequest request =
                RegisterAndLoginRequest.builder()
                        .email("eve.holt@reqres.in")
                        .password("pistol")
                        .build();

        RegisterAndLoginResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/RegisterSuccessfulResponseSchema.json"))
                .extract().as(RegisterAndLoginResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(4);
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    @DisplayName("[POST] Unsuccessful register")
    public void registerUnsuccessful() {
        RegisterAndLoginRequest request =
                RegisterAndLoginRequest.builder()
                        .email("sydney@fife")
                        .build();

        RegisterAndLoginResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/RegisterUnsuccessfulResponseSchema.json"))
                .extract().as(RegisterAndLoginResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getError()).isEqualTo("Missing password");
    }

    @Test
    @DisplayName("[POST] Successful login")
    public void loginSuccessful() {
        RegisterAndLoginRequest request =
                RegisterAndLoginRequest.builder()
                        .email("eve.holt@reqres.in")
                        .password("cityslicka")
                        .build();

        RegisterAndLoginResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/LoginSuccessfulResponseSchema.json"))
                .extract().as(RegisterAndLoginResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    @DisplayName("[POST] Unsuccessful login")
    public void loginUnsuccessful() {
        RegisterAndLoginRequest request =
                RegisterAndLoginRequest.builder()
                        .email("peter@klaven")
                        .build();

        RegisterAndLoginResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/LoginUnsuccessfulResponseSchema.json"))
                .extract().as(RegisterAndLoginResponse .class);


        assertThat(response).isNotNull();
        assertThat(response.getError()).isEqualTo("Missing password");
    }

    @Test
    @DisplayName("[GET] Get users list with 3 sec delay")
    public void getUsersWithDelay() {
        List<UserData> users = given()
                .when()
                .get("https://reqres.in/api/users?delay=3")
                .then()
                .statusCode(200)
                .time(greaterThan(3000L))
                .and().time(lessThan(5000L))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UsersListSchema.json"))
                .body("page", equalTo(1))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .extract().jsonPath().getList("data", UserData.class);

        assertThat(users).extracting(UserData::getId).isNotNull();
        assertThat(users).extracting(UserData::getFirst_name).contains("George");
        assertThat(users).extracting(UserData::getLast_name).contains("Bluth");
    }

}
