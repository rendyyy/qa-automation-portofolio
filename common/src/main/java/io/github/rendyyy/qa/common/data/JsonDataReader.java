package io.github.rendyyy.qa.common.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

/** Reads JSON test data from the classpath into typed objects. */
public final class JsonDataReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonDataReader() {
    }

    public static <T> T read(String resourcePath, Class<T> type) {
        try (InputStream stream = open(resourcePath)) {
            return MAPPER.readValue(stream, type);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to parse test data: " + resourcePath, e);
        }
    }

    public static <T> List<T> readList(String resourcePath, Class<T> elementType) {
        try (InputStream stream = open(resourcePath)) {
            return MAPPER.readValue(stream,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, elementType));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to parse test data: " + resourcePath, e);
        }
    }

    private static InputStream open(String resourcePath) {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalStateException("Test data file not found on classpath: " + resourcePath);
        }
        return stream;
    }
}