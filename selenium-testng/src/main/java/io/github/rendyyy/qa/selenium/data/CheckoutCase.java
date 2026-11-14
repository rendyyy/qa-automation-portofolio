package io.github.rendyyy.qa.selenium.data;

public record CheckoutCase(String description, String firstName, String lastName,
                           String postalCode, String expectedError) {

    public CheckoutInfo info() {
        return new CheckoutInfo(firstName, lastName, postalCode);
    }

    @Override
    public String toString() {
        return description;
    }
}