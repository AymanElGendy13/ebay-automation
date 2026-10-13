import DriverSetup.DriverManager;
import Pages.Ebay;
import Pages.SearchResultsPage;
import Utils.AllureUtil;
import Utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.*;
import Listeners.TestNGListeners;

import static Utils.PropertiesUtil.getPropertyValue;

@Listeners(TestNGListeners.class)
public class EndToEndTest {

    //For Jenkins

    Ebay ebay;
    JsonUtils testDataOne;

    @BeforeClass
    public void beforeClass() {
        testDataOne = new JsonUtils("e2eScenarioOne-test-data");
    }

    @BeforeMethod
    public void setUp() {
        //Driver instance
        DriverManager.createDriverInstance(getPropertyValue("browserType"));
        //Initialize pages object
        ebay = new Ebay();
        //Perform actions
        ebay.openSite();
        DriverManager.maximizeWindow();
    }

    // Open Site --> Validate Main Page --> Search --> Validate Results --> Log Count --> Filter Transmission: Manual --> Validate Filter
    @Test(enabled = true, invocationCount = 1, priority = 1)
    public void searchAndFilterByManualTransmission() {

        // Verify main page
        Assert.assertTrue(ebay.getHomePageTitle().contains(testDataOne.getJsonData("assertions.homePageTitle")),
                "Home page title is not correct");
        Assert.assertTrue(ebay.getHomePageUrl().contains(testDataOne.getJsonData("assertions.homePageUrl")),
                "Home page url is not correct");
        Assert.assertTrue(ebay.isLogoDisplayed(), "eBay logo is not displayed");
        Assert.assertTrue(ebay.isSearchBoxDisplayed(), "Search box is not displayed");

        // The Transmission filter is only offered for vehicles that can ship to the delivery location
        ebay.setDeliveryLocation(
                testDataOne.getJsonData("deliveryLocation.country"),
                testDataOne.getJsonData("deliveryLocation.zipCode")
        );
        Assert.assertTrue(ebay.getDeliveryCountry().contains(testDataOne.getJsonData("assertions.deliveryCountry")),
                "Delivery location was not updated");

        // Search
        String keyword = testDataOne.getJsonData("search.keyword");
        ebay.searchFor(keyword);

        // Verify results
        SearchResultsPage resultsPage = ebay.getSearchResultsPage();
        Assert.assertTrue(resultsPage.getResultsHeading().toLowerCase().contains(keyword.toLowerCase()),
                "Results heading does not mention the searched keyword");

        int resultsCount = resultsPage.getResultsCount();
        AllureUtil.attachTextToAllureReport("Search results count", "Results for '" + keyword + "': " + resultsCount);
        Assert.assertTrue(resultsCount > 0, "No results were found for: " + keyword);

        String[] titleKeywords = testDataOne.getJsonData("search.titleKeywords").split(",");
        verifyTitlesMatchKeywords(resultsPage, titleKeywords);

        // Filter
        String filterName = testDataOne.getJsonData("filter.name");
        String filterValue = testDataOne.getJsonData("filter.value");
        resultsPage.applyFilter(filterName, filterValue);

        // Verify filter
        Assert.assertTrue(ebay.getHomePageUrl().contains(filterName + "=" + filterValue),
                "Url does not contain the applied filter");
        Assert.assertTrue(resultsPage.isFilterApplied(filterName, filterValue),
                filterName + " -> " + filterValue + " is not selected");

        int filteredCount = resultsPage.getResultsCount();
        AllureUtil.attachTextToAllureReport("Filtered results count", filterName + " -> " + filterValue + ": " + filteredCount);
        Assert.assertTrue(filteredCount > 0, "No results were found after applying the filter");
        Assert.assertTrue(filteredCount < resultsCount,
                "Filtered results (" + filteredCount + ") should be less than all results (" + resultsCount + ")");
        verifyTitlesMatchKeywords(resultsPage, titleKeywords);
    }

    private void verifyTitlesMatchKeywords(SearchResultsPage resultsPage, String[] titleKeywords) {
        var titles = resultsPage.getResultTitles();
        Assert.assertFalse(titles.isEmpty(), "No result titles were found on the page");
        for (String title : titles) {
            boolean matches = false;
            for (String titleKeyword : titleKeywords) {
                if (title.toLowerCase().contains(titleKeyword.trim().toLowerCase())) {
                    matches = true;
                    break;
                }
            }
            Assert.assertTrue(matches, "Result title does not match the search: " + title);
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.tearDown();
    }

}
