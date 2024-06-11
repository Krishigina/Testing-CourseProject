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

public class LamdaPage extends BaseSeleniumPage {
    private static final Logger logger = LoggerFactory.getLogger(LamdaPage.class);

    // Locators for page elements
    @FindBy(xpath = "//h2")
    private WebElement header;

    @FindBy(xpath = "//span[@class='ng-binding'][contains(text(), '5 of 5 remaining')]")
    private WebElement remainingText;

    @FindBy(xpath = "//li[1]//input[@type='checkbox']")
    private WebElement firstCheckbox;

    @FindBy(id = "sampletodotext")
    private WebElement newTodoInput;

    @FindBy(id = "addbutton")
    private WebElement addButton;

    public LamdaPage() {
        driver.get("https://lambdatest.github.io/sample-todo-app/");
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
        try {
            WebElement firstItemSpan = waitForElement(By.xpath("//li[1]//span"));
            String spanClass = firstItemSpan.getAttribute("class");
            boolean isNotChecked = spanClass.contains("done-false");
            logger.info("First item span class: {}", spanClass);
            logger.info("First item span class contains 'done-false': {}", isNotChecked);
            return isNotChecked;
        } catch (Exception e) {
            logger.error("Failed to verify if the first item is not checked", e);
            return false;
        }
    }

    public void checkFirstItem() {
        logAndClick(firstCheckbox);
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
}
