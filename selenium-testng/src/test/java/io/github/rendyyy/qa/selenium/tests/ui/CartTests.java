package io.github.rendyyy.qa.selenium.tests.ui;

import io.github.rendyyy.qa.selenium.pages.CartPage;
import io.github.rendyyy.qa.selenium.pages.InventoryPage;
import io.github.rendyyy.qa.selenium.tests.base.BaseTest;
import org.testng.annotations.Test;

import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.REGRESSION;
import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.SMOKE;
import static io.github.rendyyy.qa.selenium.tests.data.Products.BACKPACK;
import static io.github.rendyyy.qa.selenium.tests.data.Products.BIKE_LIGHT;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class CartTests extends BaseTest {

    @Test(groups = {SMOKE, REGRESSION}, description = "Added products appear in the cart")
    public void shouldShowAddedProductsInCart() {
        InventoryPage inventory = loginAsDefaultUser();
        inventory.addProductToCart(BACKPACK).addProductToCart(BIKE_LIGHT);

        CartPage cart = inventory.header().openCart();

        assertSoftly(softly -> {
            softly.assertThat(cart.getItemNames()).containsExactlyInAnyOrder(BACKPACK, BIKE_LIGHT);
            softly.assertThat(inventory.header().getCartBadgeCount()).isEqualTo(2);
        });
    }

    @Test(groups = REGRESSION, description = "A product removed from the cart page disappears from the list")
    public void shouldRemoveProductFromCart() {
        InventoryPage inventory = loginAsDefaultUser();
        inventory.addProductToCart(BACKPACK).addProductToCart(BIKE_LIGHT);

        CartPage cart = inventory.header().openCart().removeItem(BACKPACK);

        assertSoftly(softly -> {
            softly.assertThat(cart.getItemNames()).containsExactly(BIKE_LIGHT);
            softly.assertThat(inventory.header().getCartBadgeCount()).isEqualTo(1);
        });
    }

    @Test(groups = REGRESSION, description = "A new session starts with an empty cart")
    public void shouldStartWithEmptyCart() {
        InventoryPage inventory = loginAsDefaultUser();

        CartPage cart = inventory.header().openCart();

        assertSoftly(softly -> {
            softly.assertThat(cart.getItemNames()).isEmpty();
            softly.assertThat(inventory.header().getCartBadgeCount()).isZero();
        });
    }

    @Test(groups = REGRESSION, description = "Cart contents survive a trip back to the product list")
    public void shouldKeepCartContentsAfterContinueShopping() {
        InventoryPage inventory = loginAsDefaultUser();
        inventory.addProductToCart(BACKPACK);

        InventoryPage returnedInventory = inventory.header().openCart().continueShopping();

        assertSoftly(softly -> {
            softly.assertThat(returnedInventory.header().getCartBadgeCount()).isEqualTo(1);
            softly.assertThat(returnedInventory.getProduct(BACKPACK).isAddedToCart()).isTrue();
        });
    }
}