package Pages;

import DriverSetup.DriverManager;
import Utils.Logs;
import Utils.Waits;
import core.ui.Button;
import core.ui.CheckBox;
import core.ui.Label;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchResultsPage extends Ebay {

    //Locators
    private final Label resultsHeading = new Label(By.cssSelector("h1.srp-controls__count-heading"));
    private final By resultTitles = By.cssSelector("li.s-card[data-listingid] .s-card__title span.su-styled-text.primary");

    // ebay adds a "Shop on eBay" placeholder card between the results
    private static final String PLACEHOLDER_TITLE = "Shop on eBay";

    public SearchResultsPage() {
        super();
    }

    @Step("Wait for the search results")
    public SearchResultsPage waitForResults() {
        waitForPage(resultsHeading.getLocator());
        return this;
    }

    // 70 results for mazda mx-5
    public String getResultsHeading() {
        return resultsHeading.getText();
    }

    // The heading is "70 results for ..." or "57,000+ results for ..."
    public int getResultsCount() {
        String heading = getResultsHeading();
        Matcher matcher = Pattern.compile("^([\\d,]+)\\+?\\s+results?").matcher(heading.trim());
        if (!matcher.find()) {
            throw new IllegalStateException("Could not read the number of results from the heading: " + heading);
        }
        int count = Integer.parseInt(matcher.group(1).replace(",", ""));
        Logs.info("Number of search results: " + count);
        return count;
    }

    public List<String> getResultTitles() {
        List<String> titles = new ArrayList<>();
        for (WebElement title : Waits.waitForAllElementsPresent(DriverManager.getDriver(), resultTitles)) {
            String text = title.getText().trim();
            if (!text.isEmpty() && !text.equals(PLACEHOLDER_TITLE)) {
                titles.add(text);
            }
        }
        Logs.info("Number of results listed on the page: " + titles.size());
        return titles;
    }

    @Step("Apply filter: {filterName} -> {filterValue}")
    public SearchResultsPage applyFilter(String filterName, String filterValue) {
        Button filterOption = new Button(By.xpath("//fieldset[legend[normalize-space()='aspect-" + filterName + "']]//a[.//input[@value='" + filterValue + "']]"));
        safeClick(filterOption);
        Waits.waitForUrlContains(DriverManager.getDriver(), filterName + "=" + filterValue);
        DriverManager.waitForPageToLoad();
        return waitForResults();
    }

    public boolean isFilterApplied(String filterName, String filterValue) {
        return new CheckBox(By.xpath("//input[@name='su-selection-group__aspect-" + filterName + "' and @value='" + filterValue + "']")).isChecked();
    }

}
