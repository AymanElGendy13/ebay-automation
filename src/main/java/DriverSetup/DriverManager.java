package DriverSetup;

import Utils.Logs;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @Step("Create Driver Instance On: {browserName}")
    public static void createDriverInstance(String browserName) {
        WebDriver driver = BrowserFactory.getBrowser(browserName);
        Logs.info("Created Driver instance: " + driver);
        setDriver(driver);
        getDriver();
    }

    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public static WebDriver getDriver() {
        if(driverThreadLocal.get() == null) {
            Logs.error("Driver instance is not initialized (NULL)");
            throw new IllegalStateException("Driver instance is not initialized");
        }

        return driverThreadLocal.get();
    }


    @Step("Navigate to URL: {url} with waitForPageLoad: {waitForPageLoad}")
    public static void navigateToUrl(String url, boolean waitForPageLoad) {
        WebDriver driver = getDriver();
        if (driver == null) {
            throw new IllegalStateException("Driver instance is not initialized");
        }

        try {
            driver.navigate().to(url);
            if (waitForPageLoad) {
                waitForPageToLoad();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to navigate to URL: " + url, e);
        }
    }

    // Overloaded method with default wait behavior
    @Step("Navigate to URL: {url}")
    public void navigateToUrl(String url) {
        navigateToUrl(url, true);
    }

    // Helper method to wait for page load
    public static void waitForPageToLoad() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(d -> ((JavascriptExecutor) d)
                        .executeScript("return document.readyState").equals("complete"));
    }

    public static String getPageTitle() {
        String title = getDriver().getTitle();
        Logs.info("Page title: " + title);
        return title;
    }

    public static String getCurrentUrl() {
        String url = getDriver().getCurrentUrl();
        Logs.info("Current URL: " + url);
        return url;
    }

    @Step("Refresh the page")
    public static void refreshPage() {
        getDriver().navigate().refresh();
        waitForPageToLoad();
    }

    @Step("Navigate back")
    public static void navigateBack() {
        getDriver().navigate().back();
        waitForPageToLoad();
    }

    @Step("Maximize Window")
    public static void maximizeWindow() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.manage().window().maximize();
            } catch (Exception e) {
                Logs.info("Failed to maximize window: " + e.getMessage());
            }
        }
    }

    @Step("Quit Driver")
    public static void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                Logs.error("Failed to quit driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
