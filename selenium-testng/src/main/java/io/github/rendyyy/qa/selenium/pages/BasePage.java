package io.github.rendyyy.qa.selenium.pages;

import io.github.rendyyy.qa.selenium.config.FrameworkConfig;
import io.github.rendyyy.qa.selenium.utils.WaitUtils;
import io.qameta.allure.Step;
import io.qameta.allure.Param;
import io.qameta.allure.model.Parameter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.List;

/** Common behaviour for every page object and page-level component. */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver, FrameworkConfig.explicitWait());
    }

    @Step("Open {url}")
    protected void open(String url) {
        driver.get(url);
    }

    @Step("Click {locator}")
    protected void click(By locator) {
        wait.clickable(locator).click();
    }

    @Step("Type into {locator}")
    protected void type(By locator, String text) {
        WebElement element = wait.visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return wait.visible(locator).getText().trim();
    }

    protected List<String> getTexts(By locator) {
        return wait.allVisible(locator).stream()
                .map(element -> element.getText().trim())
                .toList();
    }

    /** Immediate check without waiting; safe to use for asserting that something is absent. */
    protected boolean isDisplayed(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

        // Not delegating to type(): that would log a second, unmasked step with the same value.
        @Step("Type a masked value into {locator}")
    protected void typeMasked(By locator, @Param(mode = Parameter.Mode.MASKED) String text) {
        WebElement element = wait.visible(locator);
        element.clear();
        element.sendKeys(text);
    }
}