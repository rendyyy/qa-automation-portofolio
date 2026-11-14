package io.github.rendyyy.qa.selenium.tests.dataproviders;

import io.github.rendyyy.qa.common.data.JsonDataReader;
import io.github.rendyyy.qa.selenium.data.CheckoutCase;
import io.github.rendyyy.qa.selenium.data.LoginCase;
import io.github.rendyyy.qa.selenium.pages.SortOption;
import java.util.Arrays;
import org.testng.annotations.DataProvider;

import java.util.List;

public final class TestDataProviders {

    private static final String LOGIN_DATA = "testdata/login-data.json";
    private static final String CHECKOUT_DATA = "testdata/checkout-data.json";

    private TestDataProviders() {
    }

    @DataProvider(name = "invalidLogins")
    public static Object[][] invalidLogins() {
        return toRows(JsonDataReader.readList(LOGIN_DATA, LoginCase.class));
    }

    @DataProvider(name = "invalidCheckoutInfo")
    public static Object[][] invalidCheckoutInfo() {
        return toRows(JsonDataReader.readList(CHECKOUT_DATA, CheckoutCase.class));
    }

    private static <T> Object[][] toRows(List<T> cases) {
        return cases.stream().map(item -> new Object[]{item}).toArray(Object[][]::new);
    }
        @DataProvider(name = "sortOptions")
    public static Object[][] sortOptions() {
        return Arrays.stream(SortOption.values())
                .map(option -> new Object[]{option})
                .toArray(Object[][]::new);
    }
}