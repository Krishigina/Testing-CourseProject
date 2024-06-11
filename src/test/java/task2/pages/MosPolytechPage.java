package task2.pages;

import base.BaseSeleniumPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class MosPolytechPage extends BaseSeleniumPage {

    private static final Logger logger = LoggerFactory.getLogger(MosPolytechPage.class);
    private WebDriverWait wait;

    @FindBy(xpath = "//a[@title='Расписание']")
    private WebElement scheduleButton;

    @FindBy(xpath = "//a[@href='https://rasp.dmami.ru/']")
    private WebElement viewOnSiteLink;

    public MosPolytechPage() {
        driver.get("https://mospolytech.ru/");
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openSchedule() {
        logger.info("Clicking on the 'Расписание' button");
        wait.until(ExpectedConditions.elementToBeClickable(scheduleButton)).click();
    }

    public void clickViewOnSiteLink() {
        logger.info("Scrolling to and clicking on the 'Смотрите на сайте' link");
        moveToElement(viewOnSiteLink);
        logAndClick(viewOnSiteLink);
    }
}
