package Utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class EventHandler {

    private EventHandler() {

    }

    public static void waitForAnimationComplete(WebDriver driver, By locator) {
        WebElement element = Waits.waitForElementPresent(driver, locator);

        // JavaScript that properly handles the WebElement to DOM element conversion
        String script =
                "var element = arguments[0];" +
                        "var callback = arguments[1];" +
                        "" +
                        "if (window.getComputedStyle(element).animationName === 'none') {" +
                        "   callback(true);" +
                        "   return;" +
                        "}" +
                        "" +
                        "var handler = function() {" +
                        "   element.removeEventListener('animationend', handler);" +
                        "   callback(true);" +
                        "};" +
                        "" +
                        "element.addEventListener('animationend', handler);";

        // Use executeAsyncScript for proper Promise handling
        ((JavascriptExecutor)driver).executeAsyncScript(script, element);
    }

    // Specific method for success animation
    @Step("Waiting for success animation to complete for element: {locator}")
    public static void waitForSuccessAnimation(WebDriver driver, By locator) {
        Logs.info("Waiting for success animation to complete for element: " + locator);
        waitForAnimationComplete(driver, locator);
    }

}
