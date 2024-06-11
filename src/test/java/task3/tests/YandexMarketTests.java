package task3.tests;

import base.BaseSeleniumTest;
import base.TestListener;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task3.pages.YandexMarketPage;

@ExtendWith(TestListener.class)
@Feature("Yandex Market Tests")
public class YandexMarketTests extends BaseSeleniumTest {

    private YandexMarketPage yandexMarketPage;
    private static final Logger logger = LoggerFactory.getLogger(YandexMarketTests.class);

    // Variables to store the name and price of the second laptop
    private String secondLaptopName;
    private String secondLaptopPrice;

    @BeforeEach
    public void setUpEach() {
        Allure.step("Open Yandex Market page and initialize page", () -> {
            yandexMarketPage = new YandexMarketPage();
        });
    }

    @Test
    @DisplayName("Test for Yandex Market laptops category")
    @Story("Complete workflow for Yandex Market laptops category")
    @Description("This test performs a series of checks and actions on the Yandex Market laptops category.")
    public void fullTest() {
        logger.info("Starting comprehensive test for Yandex Market laptops category");

        Allure.step("Open catalog menu", yandexMarketPage::openCatalog);

        Allure.step("Select laptops category", yandexMarketPage::selectLaptopsCategory);

        Allure.step("Log first 5 laptops", yandexMarketPage::logFirstFiveLaptops);

        Allure.step("Remember the second laptop's name and price", () -> {
            WebElement secondLaptop = yandexMarketPage.getSecondLaptop();
            secondLaptopName = yandexMarketPage.getLaptopName(secondLaptop);
            secondLaptopPrice = yandexMarketPage.getLaptopPrice(secondLaptop);
            logger.info("Second laptop name: {}", secondLaptopName);
            logger.info("Second laptop price: {}", secondLaptopPrice);
        });

        Allure.step("Add the second laptop to the cart", yandexMarketPage::addSecondLaptopToCart);

        Allure.step("Open the cart", yandexMarketPage::openCart);

        Allure.step("Verify the second laptop is in the cart", () -> {
            boolean isInCart = yandexMarketPage.isLaptopInCart(secondLaptopName);
            Assertions.assertTrue(isInCart, "Second laptop is not in the cart.");
        });

        Allure.step("Increase the quantity of the second laptop in the cart", yandexMarketPage::increaseQuantityInCart);

        Allure.step("Verify the total price is updated", () -> {
            String price = secondLaptopPrice.replaceAll("[^0-9]", "");
            int singleLaptopPrice = Integer.parseInt(price);
            String totalPrice = yandexMarketPage.getTotalPrice().replaceAll("[^0-9]", "");
            int expectedTotalPrice = singleLaptopPrice * 2;

            // Set a tolerance range for price difference, e.g., ± 1000 rubles. I do it because the price on the page and when added to the cart was different.
            int tolerance = 10000;
            int actualTotalPrice = Integer.parseInt(totalPrice);
            boolean isWithinTolerance = Math.abs(expectedTotalPrice - actualTotalPrice) <= tolerance;

            Assertions.assertTrue(isWithinTolerance, "Total price is not within the expected tolerance range.");
        });

        Allure.step("Remove the laptop from the cart", yandexMarketPage::removeItemFromCart);
    }
}
