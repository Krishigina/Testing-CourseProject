package task1.tests;

import base.BaseSeleniumTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task1.pages.LamdaPage;

public class LamdaTestSampleAppTests extends BaseSeleniumTest {
    private static final Logger logger = LoggerFactory.getLogger(LamdaTestSampleAppTests.class);

    private void openApp() {
        driver.get("https://lambdatest.github.io/sample-todo-app/");
    }

    private LamdaPage initializePage() {
        openApp();
        return new LamdaPage();
    }

    @Test
    public void verifyPageHeader() {
        logger.info("Starting test: verifyPageHeader");
        LamdaPage lamdaPage = initializePage();

        String expectedHeader = "LambdaTest Sample App";
        String actualHeader = lamdaPage.getHeaderText();

        logger.info("Checking page header: expected '{}', found '{}'", expectedHeader, actualHeader);
        Assertions.assertEquals(expectedHeader, actualHeader, "Page header does not match the expected value.");
    }

    @Test
    public void verifyRemainingTextIsDisplayed() {
        logger.info("Starting test: verifyRemainingTextIsDisplayed");
        LamdaPage lamdaPage = initializePage();

        logger.info("Checking if remaining text is displayed");
        Assertions.assertTrue(lamdaPage.isRemainingTextPresent(), "Remaining text is not displayed.");
    }

    @Test
    public void verifyFirstItemIsNotChecked() {
        logger.info("Starting test: verifyFirstItemIsNotChecked");
        LamdaPage lamdaPage = initializePage();

        logger.info("Checking if the first item is not checked");
        boolean isNotChecked = lamdaPage.isFirstItemNotChecked();
        logger.info("First item not checked: {}", isNotChecked);
        Assertions.assertTrue(isNotChecked, "First item is expected to be not checked, but it is checked.");
    }

    @Test
    public void checkFirstItem() {
        logger.info("Starting test: checkFirstItem");
        LamdaPage lamdaPage = initializePage();

        logger.info("Checking the first item");
        lamdaPage.checkFirstItem();

        boolean isNotChecked = lamdaPage.isFirstItemNotChecked();
        logger.info("First item checked: {}", !isNotChecked);
        Assertions.assertFalse(isNotChecked, "First item is expected to be checked, but it is not.");
    }

    @Test
    public void checkAllItems() {
        logger.info("Starting test: checkAllItems");
        LamdaPage lamdaPage = initializePage();

        for (int i = 1; i <= 5; i++) {
            logger.info("Checking item {}", i);
            lamdaPage.checkFirstItem();
        }
    }

    @Test
    public void addAndVerifyNewTodoItem() {
        logger.info("Starting test: addAndVerifyNewTodoItem");
        LamdaPage lamdaPage = initializePage();

        String newItemText = "New Todo Item";
        logger.info("Adding new todo item: '{}'", newItemText);
        lamdaPage.addNewTodoItem(newItemText);

        boolean isNewItemAdded = lamdaPage.isTodoItemPresent(newItemText);
        logger.info("New todo item added: {}", isNewItemAdded);
        Assertions.assertTrue(isNewItemAdded, "New todo item was not added to the list.");

        logger.info("Checking new todo item");
        lamdaPage.checkFirstItem();
        Assertions.assertFalse(lamdaPage.isFirstItemNotChecked(), "New todo item is expected to be checked, but it is not.");
    }
}
