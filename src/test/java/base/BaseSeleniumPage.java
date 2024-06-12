package base;

import lombok.Setter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

abstract public class BaseSeleniumPage {
    @Setter
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(BaseSeleniumPage.class);

    public BaseSeleniumPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    protected void moveToElement(WebElement element) {
        logger.info("Scrolling to element: {}", element);
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
    }

    protected void logAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        logger.info("Clicked on element: {}", element);
    }

    public void switchToNewWindow() {
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    public void scrollDown(int pixels) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0," + pixels + ")");
    }

}
