package io.github.rendyyy.qa.selenium.listeners;

import io.github.rendyyy.qa.selenium.config.FrameworkConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Re-runs a failed test up to retry.max.count times. Set the property to 0 to disable retries. */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetryAnalyzer.class);

    // Keyed per test and parameter set because TestNG may share one analyzer across data-provider rows.
    private static final Map<String, AtomicInteger> ATTEMPTS = new ConcurrentHashMap<>();

    @Override
    public boolean retry(ITestResult result) {
        int attempt = ATTEMPTS
                .computeIfAbsent(TestIdentity.of(result), key -> new AtomicInteger())
                .incrementAndGet();
        int maxRetries = FrameworkConfig.retryMaxCount();

        if (attempt <= maxRetries) {
            LOG.warn("RETRY  {} (retry {} of {})", TestIdentity.of(result), attempt, maxRetries);
            return true;
        }
        return false;
    }
}