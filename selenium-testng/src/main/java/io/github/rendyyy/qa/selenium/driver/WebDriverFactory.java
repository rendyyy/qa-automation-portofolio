package io.github.rendyyy.qa.selenium.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Map;

/** Builds configured WebDriver instances. Holds no state; lifecycle belongs to DriverManager. */
public final class WebDriverFactory {

    // Binary resolution touches the shared driver cache, so serialize it for parallel runs.
    private static final Object SETUP_LOCK = new Object();

    private WebDriverFactory() {
    }

    public static WebDriver create(BrowserType browser, boolean headless, int width, int height) {
        return switch (browser) {
            case CHROME -> createChrome(headless, width, height);
            case FIREFOX -> createFirefox(headless, width, height);
            case EDGE -> createEdge(headless, width, height);
        };
    }

    private static WebDriver createChrome(boolean headless, int width, int height) {
        synchronized (SETUP_LOCK) {
            WebDriverManager.chromedriver().setup();
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=" + width + "," + height);
        // Disable the password manager and leak-detection popups that cover SauceDemo elements.
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        if (headless) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless, int width, int height) {
        synchronized (SETUP_LOCK) {
            WebDriverManager.firefoxdriver().setup();
        }
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=" + width, "--height=" + height);
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdge(boolean headless, int width, int height) {
        synchronized (SETUP_LOCK) {
            WebDriverManager.edgedriver().setup();
        }
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--window-size=" + width + "," + height);
        if (headless) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return new EdgeDriver(options);
    }
}