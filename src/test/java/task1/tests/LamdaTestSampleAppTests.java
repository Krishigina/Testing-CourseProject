package task1.tests;

import base.BaseSeleniumTest;
import base.TestListener;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task1.pages.LamdaPage;

@ExtendWith(TestListener.class)
@Feature("LambdaTest Application Tests")
public class LamdaTestSampleAppTests extends BaseSeleniumTest {

    private LamdaPage lamdaPage;

    @BeforeEach
    public void setUpEach() {
        Allure.step("Open LambdaTest sample app and initialize page", () -> {
            lamdaPage = new LamdaPage();
        });
    }

    @Test
    @DisplayName("Verify page header")
    @Story("Verify page header")
    @Description("This test verifies that the header of the LambdaTest sample app is correct.")
    public void verifyPageHeader() {
        logger.info("Starting test: verifyPageHeader");

        Allure.step("Get page header text", () -> {
            String expectedHeader = "LambdaTest Sample App";
            String actualHeader = lamdaPage.getHeaderText();
            logger.info("Checking page header: expected '{}', found '{}'", expectedHeader, actualHeader);
            Assertions.assertEquals(expectedHeader, actualHeader, "Page header does not match the expected value.");
        });
    }

    @Test
    @DisplayName("Verify remaining text")
    @Story("Verify remaining text is displayed")
    @Description("This test checks if the remaining text \"5 of 5 remaining\" is displayed on the LambdaTest sample app.")
    public void verifyRemainingTextIsDisplayed() {
        logger.info("Starting test: verifyRemainingTextIsDisplayed");

        Allure.step("Check if remaining text is displayed", () -> {
            boolean isDisplayed = lamdaPage.isRemainingTextPresent();
            logger.info("Remaining text displayed: {}", isDisplayed);
            Assertions.assertTrue(isDisplayed, "Remaining text is not displayed.");
        });
    }

    @Test
    @DisplayName("Verify first item is not checked")
    @Story("Verify first item is not checked")
    @Description("This test verifies that the first item in the todo list is not checked by default.")
    public void verifyFirstItemIsNotChecked() {
        logger.info("Starting test: verifyFirstItemIsNotChecked");

        Allure.step("Check if the first item is not checked", () -> {
            boolean isNotChecked = lamdaPage.isFirstItemNotChecked();
            logger.info("First item not checked: {}", isNotChecked);
            Assertions.assertTrue(isNotChecked, "First item is expected to be not checked, but it is checked.");
        });
    }

    @Test
    @DisplayName("Check first item of the list")
    @Story("Check first item")
    @Description("This test checks the first item in the todo list.")
    public void checkFirstItem() {
        logger.info("Starting test: checkFirstItem");

        Allure.step("Check the first item", lamdaPage::checkFirstItem);

        Allure.step("Verify the first item is checked", () -> {
            boolean isNotChecked = lamdaPage.isFirstItemNotChecked();
            logger.info("First item checked: {}", !isNotChecked);
            Assertions.assertFalse(isNotChecked, "First item is expected to be checked, but it is not.");
        });
    }

    @Test
    @DisplayName("Check all items of the list")
    @Story("Check all items")
    @Description("This test checks all items in the todo list.")
    public void checkAllItems() {
        logger.info("Starting test: checkAllItems");

        for (int i = 1; i <= 5; i++) {
            int itemNumber = i;
            Allure.step("Check item " + itemNumber, lamdaPage::checkFirstItem);
        }
    }

    @Test
    @DisplayName("Add and check new todo item")
    @Story("Add and check new todo item")
    @Description("This test adds a new todo item and verifies it is added to the list.")
    public void addAndVerifyNewTodoItem() {
        logger.info("Starting test: addAndVerifyNewTodoItem");

        String newItemText = "New Todo Item";
        Allure.step("Add new todo item: " + newItemText, () -> lamdaPage.addNewTodoItem(newItemText));

        Allure.step("Verify new todo item is added", () -> {
            boolean isNewItemAdded = lamdaPage.isTodoItemPresent(newItemText);
            logger.info("New todo item added: {}", isNewItemAdded);
            Assertions.assertTrue(isNewItemAdded, "New todo item was not added to the list.");
        });

        Allure.step("Check new todo item", lamdaPage::checkFirstItem);

        Allure.step("Verify the new item is checked", () -> {
            Assertions.assertFalse(lamdaPage.isFirstItemNotChecked(), "New todo item is expected to be checked, but it is not.");
        });
    }
}
