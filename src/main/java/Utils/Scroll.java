package Utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Scroll {

    private Scroll() {
    }

    // Scroll to a specific element
    public static void scrollToElement(WebDriver driver, WebElement element) {
        Logs.info("Scrolling to element: " + element);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // Scroll by a specific number of pixels (vertical scroll)
    @Step("Scrolling by pixels: {pixels}")
    public static void scrollByPixels(WebDriver driver, int pixels) {
        Logs.info("Scrolling by pixels: " + pixels);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
    }

    // Scroll to the top of the page
    @Step("Scrolling to the top of the page")
    public static void scrollToTop(WebDriver driver) {
        Logs.info("Scrolling to the top of the page");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0);");
    }

    // Scroll to the bottom of the page
    @Step("Scrolling to the bottom of the page")
    public static void scrollToBottom(WebDriver driver) {
        Logs.info("Scrolling to the bottom of the page");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }



}