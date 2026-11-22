package io.github.rendyyy.qa.selenium.pages.platform;

import io.github.rendyyy.qa.selenium.config.FrameworkConfig;
import io.github.rendyyy.qa.selenium.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PlatformHomePage extends BasePage {

    // Matched by link text so the locator survives routing changes (hash vs path URLs).
    private static final By ADMIN_LINK = By.partialLinkText("Admin");

    public PlatformHomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Open the platform home page")
    public PlatformHomePage load() {
        open(FrameworkConfig.platformUiBaseUrl());
        wait.visible(ADMIN_LINK);
        return this;
    }

    @Step("Open the admin login page")
    public AdminLoginPage openAdminLogin() {
        click(ADMIN_LINK);
        return new AdminLoginPage(driver).waitUntilLoaded();
    }
}