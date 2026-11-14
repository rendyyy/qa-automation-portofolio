package io.github.rendyyy.qa.selenium.tests.ui;

import io.github.rendyyy.qa.selenium.pages.LoginPage;
import io.github.rendyyy.qa.selenium.tests.base.BaseTest;
import org.testng.annotations.Test;

import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.REGRESSION;
import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.SMOKE;
import static org.assertj.core.api.Assertions.assertThat;

public class LogoutTests extends BaseTest {

    @Test(groups = {SMOKE, REGRESSION}, description = "Logging out returns the user to the login page")
    public void shouldReturnToLoginPageAfterLogout() {
        LoginPage loginPage = loginAsDefaultUser().header().logout();

        assertThat(loginPage.isLoginFormDisplayed()).isTrue();
    }

    @Test(groups = REGRESSION, description = "Protected pages are not reachable after logging out")
    public void shouldBlockProtectedPageAfterLogout() {
        LoginPage loginPage = loginAsDefaultUser()
                .header()
                .logout()
                .openInventoryWithoutSession();

        assertThat(loginPage.getErrorMessage())
                .isEqualTo("Epic sadface: You can only access '/inventory.html' when you are logged in.");
    }
}