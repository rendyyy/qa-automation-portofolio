package io.github.rendyyy.qa.selenium.driver;

import java.util.Arrays;
import java.util.Locale;

public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    public static BrowserType from(String value) {
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(type -> type.name().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported browser: '" + value + "'. Supported: " + Arrays.toString(values())));
    }
}