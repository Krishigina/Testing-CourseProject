package task1.pages;

import base.BaseSeleniumPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class LamdaPage extends BaseSeleniumPage {
    private static final Logger logger = LoggerFactory.getLogger(LamdaPage.class);

    // Locators for page elements
    @FindBy(xpath = "//h2")
    private WebElement header;

    @FindBy(xpath = "//span[@class='ng-binding'][contains(text(), '5 of 5 remaining')]")
    private WebElement remainingText;

    @FindBy(xpath = "//li")
    private List<WebElement> todoItems;

    @FindBy(id = "sampletodotext")
    private WebElement newTodoInput;

    @FindBy(id = "addbutton")
    private WebElement addButton;

    public LamdaPage() {
        driver.get("https://lambdatest.github.io/sample-todo-app/");
        PageFactory.initElements(driver, this);
    }

    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public String getHeaderText() {
        wait.until(ExpectedConditions.visibilityOf(header));
        return header.getText();
    }

    public boolean isRemainingTextPresent() {
        wait.until(ExpectedConditions.visibilityOf(remainingText));
        return remainingText.isDisplayed();
    }

    public boolean isFirstItemNotChecked() {
        return isItemNotChecked(1);
    }

    public boolean isItemNotChecked(int itemIndex) {
        try {
            WebElement itemCheckbox = todoItems.get(itemIndex - 1).findElement(By.xpath(".//input[@type='checkbox']"));
            WebElement itemSpan = todoItems.get(itemIndex - 1).findElement(By.xpath(".//span"));
            String spanClass = itemSpan.getAttribute("class");
            boolean isNotChecked = spanClass.contains("done-false");
            logger.info("Item {} span class: {}", itemIndex, spanClass);
            logger.info("Item {} span class contains 'done-false': {}", itemIndex, isNotChecked);
            return isNotChecked;
        } catch (Exception e) {
            logger.error("Failed to verify if item {} is not checked", itemIndex, e);
            return false;
        }
    }

    public void checkFirstItem() {
        checkItem(1);
    }

    public void checkItem(int itemIndex) {
        try {
            WebElement itemCheckbox = todoItems.get(itemIndex - 1).findElement(By.xpath(".//input[@type='checkbox']"));
            logAndClick(itemCheckbox);
        } catch (Exception e) {
            logger.error("Failed to check item {}", itemIndex, e);
        }
    }

    public void addNewTodoItem(String todoText) {
        wait.until(ExpectedConditions.visibilityOf(newTodoInput)).sendKeys(todoText);
        logAndClick(addButton);
        waitForElement(By.xpath("//li/span[contains(text(), '" + todoText + "')]"));
    }

    public boolean isTodoItemPresent(String todoText) {
        try {
            waitForElement(By.xpath("//li/span[contains(text(), '" + todoText + "')]"));
            return true;
        } catch (Exception e) {
            logger.error("Todo item '{}' not found", todoText, e);
            return false;
        }
    }

    public void checkTodoItem(String todoText) {
        try {
            WebElement itemCheckbox = waitForElement(By.xpath("//li[span[text()='" + todoText + "']]//input[@type='checkbox']"));
            logAndClick(itemCheckbox);
        } catch (Exception e) {
            logger.error("Failed to check todo item '{}'", todoText, e);
        }
    }

    public boolean isTodoItemChecked(String todoText) {
        try {
            WebElement itemSpan = waitForElement(By.xpath("//li[span[text()='" + todoText + "']]//span"));
            String spanClass = itemSpan.getAttribute("class");
            boolean isChecked = spanClass.contains("done-true");
            logger.info("Todo item '{}' span class: {}", todoText, spanClass);
            logger.info("Todo item '{}' span class contains 'done-true': {}", todoText, isChecked);
            return isChecked;
        } catch (Exception e) {
            logger.error("Failed to verify if todo item '{}' is checked", todoText, e);
            return false;
        }
    }
    }
