package Utils;

import DriverSetup.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;

public class AlertHandler {


    private AlertHandler() {
    }

    // Core alert operations
    @Step("Accepting alert")
    public static void accept() {
        Logs.info("Accepting alert");
        Waits.waitForAlertPresent(DriverManager.getDriver());
        getAlert().accept();
    }

    @Step("Dismissing alert")
    public static void dismiss() {
        Logs.info("Dismissing alert");
        Waits.waitForAlertPresent(DriverManager.getDriver());
        getAlert().dismiss();
    }

    @Step("Getting alert text")
    public static String getText() {
        Logs.info("Getting alert text");
        Waits.waitForAlertPresent(DriverManager.getDriver());
        return getAlert().getText();
    }

    @Step("Sending Keys to Alert")
    public static void sendKeys(String text) {
        Logs.info("Sending keys to alert: " + text);
        Waits.waitForAlertPresent(DriverManager.getDriver());
        getAlert().sendKeys(text);
    }

    // Verification methods
    public static void verifyTextContains(String expectedText) {
        Logs.info("Verifying alert text contains: " + expectedText);
        String actualText = getText();
        if (!actualText.contains(expectedText)) {
            throw new AssertionError("Alert text does not contain '" + expectedText + "'. Actual: " + actualText);
        }
    }

    public static void verifyTextMatches(String expectedText) {
        Logs.info("Verifying alert text matches: " + expectedText);
        String actualText = getText();
        if (!actualText.equals(expectedText)) {
            throw new AssertionError("Alert text mismatch. Expected: '" + expectedText + "', Actual: '" + actualText + "'");
        }
    }

    // Combined operations
    @Step("Verifying and accepting alert with text: {expectedText}")
    public static void verifyAndAccept(String expectedText) {
        verifyTextContains(expectedText);
        accept();
    }

    @Step("Verifying and dismissing alert with text: {expectedText}")
    public static void verifyAndDismiss(String expectedText) {
        verifyTextContains(expectedText);
        dismiss();
    }

    // Private helper method
    private static Alert getAlert() {
        return Waits.waitForAlertPresent(DriverManager.getDriver());
    }

}
