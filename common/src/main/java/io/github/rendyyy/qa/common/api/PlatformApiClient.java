package io.github.rendyyy.qa.common.api;

import io.github.rendyyy.qa.common.api.model.AuthRequest;
import io.github.rendyyy.qa.common.api.model.ContactMessage;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;

/** Client for the Restful-booker-platform services used by the hybrid tests. */
public class PlatformApiClient {

    private static final String LOGIN_PATH = "/auth/login";
     private static final String MESSAGE_PATH = "/message";
    private static final String MESSAGE_BY_ID_PATH = "/message/{id}";
    private static final String TOKEN_COOKIE = "token";

    private final RequestSpecification spec;

    public PlatformApiClient() {
        this.spec = ApiSpecs.base(ApiConfig.platformBaseUrl());
    }

    @Step("Create a contact message")
    public Response createMessage(ContactMessage message) {
        return given().spec(spec).body(message).post(MESSAGE_PATH);
    }

    @Step("Log in to the platform API as {username}")
    public Response login(String username, @Param(mode = Parameter.Mode.MASKED) String password) {
        return given().spec(spec).body(new AuthRequest(username, password)).post(LOGIN_PATH);
    }

    /** The platform hands the session token back as a cookie. */
     public String fetchAdminToken() {
        Response response = login(ApiConfig.platformAdminUsername(), ApiConfig.platformAdminPassword());

        String token = response.getCookie(TOKEN_COOKIE);
        if (token == null || token.isBlank()) {
            token = tokenFromBody(response);
        }
        if (token == null || token.isBlank()) {
            String body = response.asString();
            throw new IllegalStateException("Platform login returned no token (status " + response.statusCode()
                    + ", body: " + body.substring(0, Math.min(body.length(), 300)) + ")");
        }
        return token;
    }

    private static String tokenFromBody(Response response) {
        try {
            return response.jsonPath().getString("token");
        } catch (RuntimeException e) {
            return null; // body is empty or not JSON
        }
    }
    
    @Step("Get all messages")
    public Response getMessages(String token) {
        return given().spec(spec).cookie(TOKEN_COOKIE, token).get(MESSAGE_PATH);
    }

    @Step("Delete message {id}")
    public Response deleteMessage(int id, String token) {
        return given().spec(spec).cookie(TOKEN_COOKIE, token).pathParam("id", id).delete(MESSAGE_BY_ID_PATH);
    }

    /** Returns the ids of all messages with exactly this subject; empty when none match. */
    public List<Integer> findMessageIdsBySubject(String subject, String token) {
        List<Map<String, Object>> messages = getMessages(token).jsonPath().getList("messages");
        if (messages == null) {
            return List.of();
        }
        return messages.stream()
                .filter(message -> subject.equals(message.get("subject")))
                .map(message -> message.getOrDefault("id", message.get("messageid")))
                .filter(Objects::nonNull)
                .map(id -> ((Number) id).intValue())
                .toList();
    }
}