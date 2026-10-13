package Pages;

import DriverSetup.DriverManager;
import Utils.FrameHandler;
import Utils.Logs;
import Utils.Waits;
import core.ui.Button;
import core.ui.DropDown;
import core.ui.Label;
import core.ui.TextBox;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;

import static Utils.PropertiesUtil.getPropertyValue;

//vars
//locators
//actions

public class Ebay {

    SearchResultsPage searchResultsPage;

    public Ebay() {

    }

    public SearchResultsPage getSearchResultsPage() {
        if (searchResultsPage == null) {
            searchResultsPage = new SearchResultsPage();
        }
        return searchResultsPage;
    }

    //Locators
    //Header
    private final Label logo = new Label(By.cssSelector("a.gh-logo"));
    private final TextBox searchBox = new TextBox(By.id("gh-ac"));
    private final Button searchBtn = new Button(By.id("gh-search-btn"));
    //Delivery Location Locators
    private final Button shipToBtn = new Button(By.cssSelector("button.gh-ship-to__menu"));
    private final Label shipToCountry = new Label(By.cssSelector("span.gh-ship-to__menu-country-name"));
    private final By shipToFrame = By.cssSelector("iframe[src*='flowType=WIDGET']");
    private final By shippingPopupFrame = By.cssSelector("div.address-dialog__lightbox iframe[title='Shipping Address']");
    private final DropDown locationCountry = new DropDown(By.id("country"));
    private final TextBox locationZipCode = new TextBox(By.id("postalCode"));
    private final Button locationConfirmBtn = new Button(By.cssSelector("button.submit-button"));

    //Others
    private final By shippingPopup = By.cssSelector("div.address-dialog__lightbox");
    private final By shippingPopupCloseBtn = By.cssSelector("div.address-dialog__lightbox button.lightbox-dialog__close");
    private final By shipToCountryName = By.cssSelector("span.gh-ship-to__menu-country-name");

    //Actions
    @Step("Open eBay site")
    public Ebay openSite() {
        DriverManager.navigateToUrl(getPropertyValue("baseURL"), true);
        waitForPage(searchBox.getLocator());
        return this;
    }

    public String getHomePageTitle() {
        return DriverManager.getPageTitle();
    }

    public String getHomePageUrl() {
        return DriverManager.getCurrentUrl();
    }

    public boolean isLogoDisplayed() {
        return this.logo.isDisplayed();
    }

    public boolean isSearchBoxDisplayed() {
        return this.searchBox.isDisplayed();
    }

    // The "Are you shipping to ...?" popup has the same form as the header's "Ship to" menu, so use whichever is there
    @Step("Set delivery location to country: {country} and zip code: {zipCode}")
    public Ebay setDeliveryLocation(String country, String zipCode) {
        if (isShippingPopupDisplayed()) {
            FrameHandler.switchToFrame(shippingPopupFrame);
        } else {
            safeClick(this.shipToBtn);
            FrameHandler.switchToFrame(shipToFrame);
        }
        this.locationCountry.selectByVisibleText(country);
        this.locationZipCode.sendData(zipCode);
        this.locationConfirmBtn.click();
        FrameHandler.switchToDefaultContent();
        Waits.waitForTextToBePresent(DriverManager.getDriver(), shipToCountryName, country);
        return this;
    }

    public String getDeliveryCountry() {
        return this.shipToCountry.getText();
    }

    @Step("Search for: {keyword}")
    public Ebay searchFor(String keyword) {
        this.searchBox.sendData(keyword);
        safeClick(this.searchBtn);
        getSearchResultsPage().waitForResults();
        return this;
    }

    private boolean isShippingPopupDisplayed() {
        return Waits.waitForOptionalElementClickable(DriverManager.getDriver(), shippingPopupCloseBtn) != null;
    }

    // The "Are you shipping to ...?" popup shows up randomly and covers the page
    @Step("Close the shipping popup if it shows up")
    public void closeShippingPopup() {
        WebElement closeBtn = Waits.waitForOptionalElementClickable(DriverManager.getDriver(), shippingPopupCloseBtn);
        if (closeBtn != null) {
            closeBtn.click();
            Waits.waitForElementInvisible(DriverManager.getDriver(), shippingPopup);
        }
    }

    // ebay sometimes shows a short interstitial or answers with "Error Page" for no clear reason, a refresh fixes it
    protected void waitForPage(By readyLocator) {
        int retries = Integer.parseInt(getPropertyValue("errorPageRetries"));
        for (int attempt = 1; attempt <= retries; attempt++) {
            if (Waits.waitForElementVisibleOrTitleContains(DriverManager.getDriver(), readyLocator, "Error Page")) {
                return;
            }
            Logs.warn("eBay returned its Error Page, refreshing. Attempt: " + attempt);
            try {
                Thread.sleep(2000L * attempt);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            DriverManager.refreshPage();
        }
        Waits.waitForElementVisible(DriverManager.getDriver(), readyLocator);
    }

    protected void safeClick(Button button) {
        try {
            button.click();
        } catch (ElementClickInterceptedException e) {
            Logs.warn("Click was intercepted, closing the shipping popup and trying again: " + button.getLocator());
            closeShippingPopup();
            button.click();
        }
    }

}
