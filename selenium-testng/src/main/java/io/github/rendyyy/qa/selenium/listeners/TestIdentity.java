package io.github.rendyyy.qa.selenium.listeners;

import org.testng.ITestResult;

import java.util.Arrays;

/** Builds a readable, unique identifier for one test invocation, including data-driven parameters. */
final class TestIdentity {

    private TestIdentity() {
    }

    static String of(ITestResult result) {
        String base = result.getMethod().getQualifiedName();
        Object[] parameters = result.getParameters();
        return parameters.length == 0 ? base : base + Arrays.deepToString(parameters);
    }
}