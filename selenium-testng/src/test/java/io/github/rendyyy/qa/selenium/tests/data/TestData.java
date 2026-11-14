package io.github.rendyyy.qa.selenium.tests.data;

import io.github.rendyyy.qa.common.data.JsonDataReader;
import io.github.rendyyy.qa.selenium.data.CheckoutInfo;

public final class TestData {

    private static final String VALID_CHECKOUT_DATA = "testdata/checkout-valid.json";

    private TestData() {
    }

    public static CheckoutInfo validCheckoutInfo() {
        return JsonDataReader.read(VALID_CHECKOUT_DATA, CheckoutInfo.class);
    }
}