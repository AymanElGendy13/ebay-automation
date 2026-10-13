# eBay Automation Framework
<a href="https://selenium.dev"><img src="https://selenium.dev/images/selenium_logo_square_green.png" width="50" alt="Selenium"/></a>
![Java](https://img.shields.io/badge/Java-21-red) ![Selenium](https://img.shields.io/badge/Selenium-4.29-blue) ![TestNG](https://img.shields.io/badge/TestNG-7.11-orange) ![Allure](https://img.shields.io/badge/Reporting-AllureReports-green)

End-to-end test automation framework for the eBay website implementing a high-level search and filter scenario.

### Website
Link: [eBay Website](https://www.ebay.com/)

## 🧪 Scenario Covered

1. Navigate to eBay and validate that the main page is displayed (title, url, logo and search box).
2. Set the delivery location (read from the test data).
3. Search for `mazda mx-5`.
4. Validate the results (heading, number of results, titles of the listed items) and log the number of results.
5. From the left hand side filter choose `Transmission` -> `Manual`.
6. Validate the filter (url, selected check box, number of results, titles of the listed items).

> **Why the delivery location?** eBay only offers the `Transmission` filter when vehicles can be shipped to the
> delivery location of the visitor. With a delivery location where vehicles are not available (ex: Egypt) the left hand
> side only has Category / Price / Condition filters. So the country and the zip code are part of the test data
> (`deliveryLocation` in `e2eScenarioOne-test-data.json`) and the framework sets them from the "Are you shipping to ...?"
> popup, or the "Ship to" menu in the header when the popup does not show up.

## 🚀 Key Features

- **End-to-End Test Coverage** of the search and filter journey.
- **Page Object Model** and Fluent Pattern Interface design.
- **JSON Data-Driven** testing approach (nothing is hard coded in the test).
- **Multi-Layer Reporting**:
  - Allure Reports with steps, result counts and screenshots.
  - Log4j2 execution logs.
- **Advanced Features**:
  - TestNG Listeners for custom reporting.
  - Smart element waits with fluent patterns.
  - Common browser actions: navigation, refresh/back, alerts, frames, scroll, drop downs, check boxes.
  - Handles eBay's random "Error Page" (refresh and retry) and the shipping popup.
  - Cross-browser support (Chrome/Firefox/Edge).
  - CI/CD ready (Jenkins/GitHub Actions compatible).

## 🛠️ Technologies Used

| Component          | Technology Stack |
|--------------------|------------------|
| Test Framework     | TestNG 7.11      |
| Language           | Java 21          |
| Web Automation     | Selenium 4       |
| Design Pattern     | POM + Fluent pattern  |
| Data Management    | JSON files       |
| Reporting & Logging         | Allure Reports + Log4j2|
| Build Tool         | Maven            |
| Visual Validation  | Screenshot embedding |

## 📂 Project Structure
```
ebay-automation/
├── src/
│ ├── main/java/
│ │ ├── Pages/ # Page Objects with fluent methods (Ebay, SearchResultsPage)
│ │ ├── core.ui/ # Reusable UI components (Button, Label, TextBox, CheckBox, DropDown)
│ │ ├── Listeners/ # Custom TestNG listeners
│ │ ├── Utils/ # Helper classes (Waits, Scroll, AlertHandler, FrameHandler, JsonUtils, ...)
│ │ └── DriverSetup/ # Driver/browser factories
| | └── resources/ # All properties files
│ │
│ └── test/java/
│ │ ├── EndToEndTest # TestNG test class
│ ├── resources/ # JSON test data files
│ 
│
├── Postman/ # Task 1: Postman collection + environment (Reqres API)
├── test-outputs/
│ ├── logs/ # Log4j2 output
│ ├── allure-results/ # Allure raw reports
│ └── Screenshots/ # Captures
└── pom.xml # Maven dependencies
└── testng.xml # configuration file for organizing and executing tests in TestNG
```

## ⚙️ Configuration

| File | What is inside |
|------|----------------|
| `src/main/resources/environment.properties` | `baseURL` |
| `src/main/resources/web.properties` | `browserType` (chrome, edge, firefox) and `executionType` (Local, or anything else to run headless) |
| `src/main/resources/wait.properties` | `explicitWait`, `popupWait`, `errorPageRetries` |
| `src/test/resources/e2eScenarioOne-test-data.json` | delivery location, search keyword, filter and the expected values |

Any property can be overridden from the command line, ex: `mvn test -Pend-to-end-tests -DbrowserType=edge`

### Installation
```bash
- git clone https://github.com/AymanElGendy13/ebay-automation.git
- cd ebay-automation
- use: mvn test -Pend-to-end-tests (To Run the Project Suite) or Run directly through testng.xml file.
```

### Reports
```bash
- allure serve test-outputs/allure-results (Opens the Allure report)
- Logs: test-outputs/logs
- Screenshots: test-outputs/Screenshots
```

##  Task 1: API Testing (Postman)

The collection and the environment are inside the `Postman/` folder:

- `Reqres_API.postman_collection.json`
- `Reqres.postman_environment.json`

Import both in Postman and select the `Reqres Environment`, or run them from the terminal with newman:
```bash
- newman run Postman/Reqres_API.postman_collection.json -e Postman/Reqres.postman_environment.json
```

##  Task 2: Is already this project
