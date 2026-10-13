package core.ui;

import Utils.Logs;
import Utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class CheckBox extends Element {

    public CheckBox(By locator) {
        super(locator);
    }

    // Check boxes are usually styled, so the real input might not be visible, present is enough
    @Override
    protected WebElement waitForElement() {
        return Waits.waitForElementPresent(getDriver(), locator);
    }

    public boolean isChecked() {
        Logs.info("Checking if check box is selected: " + locator);
        return waitForElement().isSelected();
    }

    public void check() {
        Logs.info("Checking the check box: " + locator);
        if (!isChecked()) {
            click();
        }
    }

    public void uncheck() {
        Logs.info("Unchecking the check box: " + locator);
        if (isChecked()) {
            click();
        }
    }

    private void click() {
        WebElement element = waitAndScroll();
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            Logs.warn("Normal click was intercepted, clicking with javascript: " + locator);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
        }
    }

}
