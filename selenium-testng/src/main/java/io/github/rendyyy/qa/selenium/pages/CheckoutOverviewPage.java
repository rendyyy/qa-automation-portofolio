package io.github.rendyyy.qa.selenium.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutOverviewPage extends BasePage {

    private static final By ITEM_NAME = By.cssSelector("[data-test='inventory-item-name']");
    private static final By SUBTOTAL_LABEL = By.cssSelector("[data-test='subtotal-label']");
    private static final By TAX_LABEL = By.cssSelector("[data-test='tax-label']");
    private static final By TOTAL_LABEL = By.cssSelector("[data-test='total-label']");
    private static final By FINISH_BUTTON = By.cssSelector("[data-test='finish']");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutOverviewPage waitUntilLoaded() {
        wait.visible(FINISH_BUTTON);
        return this;
    }

    public List<String> getItemNames() {
        return getTexts(ITEM_NAME);
    }

    public BigDecimal getSubtotal() {
        return toAmount(getText(SUBTOTAL_LABEL));
    }

    public BigDecimal getTax() {
        return toAmount(getText(TAX_LABEL));
    }

    public BigDecimal getTotal() {
        return toAmount(getText(TOTAL_LABEL));
    }

    @Step("Finish the order")
    public CheckoutCompletePage finishOrder() {
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(driver).waitUntilLoaded();
    }

    /** Turns labels such as "Item total: $39.98" into 39.98. */
    private static BigDecimal toAmount(String label) {
        return new BigDecimal(label.substring(label.indexOf('$') + 1).trim());
    }
}