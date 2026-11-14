package io.github.rendyyy.qa.selenium.pages.components;

import io.github.rendyyy.qa.selenium.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;

/** One product tile on the inventory page. Every lookup is scoped to the tile's root element. */
public class ProductCardComponent {

    private static final By NAME = By.cssSelector("[data-test='inventory-item-name']");
    private static final By PRICE = By.cssSelector("[data-test='inventory-item-price']");
    private static final By ADD_BUTTON = By.cssSelector("button[data-test^='add-to-cart']");
    private static final By REMOVE_BUTTON = By.cssSelector("button[data-test^='remove']");

    private final WebElement root;
    private final WaitUtils wait;

    public ProductCardComponent(WebElement root, WaitUtils wait) {
        this.root = root;
        this.wait = wait;
    }

    public String getName() {
        return root.findElement(NAME).getText().trim();
    }

    public BigDecimal getPrice() {
        String raw = root.findElement(PRICE).getText();
        return new BigDecimal(raw.replace("$", "").trim());
    }

    public boolean isAddedToCart() {
        return !root.findElements(REMOVE_BUTTON).isEmpty();
    }

    @Step("Add product to cart")
    public void addToCart() {
        wait.clickable(root.findElement(ADD_BUTTON)).click();
        // The button is swapped for "Remove"; wait for it so callers never read a stale state.
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, REMOVE_BUTTON));
    }

    @Step("Remove product from cart")
    public void removeFromCart() {
        wait.clickable(root.findElement(REMOVE_BUTTON)).click();
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(root, ADD_BUTTON));
    }
}