package io.github.rendyyy.qa.selenium.config;

import io.github.rendyyy.qa.common.config.ConfigReader;
import io.github.rendyyy.qa.selenium.driver.BrowserType;

import java.time.Duration;

/** Typed access to UI-related configuration. */
public final class FrameworkConfig {

    private FrameworkConfig() {
    }

    public static BrowserType browser() {
        return BrowserType.from(ConfigReader.get("browser", "chrome"));
    }

    public static boolean isHeadless() {
        return ConfigReader.getBoolean("headless", false);
    }

    public static String baseUrl() {
        return ConfigReader.get("ui.base.url");
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(ConfigReader.getInt("explicit.wait.seconds", 10));
    }

    public static Duration pageLoadTimeout() {
        return Duration.ofSeconds(ConfigReader.getInt("page.load.timeout.seconds", 30));
    }

    public static int windowWidth() {
        return ConfigReader.getInt("window.width", 1920);
    }

    public static int windowHeight() {
        return ConfigReader.getInt("window.height", 1080);
    }

    public static String username() {
        return ConfigReader.get("ui.username");
    }

    public static String password() {
        return ConfigReader.get("ui.password");
    }

    public static int retryMaxCount() {
        return ConfigReader.getInt("retry.max.count", 1);
    }
}