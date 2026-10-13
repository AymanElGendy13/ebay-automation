package core.ui;

import Utils.Logs;
import Utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Label extends Element {
    public Label(By locator) {
        super(locator);
    }

    @Override
    protected WebElement waitForElement() {
        return Waits.waitForElementVisible(getDriver(), locator);
    }

    public String getText() {
        Logs.info("Getting text from label: " + locator);
        return waitAndScroll().getText();
    }

}
