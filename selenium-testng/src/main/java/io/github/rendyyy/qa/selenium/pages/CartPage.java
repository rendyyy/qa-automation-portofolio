package io.github.rendyyy.qa.selenium.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class CartPage extends BasePage {

    private static final By CART_ITEM = By.cssSelector("[data-test='inventory-item']");
    private static final By ITEM_NAME = By.cssSelector("[data-test='inventory-item-name']");
    private static final By REMOVE_BUTTON = By.cssSelector("button[data-test^='remove']");
    private static final By CHECKOUT_BUTTON = By.cssSelector("[data-test='checkout']");
    private static final By CONTINUE_SHOPPING_BUTTON = By.cssSelector("[data-test='continue-shopping']");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage waitUntilLoaded() {
        wait.urlContains("cart.html");
        wait.visible(CHECKOUT_BUTTON);
        return this;
    }

    /** Immediate lookup so an empty cart returns an empty list instead of timing out. */
    public List<String> getItemNames() {
        return driver.findElements(ITEM_NAME).stream()
                .map(element -> element.getText().trim())
                .toList();
    }

    @Step("Remove {productName} from the cart page")
    public CartPage removeItem(String productName) {
        WebElement item = findItem(productName);
        wait.clickable(item.findElement(REMOVE_BUTTON)).click();
        // React may reuse DOM nodes, so wait on the visible outcome instead of element staleness.
        wait.until(d -> !getItemNames().contains(productName));
        return this;
    }

    @Step("Proceed to checkout")
    public CheckoutInformationPage proceedToCheckout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutInformationPage(driver).waitUntilLoaded();
    }

    @Step("Continue shopping")
    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
        return new InventoryPage(driver).waitUntilLoaded();
    }

    private WebElement findItem(String productName) {
        return driver.findElements(CART_ITEM).stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not in cart: " + productName));
    }
}