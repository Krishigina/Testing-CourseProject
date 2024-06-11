package task1.tests;

import base.BaseSeleniumTest;
import base.TestListener;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task1.pages.LamdaPage;

@ExtendWith(TestListener.class)

@Feature("LambdaTest Application Tests")
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
    @DisplayName("Verify page header")
    @Story("Verify page header")
    @Description("This test verifies that the header of the LambdaTest sample app is correct.")
    public void verifyPageHeader() {
        logger.info("Starting test: verifyPageHeader");
        LamdaPage lamdaPage = initializePage();

        String expectedHeader = "LambdaTest Sample App";
        String actualHeader = lamdaPage.getHeaderText();

        logger.info("Checking page header: expected '{}', found '{}'", expectedHeader, actualHeader);
        Assertions.assertEquals(expectedHeader, actualHeader, "Page header does not match the expected value.");
    }

    @Test
    @DisplayName("Verify remaining text")
    @Story("Verify remaining text is displayed")
    @Description("This test checks if the remaining text \"5 of 5 remaining\" is displayed on the LambdaTest sample app.")
    public void verifyRemainingTextIsDisplayed() {
        logger.info("Starting test: verifyRemainingTextIsDisplayed");
        LamdaPage lamdaPage = initializePage();

        logger.info("Checking if remaining text is displayed");
        Assertions.assertTrue(lamdaPage.isRemainingTextPresent(), "Remaining text is not displayed.");
    }

    @Test
    @DisplayName("Verify first item is not checked")
    @Story("Verify first item is not checked")
    @Description("This test verifies that the first item in the todo list is not checked by default.")
    public void verifyFirstItemIsNotChecked() {
        logger.info("Starting test: verifyFirstItemIsNotChecked");
        LamdaPage lamdaPage = initializePage();

        logger.info("Checking if the first item is not checked");
        boolean isNotChecked = lamdaPage.isFirstItemNotChecked();
        logger.info("First item not checked: {}", isNotChecked);
        Assertions.assertTrue(isNotChecked, "First item is expected to be not checked, but it is checked.");
    }

    @Test
    @DisplayName("Check first item of the list")
    @Story("Check first item")
    @Description("This test checks the first item in the todo list.")

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
    @DisplayName("Check all items of the list")
    @Story("Check all items")
    @Description("This test checks all items in the todo list.")
    public void checkAllItems() {
        logger.info("Starting test: checkAllItems");
        LamdaPage lamdaPage = initializePage();

        for (int i = 1; i <= 5; i++) {
            logger.info("Checking item {}", i);
            lamdaPage.checkFirstItem();
        }
    }

    @Test
    @DisplayName("Add and check new todo item")
    @Story("Add and check new todo item")
    @Description("This test adds a new todo item and verifies it is added to the list.")
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
