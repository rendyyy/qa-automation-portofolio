package io.github.rendyyy.qa.selenium.pages.components;

import io.github.rendyyy.qa.selenium.pages.CartPage;
import io.github.rendyyy.qa.selenium.pages.LoginPage;
import io.github.rendyyy.qa.selenium.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Header shown on every page after login: menu, logout and cart. */
public class HeaderComponent extends BasePage {

    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.cssSelector("[data-test='logout-sidebar-link']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    @Step("Open the cart")
    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver).waitUntilLoaded();
    }

    @Step("Log out via the side menu")
    public LoginPage logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
        return new LoginPage(driver).waitUntilLoaded();
    }

    /** Returns 0 when the badge is not rendered (empty cart). */
    public int getCartBadgeCount() {
        return isDisplayed(CART_BADGE) ? Integer.parseInt(getText(CART_BADGE)) : 0;
    }
}