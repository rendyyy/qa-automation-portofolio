package io.github.rendyyy.qa.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

/**
 * Resolves configuration values with this precedence:
 * system property (-Dkey=value) > environment variable > config-{env}.properties > config.properties.
 */
public final class ConfigReader {

    private static final String BASE_FILE = "config/config.properties";
    private static final String ENV_FILE_PATTERN = "config/config-%s.properties";
    private static final String DEFAULT_ENV = "local";

    private static final Properties FILE_PROPERTIES = loadProperties();

    private ConfigReader() {
    }

    public static String get(String key) {
        return resolve(key).orElseThrow(
                () -> new IllegalStateException("Missing required config key: " + key));
    }

    public static String get(String key, String defaultValue) {
        return resolve(key).orElse(defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return resolve(key).map(Boolean::parseBoolean).orElse(defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return resolve(key).map(Integer::parseInt).orElse(defaultValue);
    }

    private static Optional<String> resolve(String key) {
        return firstNonBlank(System.getProperty(key))
                .or(() -> firstNonBlank(System.getenv(toEnvName(key))))
                .or(() -> firstNonBlank(FILE_PROPERTIES.getProperty(key)));
    }

    private static Optional<String> firstNonBlank(String value) {
        return Optional.ofNullable(value).map(String::trim).filter(v -> !v.isEmpty());
    }

    /** Converts "ui.base.url" to "UI_BASE_URL". */
    private static String toEnvName(String key) {
        return key.replace('.', '_').replace('-', '_').toUpperCase(Locale.ROOT);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        loadInto(properties, BASE_FILE, true);

        String env = firstNonBlank(System.getProperty("env"))
                .or(() -> firstNonBlank(System.getenv("ENV")))
                .orElse(DEFAULT_ENV);
        loadInto(properties, String.format(ENV_FILE_PATTERN, env), false);
        return properties;
    }

    private static void loadInto(Properties target, String resourcePath, boolean required) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream stream = classLoader.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                if (required) {
                    throw new IllegalStateException("Config file not found on classpath: " + resourcePath);
                }
                return;
            }
            target.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read config file: " + resourcePath, e);
        }
    }
}