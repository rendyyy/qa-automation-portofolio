package io.github.rendyyy.qa.selenium.pages.platform;

import io.github.rendyyy.qa.selenium.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class AdminMessagesPage extends BasePage {

    public AdminMessagesPage(WebDriver driver) {
        super(driver);
    }

    public AdminMessagesPage waitUntilLoaded() {
        // The admin route is /admin/message (singular)
        wait.urlContains("/message");
        return this;
    }

    /** Waits for the subject to appear in the message list; returns false if it never does. */
    @Step("Check that the message with subject '{subject}' is listed")
    public boolean isMessageListed(String subject) {
        try {
            wait.visible(byText(subject));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Matches any element that has a text node containing the given text. */
    private static By byText(String text) {
        if (text.contains("'")) {
            throw new IllegalArgumentException("Text locators do not support single quotes: " + text);
        }
        return By.xpath("//*[text()[contains(normalize-space(.), '" + text + "')]]");
    }
}