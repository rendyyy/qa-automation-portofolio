package io.github.rendyyy.qa.selenium.data;

public record LoginCase(String description, String username, String password, String expectedError) {

    /** Used as the iteration name in TestNG and Allure; keeps credentials out of reports. */
    @Override
    public String toString() {
        return description;
    }
}