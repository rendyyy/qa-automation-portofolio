package io.github.rendyyy.qa.selenium.tests.api;

import io.github.rendyyy.qa.common.api.AuthClient;
import io.github.rendyyy.qa.common.api.ApiConfig;
import io.github.rendyyy.qa.common.api.model.TokenResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.REGRESSION;
import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.SMOKE;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class AuthApiTests extends BaseApiTest {

    @Test(groups = {SMOKE, REGRESSION}, description = "Valid credentials return a token")
    public void shouldIssueTokenForValidCredentials() {
        Response response = new AuthClient().createToken(ApiConfig.username(), ApiConfig.password());

        response.then().body(matchesJsonSchemaInClasspath("schemas/auth-token-schema.json"));
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(200);
            softly.assertThat(response.as(TokenResponse.class).token()).isNotBlank();
        });
    }

    @Test(groups = REGRESSION, description = "Wrong credentials return a reason and no token")
    public void shouldNotIssueTokenForInvalidCredentials() {
        Response response = new AuthClient().createToken(ApiConfig.username(), "wrong-password");

        // This API reports bad credentials with HTTP 200 and a "reason" field instead of 401.
        TokenResponse body = response.as(TokenResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(200);
            softly.assertThat(body.token()).isNull();
            softly.assertThat(body.reason()).isEqualTo("Bad credentials");
        });
    }
}