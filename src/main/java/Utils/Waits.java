package Utils;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static Utils.PropertiesUtil.getPropertyValue;

public class Waits {

    private static final int EXPLICIT_WAIT = Integer.parseInt(getPropertyValue("explicitWait"));
    private static final int POPUP_WAIT = Integer.parseInt(getPropertyValue("popupWait"));



    private Waits() {
    }

    @Step("Waiting for element to be present: {locator}")
    public static WebElement waitForElementPresent(WebDriver driver, By locator) {

        Logs.info("Waiting for element to be present: " + locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT)).
                until(driver1 -> driver1.findElement(locator));
    }

    @Step("Waiting for element to be Visible: {locator}")
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        Logs.info("Waiting for element to be visible: " + locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT)).
                until(driver1 ->
                        {
                            WebElement element = waitForElementPresent(driver, locator);
                            return element.isDisplayed() ? element : null;
                        }
                );
    }

    @Step("Waiting for element to be Clickable: {locator}")
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        Logs.info("Waiting for element to be clickable: " + locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(driver1 ->
                        {
                            WebElement element = waitForElementVisible(driver, locator);
                            return element.isEnabled() ? element : null;
                        }
                );
    }

    @Step("Waiting for all elements to be present: {locator}")
    public static List<WebElement> waitForAllElementsPresent(WebDriver driver, By locator) {
        Logs.info("Waiting for all elements to be present: " + locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    // For things that show up randomly (popups), returns null instead of failing when it never shows up
    @Step("Looking for optional element: {locator}")
    public static WebElement waitForOptionalElementClickable(WebDriver driver, By locator) {
        Logs.info("Looking for optional element: " + locator);
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(POPUP_WAIT))
                    .until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            Logs.info("Optional element did not show up: " + locator);
            return null;
        }
    }

    // true when the element is visible, false when the title shows up first (error pages)
    @Step("Waiting for element: {locator} or title containing: {titlePart}")
    public static boolean waitForElementVisibleOrTitleContains(WebDriver driver, By locator, String titlePart) {
        Logs.info("Waiting for element: " + locator + " or title containing: " + titlePart);
        String result = new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(driver1 ->
                        {
                            if (driver1.getTitle().contains(titlePart)) {
                                return "title";
                            }
                            List<WebElement> elements = driver1.findElements(locator);
                            return !elements.isEmpty() && elements.get(0).isDisplayed() ? "element" : null;
                        }
                );
        return result.equals("element");
    }

    @Step("Waiting for element to be invisible: {locator}")
    public static boolean waitForElementInvisible(WebDriver driver, By locator) {
        Logs.info("Waiting for element to be invisible: " + locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @Step("Waiting for text: {text} in element: {locator}")
    public static boolean waitForTextToBePresent(WebDriver driver, By locator, String text) {
        Logs.info("Waiting for text: " + text + " in element: " + locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    @Step("Waiting for element to be not present: {element}")
    public static boolean waitForElementNotPresent(WebDriver driver, WebElement element) {
        Logs.info("Waiting for element to be not present: " + element);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.stalenessOf(element));
    }

    @Step("Waiting for url to contain: {urlPart}")
    public static boolean waitForUrlContains(WebDriver driver, String urlPart) {
        Logs.info("Waiting for url to contain: " + urlPart);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.urlContains(urlPart));
    }

    @Step("Waiting for title to contain: {titlePart}")
    public static boolean waitForTitleContains(WebDriver driver, String titlePart) {
        Logs.info("Waiting for title to contain: " + titlePart);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.titleContains(titlePart));
    }

    @Step("Waiting for frame and switching to it: {locator}")
    public static WebDriver waitForFrameAndSwitch(WebDriver driver, By locator) {
        Logs.info("Waiting for frame to be available: " + locator);
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    @Step("Waiting for Alert to be present")
    public static Alert waitForAlertPresent(WebDriver driver) {
        Logs.info("Waiting for alert to be present");
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.alertIsPresent());
    }

}
