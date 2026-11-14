package io.github.rendyyy.qa.common.api;

import io.github.rendyyy.qa.common.api.model.AuthRequest;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AuthClient {

    private static final String AUTH_PATH = "/auth";

    private final RequestSpecification spec;

    public AuthClient() {
        this.spec = ApiSpecs.base();
    }

    @Step("Request an auth token for {username}")
    public Response createToken(String username, @Param(mode = Parameter.Mode.MASKED) String password) {
        return given().spec(spec)
                .body(new AuthRequest(username, password))
                .post(AUTH_PATH);
    }

    /** Fetches a token for the configured admin user; fails fast if the API does not issue one. */
    public String fetchAdminToken() {
        Response response = createToken(ApiConfig.username(), ApiConfig.password());
        String token = response.then().statusCode(200).extract().path("token");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Auth API did not return a token: " + response.asString());
        }
        return token;
    }
}