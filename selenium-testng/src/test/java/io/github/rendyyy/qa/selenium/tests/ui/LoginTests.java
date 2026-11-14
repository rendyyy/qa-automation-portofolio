package io.github.rendyyy.qa.selenium.tests.ui;

import io.github.rendyyy.qa.selenium.config.FrameworkConfig;
import io.github.rendyyy.qa.selenium.data.LoginCase;
import io.github.rendyyy.qa.selenium.pages.InventoryPage;
import io.github.rendyyy.qa.selenium.pages.LoginPage;
import io.github.rendyyy.qa.selenium.tests.base.BaseTest;
import io.github.rendyyy.qa.selenium.tests.dataproviders.TestDataProviders;
import org.testng.annotations.Test;

import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.REGRESSION;
import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.SMOKE;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class LoginTests extends BaseTest {

    @Test(groups = {SMOKE, REGRESSION}, description = "Valid credentials open the products page")
    public void shouldLoginWithValidCredentials() {
        InventoryPage inventory = new LoginPage(driver())
                .load()
                .loginAs(FrameworkConfig.username(), FrameworkConfig.password());

        assertSoftly(softly -> {
            softly.assertThat(inventory.getHeading()).isEqualTo("Products");
            softly.assertThat(inventory.getCurrentUrl()).contains("inventory.html");
        });
    }

    @Test(groups = REGRESSION,
            dataProvider = "invalidLogins",
            dataProviderClass = TestDataProviders.class,
            description = "Invalid credentials show an error and keep the user on the login page")
    public void shouldRejectInvalidLogin(LoginCase testCase) {
        LoginPage loginPage = new LoginPage(driver())
                .load()
                .loginExpectingError(testCase.username(), testCase.password());

        assertSoftly(softly -> {
            softly.assertThat(loginPage.getErrorMessage()).isEqualTo(testCase.expectedError());
            softly.assertThat(loginPage.isLoginFormDisplayed()).isTrue();
        });
    }
}