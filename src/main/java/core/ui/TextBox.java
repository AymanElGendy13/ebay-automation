package core.ui;

import Utils.Logs;
import Utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TextBox extends Element {
    public TextBox(By locator) {
        super(locator);
    }

    @Override
    protected WebElement waitForElement() {
        return Waits.waitForElementVisible(getDriver(), locator);
    }

    public void sendData(String data) {
        Logs.info("Sending data to textbox: " + locator + " with data: " + data);
        Waits.waitForElementPresent(getDriver(), locator);
        WebElement element = waitAndScroll();
        element.clear();
        element.sendKeys(data);
    }

}