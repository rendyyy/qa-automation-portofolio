package io.github.rendyyy.qa.selenium.pages.platform;

import io.github.rendyyy.qa.selenium.pages.BasePage;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminLoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("username");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("doLogin");

    public AdminLoginPage(WebDriver driver) {
        super(driver);
    }

    public AdminLoginPage waitUntilLoaded() {
        wait.visible(LOGIN_BUTTON);
        return this;
    }

    @Step("Log in to the admin panel as {username}")
    public AdminPage loginAs(String username, @Param(mode = Parameter.Mode.MASKED) String password) {
        type(USERNAME_INPUT, username);
        typeMasked(PASSWORD_INPUT, password);
        click(LOGIN_BUTTON);
        return new AdminPage(driver).waitUntilLoaded();
    }
}