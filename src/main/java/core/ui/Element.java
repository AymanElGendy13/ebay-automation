package core.ui;

import DriverSetup.DriverManager;
import Utils.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class Element {

    protected By locator;

    public Element(By locator) {
        this.locator = locator;
    }

    protected WebDriver getDriver() {
        Logs.info("Getting driver from ThreadLocal");
        return DriverManager.getDriver(); // Gets driver from ThreadLocal
    }

    // Element uses waitForElementPresent as default
    protected WebElement waitForElement() {
        return Waits.waitForElementPresent(getDriver(), locator);
    }

    protected WebElement waitAndScroll() {
        WebDriver driver = getDriver();
        WebElement element = waitForElement(); // Get the element first
        Scroll.scrollToElement(driver, element); // Pass WebElement to scroll
        return element;
    }

    public boolean isDisplayed() {
        try {
            return waitAndScroll().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public By getLocator() {
        return locator;
    }

}
