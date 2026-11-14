package io.github.rendyyy.qa.selenium.tests.ui;

import io.github.rendyyy.qa.selenium.data.CheckoutCase;
import io.github.rendyyy.qa.selenium.pages.CheckoutCompletePage;
import io.github.rendyyy.qa.selenium.pages.CheckoutInformationPage;
import io.github.rendyyy.qa.selenium.pages.CheckoutOverviewPage;
import io.github.rendyyy.qa.selenium.pages.InventoryPage;
import io.github.rendyyy.qa.selenium.tests.base.BaseTest;
import io.github.rendyyy.qa.selenium.tests.data.TestData;
import io.github.rendyyy.qa.selenium.tests.dataproviders.TestDataProviders;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.REGRESSION;
import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.SMOKE;
import static io.github.rendyyy.qa.selenium.tests.data.Products.BACKPACK;
import static io.github.rendyyy.qa.selenium.tests.data.Products.BIKE_LIGHT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class CheckoutTests extends BaseTest {

    @Test(groups = {SMOKE, REGRESSION}, description = "A user can complete an order end to end")
    public void shouldCompleteCheckoutWithValidInformation() {
        InventoryPage inventory = loginAsDefaultUser();
        inventory.addProductToCart(BACKPACK).addProductToCart(BIKE_LIGHT);
        BigDecimal expectedSubtotal = inventory.getProduct(BACKPACK).getPrice()
                .add(inventory.getProduct(BIKE_LIGHT).getPrice());

        CheckoutOverviewPage overview = inventory.header()
                .openCart()
                .proceedToCheckout()
                .fillInformation(TestData.validCheckoutInfo())
                .continueToOverview();

        assertSoftly(softly -> {
            softly.assertThat(overview.getItemNames()).containsExactlyInAnyOrder(BACKPACK, BIKE_LIGHT);
            softly.assertThat(overview.getSubtotal()).isEqualByComparingTo(expectedSubtotal);
            softly.assertThat(overview.getTotal())
                    .isEqualByComparingTo(overview.getSubtotal().add(overview.getTax()));
        });

        CheckoutCompletePage complete = overview.finishOrder();

        assertThat(complete.getConfirmationHeader()).isEqualTo("Thank you for your order!");
    }

    @Test(groups = REGRESSION,
            dataProvider = "invalidCheckoutInfo",
            dataProviderClass = TestDataProviders.class,
            description = "Missing checkout information is rejected with a validation error")
    public void shouldRejectInvalidCheckoutInformation(CheckoutCase testCase) {
        InventoryPage inventory = loginAsDefaultUser();
        inventory.addProductToCart(BACKPACK);

        CheckoutInformationPage informationPage = inventory.header()
                .openCart()
                .proceedToCheckout()
                .fillInformation(testCase.info())
                .continueExpectingError();

        assertThat(informationPage.getErrorMessage()).isEqualTo(testCase.expectedError());
    }
}