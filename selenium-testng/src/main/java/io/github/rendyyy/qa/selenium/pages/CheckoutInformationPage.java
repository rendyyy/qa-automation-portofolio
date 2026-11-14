package io.github.rendyyy.qa.selenium.pages;

import io.github.rendyyy.qa.selenium.data.CheckoutInfo;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInformationPage extends BasePage {

    private static final By FIRST_NAME_INPUT = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME_INPUT = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE_INPUT = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE_BUTTON = By.cssSelector("[data-test='continue']");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutInformationPage waitUntilLoaded() {
        wait.visible(CONTINUE_BUTTON);
        return this;
    }

    @Step("Fill checkout information")
    public CheckoutInformationPage fillInformation(CheckoutInfo info) {
        type(FIRST_NAME_INPUT, info.firstName());
        type(LAST_NAME_INPUT, info.lastName());
        type(POSTAL_CODE_INPUT, info.postalCode());
        return this;
    }

    @Step("Continue to the order overview")
    public CheckoutOverviewPage continueToOverview() {
        click(CONTINUE_BUTTON);
        return new CheckoutOverviewPage(driver).waitUntilLoaded();
    }

    @Step("Continue and expect a validation error")
    public CheckoutInformationPage continueExpectingError() {
        click(CONTINUE_BUTTON);
        wait.visible(ERROR_MESSAGE);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
}