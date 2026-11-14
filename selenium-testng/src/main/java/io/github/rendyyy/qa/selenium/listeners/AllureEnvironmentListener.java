package io.github.rendyyy.qa.selenium.listeners;

import io.github.rendyyy.qa.selenium.config.FrameworkConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Writes environment.properties so the Allure report shows what the run was executed against. */
public class AllureEnvironmentListener implements ISuiteListener {

    private static final Logger LOG = LogManager.getLogger(AllureEnvironmentListener.class);

    // Must match allure.results.directory in allure.properties.
    private static final Path RESULTS_DIR = Path.of("target", "allure-results");

    @Override
    public void onStart(ISuite suite) {
        Properties environment = new Properties();
        environment.setProperty("Browser", FrameworkConfig.browser().name());
        environment.setProperty("Headless", String.valueOf(FrameworkConfig.isHeadless()));
        environment.setProperty("BaseUrl", FrameworkConfig.baseUrl());
        environment.setProperty("RetryMaxCount", String.valueOf(FrameworkConfig.retryMaxCount()));
        environment.setProperty("Java", System.getProperty("java.version"));
        environment.setProperty("OS", System.getProperty("os.name"));

        try {
            Files.createDirectories(RESULTS_DIR);
            try (OutputStream out = Files.newOutputStream(RESULTS_DIR.resolve("environment.properties"))) {
                environment.store(out, "Allure environment");
            }
        } catch (IOException e) {
            LOG.warn("Could not write Allure environment file: {}", e.getMessage());
        }
    }
}