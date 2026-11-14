package io.github.rendyyy.qa.selenium.pages;

import io.github.rendyyy.qa.selenium.config.FrameworkConfig;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.cssSelector("[data-test='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-test='password']");
    private static final By LOGIN_BUTTON = By.cssSelector("[data-test='login-button']");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the login page")
    public LoginPage load() {
        open(FrameworkConfig.baseUrl());
        return waitUntilLoaded();
    }

    public LoginPage waitUntilLoaded() {
        wait.visible(LOGIN_BUTTON);
        return this;
    }

    @Step("Log in as {username}")
    public InventoryPage loginAs(String username, @Param(mode = Parameter.Mode.MASKED) String password) {
        submitCredentials(username, password);
        return new InventoryPage(driver).waitUntilLoaded();
    }

    @Step("Log in as {username} and expect an error")
    public LoginPage loginExpectingError(String username, @Param(mode = Parameter.Mode.MASKED) String password) {
        submitCredentials(username, password);
        wait.visible(ERROR_MESSAGE);
        return this;
    }

    @Step("Open the inventory URL without a session")
    public LoginPage openInventoryWithoutSession() {
        open(FrameworkConfig.baseUrl() + "/inventory.html");
        wait.visible(ERROR_MESSAGE);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isLoginFormDisplayed() {
        return isDisplayed(LOGIN_BUTTON);
    }

    private void submitCredentials(String username, String password) {
        type(USERNAME_INPUT, username);
        typeMasked(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
    }
}