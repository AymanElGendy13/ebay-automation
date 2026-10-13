package Utils;

import DriverSetup.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class FrameHandler {

    private FrameHandler() {
    }

    @Step("Switching to frame: {locator}")
    public static void switchToFrame(By locator) {
        Logs.info("Switching to frame: " + locator);
        Waits.waitForFrameAndSwitch(DriverManager.getDriver(), locator);
    }

    @Step("Switching back to the main page")
    public static void switchToDefaultContent() {
        Logs.info("Switching back to the main page");
        DriverManager.getDriver().switchTo().defaultContent();
    }

}
