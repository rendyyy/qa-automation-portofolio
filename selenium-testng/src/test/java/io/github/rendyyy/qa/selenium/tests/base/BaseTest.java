package io.github.rendyyy.qa.selenium.tests.base;

import io.github.rendyyy.qa.selenium.config.FrameworkConfig;
import io.github.rendyyy.qa.selenium.driver.DriverManager;
import io.github.rendyyy.qa.selenium.pages.InventoryPage;
import io.github.rendyyy.qa.selenium.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected InventoryPage loginAsDefaultUser() {
        return new LoginPage(driver())
                .load()
                .loginAs(FrameworkConfig.username(), FrameworkConfig.password());
    }
}