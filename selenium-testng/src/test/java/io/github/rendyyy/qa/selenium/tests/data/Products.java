package io.github.rendyyy.qa.selenium.tests.data;

import java.util.List;

/** Product names exposed by the SauceDemo catalogue. */
public final class Products {

    public static final String BACKPACK = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    public static final String BOLT_TSHIRT = "Sauce Labs Bolt T-Shirt";
    public static final String FLEECE_JACKET = "Sauce Labs Fleece Jacket";
    public static final String ONESIE = "Sauce Labs Onesie";
    public static final String RED_TSHIRT = "Test.allTheThings() T-Shirt (Red)";

    public static final List<String> ALL = List.of(
            BACKPACK, BIKE_LIGHT, BOLT_TSHIRT, FLEECE_JACKET, ONESIE, RED_TSHIRT);

    private Products() {
    }
}