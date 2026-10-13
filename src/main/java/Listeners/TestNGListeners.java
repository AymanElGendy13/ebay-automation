package Listeners;

import Utils.AllureUtil;
import Utils.FilesUtil;
import Utils.Logs;
import Utils.Screenshots;
import org.testng.*;

import java.io.File;

import static Utils.PropertiesUtil.loadProperties;

public class TestNGListeners implements IExecutionListener, ITestListener, IInvokedMethodListener {

    File allureResults = new File("test-outputs/allure-results");
    File logs = new File("test-outputs/logs");
    File screenshots = new File("test-outputs/Screenshots");

    @Override
    public void onExecutionStart() {
        Logs.info("Test Execution Started");
        loadProperties();
        FilesUtil.deleteFiles(allureResults);
        FilesUtil.cleanDirectory(logs);
        FilesUtil.cleanDirectory(screenshots);
        FilesUtil.createDirectory(allureResults);
        FilesUtil.createDirectory(logs);
        FilesUtil.createDirectory(screenshots);
    }

    @Override
    public void onExecutionFinish() {
        Logs.info("Test Execution Finished");
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if(method.isTestMethod()) {
            Logs.info("Test Method " + method.getTestMethod().getMethodName() + " Finished");
            AllureUtil.attachLogsToAllureReport();
            switch (testResult.getStatus()) {
                case ITestResult.SUCCESS -> {
                    Screenshots.takeScreenShot("Passed_" + testResult.getName());
                    Logs.info("Test Method " + method.getTestMethod().getMethodName() + " Passed");
                }
                case ITestResult.FAILURE -> {
                    Screenshots.takeScreenShot("Failed_" + testResult.getName());
                    Logs.info("Test Method " + method.getTestMethod().getMethodName() + " Failed");
                }
                case ITestResult.SKIP -> {
                    Screenshots.takeScreenShot("Skipped_" + testResult.getName());
                    Logs.info("Test Method " + method.getTestMethod().getMethodName() + " Skipped");
                }
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        Logs.info("Test Case " + result.getName() + " Started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Logs.info("Test Case " + result.getName() + " Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Logs.info("Test Case " + result.getName() + " Failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Logs.info("Test Case " + result.getName() + " Skipped");
    }

}
