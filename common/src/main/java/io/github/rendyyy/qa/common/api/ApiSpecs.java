package io.github.rendyyy.qa.common.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/** Builds the shared request specification. A fresh instance is created per client. */
public final class ApiSpecs {

    private ApiSpecs() {
    }

        public static RequestSpecification base() {
        return base(ApiConfig.baseUrl());
    }

    public static RequestSpecification base(String baseUri) {
        RestAssuredConfig config = RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", (int) ApiConfig.connectTimeout().toMillis())
                        .setParam("http.socket.timeout", (int) ApiConfig.readTimeout().toMillis()));

        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                // Send a plain value: some APIs reject the multi-value header RestAssured generates for ContentType.JSON
                .setAccept("application/json")
                .setConfig(config)
                .addFilter(new AllureRestAssured())
                .build();
    }
}