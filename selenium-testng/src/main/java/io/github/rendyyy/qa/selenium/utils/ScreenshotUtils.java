package io.github.rendyyy.qa.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.util.Optional;

public final class ScreenshotUtils {

    private static final Logger LOG = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    /** Returns PNG bytes, or empty when the browser is already gone or cannot take screenshots. */
    public static Optional<byte[]> capture(WebDriver driver) {
        if (!(driver instanceof TakesScreenshot screenshotter)) {
            return Optional.empty();
        }
        try {
            return Optional.of(screenshotter.getScreenshotAs(OutputType.BYTES));
        } catch (WebDriverException e) {
            LOG.warn("Could not capture screenshot: {}", e.getMessage());
            return Optional.empty();
        }
    }
}