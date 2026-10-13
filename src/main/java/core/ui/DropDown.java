package core.ui;

import Utils.Logs;
import Utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DropDown extends Element {

    public DropDown(By locator) {
        super(locator);
    }

    @Override
    protected WebElement waitForElement() {
        return Waits.waitForElementVisible(getDriver(), locator);
    }

    public void selectByVisibleText(String text) {
        Logs.info("Selecting: " + text + " from dropdown: " + locator);
        new Select(waitAndScroll()).selectByVisibleText(text);
    }

    public String getSelectedOption() {
        Logs.info("Getting selected option from dropdown: " + locator);
        return new Select(waitAndScroll()).getFirstSelectedOption().getText();
    }

}
