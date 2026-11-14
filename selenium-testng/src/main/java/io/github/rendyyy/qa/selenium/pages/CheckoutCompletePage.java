package io.github.rendyyy.qa.selenium.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private static final By CONFIRMATION_HEADER = By.cssSelector("[data-test='complete-header']");
    private static final By BACK_HOME_BUTTON = By.cssSelector("[data-test='back-to-products']");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutCompletePage waitUntilLoaded() {
        wait.visible(CONFIRMATION_HEADER);
        return this;
    }

    public String getConfirmationHeader() {
        return getText(CONFIRMATION_HEADER);
    }

    @Step("Go back to the products page")
    public InventoryPage backToProducts() {
        click(BACK_HOME_BUTTON);
        return new InventoryPage(driver).waitUntilLoaded();
    }
}