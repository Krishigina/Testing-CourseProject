package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

abstract public class BaseSeleniumTest {

    public static WebDriver driver;
    protected static final Logger logger = LoggerFactory.getLogger(BaseSeleniumTest.class);

    @BeforeEach
    public void setUp(){
        try {
            logger.info("Setting up the driver");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            logger.info("Driver setup completed");
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            BaseSeleniumPage.setDriver(driver);
            driver = new EventFiringDecorator(new AllureEventListener()).decorate(driver);
        } catch (Exception e) {
            logger.error("Error during driver setup", e);
            throw new RuntimeException(e);
        }
    }




//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            try {
//                logger.info("Tearing down the driver");
//                driver.close();
//                driver.quit();
//                logger.info("Driver quit successfully");
//            } catch (Exception e) {
//                logger.error("Error during driver teardown", e);
//            }
//        }
//    }


}
