package io.github.rendyyy.qa.selenium.listeners;

import io.github.rendyyy.qa.selenium.driver.DriverManager;
import io.github.rendyyy.qa.selenium.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/** Logs test lifecycle events and attaches a screenshot and the page URL when a test fails. */
public class TestListener implements ITestListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("START  {}", TestIdentity.of(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("PASS   {}", TestIdentity.of(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("FAIL   {} - {}", TestIdentity.of(result), result.getThrowable().toString());
        attachEvidence();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("SKIP   {}", TestIdentity.of(result));
        // A failed attempt that will be retried is reported as skipped; keep its evidence.
        if (result.getThrowable() != null) {
            attachEvidence();
        }
    }

    private void attachEvidence() {
        if (!DriverManager.hasDriver()) {
            return;
        }
        WebDriver driver = DriverManager.getDriver();
        ScreenshotUtils.capture(driver).ifPresent(bytes ->
                Allure.addAttachment("Screenshot on failure", "image/png", new ByteArrayInputStream(bytes), "png"));
        try {
            Allure.addAttachment("Page URL", "text/plain", driver.getCurrentUrl(), ".txt");
        } catch (WebDriverException e) {
            LOG.warn("Could not read the current URL: {}", e.getMessage());
        }
    }
}