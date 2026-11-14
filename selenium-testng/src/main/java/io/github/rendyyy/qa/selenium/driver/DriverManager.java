package io.github.rendyyy.qa.selenium.driver;

import io.github.rendyyy.qa.selenium.config.FrameworkConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

/** Owns one WebDriver per thread so tests can run in parallel safely. */
public final class DriverManager {

    private static final Logger LOG = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver() {
        if (DRIVER.get() != null) {
            throw new IllegalStateException("Driver already initialised for thread " + Thread.currentThread().getName());
        }
        WebDriver driver = WebDriverFactory.create(
                FrameworkConfig.browser(),
                FrameworkConfig.isHeadless(),
                FrameworkConfig.windowWidth(),
                FrameworkConfig.windowHeight());

        // Implicit wait stays at zero on purpose; synchronization is done with explicit waits only.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(FrameworkConfig.pageLoadTimeout());

        DRIVER.set(driver);
        LOG.info("Started {} (headless={}) on thread {}",
                FrameworkConfig.browser(), FrameworkConfig.isHeadless(), Thread.currentThread().getName());
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialised for thread " + Thread.currentThread().getName());
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }
        try {
            driver.quit();
        } finally {
            DRIVER.remove();
            LOG.info("Driver closed on thread {}", Thread.currentThread().getName());
        }
    }
    
    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }
}