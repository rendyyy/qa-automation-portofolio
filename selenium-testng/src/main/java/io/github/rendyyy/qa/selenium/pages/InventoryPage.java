package io.github.rendyyy.qa.selenium.pages;

import io.github.rendyyy.qa.selenium.pages.components.HeaderComponent;
import io.github.rendyyy.qa.selenium.pages.components.ProductCardComponent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public class InventoryPage extends BasePage {

    private static final By HEADING = By.cssSelector("[data-test='title']");
    private static final By PRODUCT_CARD = By.cssSelector("[data-test='inventory-item']");
    private static final By SORT_DROPDOWN = By.cssSelector("[data-test='product-sort-container']");
    private static final By ACTIVE_SORT_OPTION = By.cssSelector("[data-test='active-option']");

    private final HeaderComponent header;

    public InventoryPage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }

    public InventoryPage waitUntilLoaded() {
        wait.visible(HEADING);
        wait.allVisible(PRODUCT_CARD);
        return this;
    }

    public HeaderComponent header() {
        return header;
    }

    public String getHeading() {
        return getText(HEADING);
    }

    /** Cards are looked up on every call because sorting re-renders the list. */
    public List<ProductCardComponent> getProducts() {
        return wait.allVisible(PRODUCT_CARD).stream()
                .map(root -> new ProductCardComponent(root, wait))
                .toList();
    }

    public int getProductCount() {
        return getProducts().size();
    }

    public List<String> getProductNames() {
        return getProducts().stream().map(ProductCardComponent::getName).toList();
    }

    public List<BigDecimal> getProductPrices() {
        return getProducts().stream().map(ProductCardComponent::getPrice).toList();
    }

    public ProductCardComponent getProduct(String name) {
        return getProducts().stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found on inventory page: " + name));
    }

    @Step("Sort products by {option}")
    public InventoryPage sortBy(SortOption option) {
        new Select(wait.visible(SORT_DROPDOWN)).selectByValue(option.value());
        wait.until(ExpectedConditions.textToBe(ACTIVE_SORT_OPTION, option.label()));
        return this;
    }

    @Step("Add {productName} to the cart")
    public InventoryPage addProductToCart(String productName) {
        getProduct(productName).addToCart();
        return this;
    }

    @Step("Remove {productName} from the cart")
    public InventoryPage removeProductFromCart(String productName) {
        getProduct(productName).removeFromCart();
        return this;
    }
}