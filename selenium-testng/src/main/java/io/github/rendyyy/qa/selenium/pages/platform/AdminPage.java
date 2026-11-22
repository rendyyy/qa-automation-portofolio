package io.github.rendyyy.qa.selenium.pages.platform;

import io.github.rendyyy.qa.selenium.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage extends BasePage {

    private static final By MESSAGES_LINK = By.partialLinkText("Messages");

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public AdminPage waitUntilLoaded() {
        wait.visible(MESSAGES_LINK);
        return this;
    }

    @Step("Open the admin messages page")
    public AdminMessagesPage openMessages() {
        click(MESSAGES_LINK);
        return new AdminMessagesPage(driver).waitUntilLoaded();
    }
}