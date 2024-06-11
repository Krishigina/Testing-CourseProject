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
    private static final Logger logger = LoggerFactory.getLogger(LamdaTestSampleAppTests.class);

    @BeforeEach
    public void setUpEach() {
        Allure.step("Open LambdaTest sample app and initialize page", () -> {
            lamdaPage = new LamdaPage();
        });
    }

    @Test
    @DisplayName("Test for LambdaTest sample app")
    @Story("Complete workflow for LambdaTest sample app")
    @Description("This test performs a series of checks and actions on the LambdaTest sample app.")
    public void fullTest() {
        logger.info("Starting comprehensive test for LambdaTest sample app");



        Allure.step("Verify '5 of 5 remaining' text is displayed", () -> {
            boolean isDisplayed = lamdaPage.isRemainingTextPresent();
            logger.info("'5 of 5 remaining' text displayed: {}", isDisplayed);
            Assertions.assertTrue(isDisplayed, "'5 of 5 remaining' text is not displayed.");
        });

        Allure.step("Verify first item is not checked", () -> {
            boolean isNotChecked = lamdaPage.isFirstItemNotChecked();
            logger.info("First item not checked: {}", isNotChecked);
            Assertions.assertTrue(isNotChecked, "First item is expected to be not checked, but it is checked.");
        });

        Allure.step("Check the first item", lamdaPage::checkFirstItem);

        for (int i = 2; i <= 5; i++) {
            int itemNumber = i;
            Allure.step("Verify item " + itemNumber + " is not checked", () -> {
                boolean isNotChecked = lamdaPage.isItemNotChecked(itemNumber);
                logger.info("Item {} not checked: {}", itemNumber, isNotChecked);
                Assertions.assertTrue(isNotChecked, "Item " + itemNumber + " is expected to be not checked, but it is checked.");
            });

            Allure.step("Check item " + itemNumber, () -> {
                lamdaPage.checkItem(itemNumber);
                boolean isChecked = !lamdaPage.isItemNotChecked(itemNumber);
                logger.info("Item {} checked: {}", itemNumber, isChecked);
                Assertions.assertFalse(lamdaPage.isItemNotChecked(itemNumber), "Item " + itemNumber + " is expected to be checked, but it is not.");
            });
        }

        String newItemText = "New Todo Item";
        Allure.step("Add new todo item: " + newItemText, () -> lamdaPage.addNewTodoItem(newItemText));

        Allure.step("Verify new todo item is added", () -> {
            boolean isNewItemAdded = lamdaPage.isTodoItemPresent(newItemText);
            logger.info("New todo item added: {}", isNewItemAdded);
            Assertions.assertTrue(isNewItemAdded, "New todo item was not added to the list.");
        });

        Allure.step("Check new todo item", () -> {
            lamdaPage.checkTodoItem(newItemText);
            boolean isNewItemChecked = lamdaPage.isTodoItemChecked(newItemText);
            logger.info("New todo item checked: {}", isNewItemChecked);
            Assertions.assertTrue(isNewItemChecked, "New todo item is expected to be checked, but it is not.");
        });
    }
}
