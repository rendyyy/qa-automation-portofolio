package io.github.rendyyy.qa.common.api;

import io.github.rendyyy.qa.common.config.ConfigReader;

import java.time.Duration;

/** Typed access to API-related configuration. */
public final class ApiConfig {

    private ApiConfig() {
    }

    public static String baseUrl() {
        return ConfigReader.get("api.base.url");
    }

    public static String username() {
        return ConfigReader.get("api.username");
    }

    public static String password() {
        return ConfigReader.get("api.password");
    }

    public static Duration connectTimeout() {
        return Duration.ofSeconds(ConfigReader.getInt("api.connect.timeout.seconds", 10));
    }

    public static Duration readTimeout() {
        return Duration.ofSeconds(ConfigReader.getInt("api.read.timeout.seconds", 20));
    }
        public static String platformBaseUrl() {
        return ConfigReader.get("platform.api.base.url");
    }

    public static String platformAdminUsername() {
        return ConfigReader.get("platform.admin.username");
    }

    public static String platformAdminPassword() {
        return ConfigReader.get("platform.admin.password");
    }
}