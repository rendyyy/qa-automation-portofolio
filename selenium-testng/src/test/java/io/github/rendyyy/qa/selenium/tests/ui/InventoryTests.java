package io.github.rendyyy.qa.selenium.tests.ui;

import io.github.rendyyy.qa.selenium.pages.InventoryPage;
import io.github.rendyyy.qa.selenium.pages.SortOption;
import io.github.rendyyy.qa.selenium.tests.base.BaseTest;
import io.github.rendyyy.qa.selenium.tests.dataproviders.TestDataProviders;
import io.github.rendyyy.qa.selenium.tests.data.Products;
import org.testng.annotations.Test;

import java.util.Comparator;

import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.REGRESSION;
import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.SMOKE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class InventoryTests extends BaseTest {

    @Test(groups = {SMOKE, REGRESSION}, description = "Catalogue lists every product with a name and a price")
    public void shouldListAllProducts() {
        InventoryPage inventory = loginAsDefaultUser();

        assertSoftly(softly -> {
            softly.assertThat(inventory.getProductNames()).containsExactlyInAnyOrderElementsOf(Products.ALL);
            softly.assertThat(inventory.getProductPrices()).allMatch(price -> price.signum() > 0, "has a positive price");
        });
    }

    @Test(groups = REGRESSION,
            dataProvider = "sortOptions",
            dataProviderClass = TestDataProviders.class,
            description = "Products are ordered according to the selected sort option")
    public void shouldSortProducts(SortOption option) {
        InventoryPage inventory = loginAsDefaultUser().sortBy(option);

        switch (option) {
            case NAME_A_TO_Z -> assertThat(inventory.getProductNames())
                    .isSortedAccordingTo(Comparator.naturalOrder());
            case NAME_Z_TO_A -> assertThat(inventory.getProductNames())
                    .isSortedAccordingTo(Comparator.reverseOrder());
            case PRICE_LOW_TO_HIGH -> assertThat(inventory.getProductPrices())
                    .isSortedAccordingTo(Comparator.naturalOrder());
            case PRICE_HIGH_TO_LOW -> assertThat(inventory.getProductPrices())
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }
    }

    @Test(groups = REGRESSION, description = "Add and remove buttons keep the cart badge in sync")
    public void shouldUpdateCartBadgeWhenAddingAndRemovingProducts() {
        InventoryPage inventory = loginAsDefaultUser();

        inventory.addProductToCart(Products.BACKPACK).addProductToCart(Products.BIKE_LIGHT);
        int badgeAfterAdding = inventory.header().getCartBadgeCount();

        inventory.removeProductFromCart(Products.BACKPACK);
        int badgeAfterRemoving = inventory.header().getCartBadgeCount();

        assertSoftly(softly -> {
            softly.assertThat(badgeAfterAdding).isEqualTo(2);
            softly.assertThat(badgeAfterRemoving).isEqualTo(1);
            softly.assertThat(inventory.getProduct(Products.BACKPACK).isAddedToCart()).isFalse();
            softly.assertThat(inventory.getProduct(Products.BIKE_LIGHT).isAddedToCart()).isTrue();
        });
    }
}