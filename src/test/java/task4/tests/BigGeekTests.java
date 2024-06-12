package task4.tests;

import base.BaseSeleniumTest;
import base.TestListener;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task4.pages.BigGeekPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestListener.class)
@Feature("BigGeek Tests")
public class BigGeekTests extends BaseSeleniumTest {

    private BigGeekPage bigGeekPage;
    private static final Logger logger = LoggerFactory.getLogger(BigGeekTests.class);

    @BeforeEach
    public void setUpEach() {
        Allure.step("Open BigGeek page and initialize page", () -> {
            bigGeekPage = new BigGeekPage();
        });
    }

    @Test
    @DisplayName("Test for Search site BigGeek")
    @Story("Complete workflow for Search site BigGeek")
    @Description("This test performs a series of checks and actions on the search functions site BigGeek")
    public void searchTest() {
        String searchQuerry = "Смартфон Apple iPhone 14 128 ГБ (Синий | Blue)";
        logger.info("Starting test for BigGeek search functionality");

        Allure.step("Open search field", bigGeekPage::openSearch);

        Allure.step("Enter search query in search field and click search button", () -> {
            bigGeekPage.enterSearchQuery(searchQuerry);
            bigGeekPage.clickSearch();
        });

        Allure.step("Verify the search results", () -> {
            boolean isProductListDisplayed = bigGeekPage.isProductListDisplayed(searchQuerry);
            logger.info("Starting verify the search results");
            assertTrue(isProductListDisplayed, "The product list does not display items matching the search query.");
        });
    }

    @Test
    @DisplayName("Test for filtering products by price site BigGeek")
    @Story("Filter products by price")
    @Description("This test verifies the filtering functionality of products by price on BigGeek")
    public void priceFilterTest() {
        String searchQuery = "смартфон";
        String minPrice = "10000";
        String maxPrice = "15000";

        logger.info("Starting test for BigGeek price filter functionality");

        Allure.step("Open search field", bigGeekPage::openSearch);

        Allure.step("Enter search query in search field and click search button", () -> {
            bigGeekPage.enterSearchQuery(searchQuery);
            bigGeekPage.clickSearch();
        });

        Allure.step("Set price filter and apply", () -> {
            bigGeekPage.setPriceFilter(minPrice, maxPrice);
            Thread.sleep(5000);
        });


        Allure.step("Verify the 5 filtered products are within the specified price range", () -> {
            boolean areFilteredProductsDisplayed = bigGeekPage.areFilteredProductsDisplayed(Integer.parseInt(minPrice), Integer.parseInt(maxPrice), 5);
            assertTrue(areFilteredProductsDisplayed, "The filtered product list does not display items within the specified price range.");
        });
    }

    @Test
    @DisplayName("Test for add to cart function site BigGeek")
    @Story("Complete workflow for add to cart function site BigGeek")
    @Description("This test performs a series of checks and actions on the search function site BigGeek")

    public void addToCartTest() {
        String searchQuery = "телевизор";
        logger.info("Starting test for adding product to cart functionality");

        Allure.step("Open search field", bigGeekPage::openSearch);

        Allure.step("Enter search query in search field and click search button", () -> {
            bigGeekPage.enterSearchQuery(searchQuery);
            bigGeekPage.clickSearch();
            Thread.sleep(3000);
        });

        String selectedProductName = Allure.step("Add the first product to the cart", () -> {
            return bigGeekPage.addToCart();
        });

        Allure.step("Verify the product in the cart", () -> {
            boolean isProductInCart = bigGeekPage.verifyProductInCart(selectedProductName);
            assertTrue(isProductInCart, "The product in the cart does not match the selected product.");
        });
    }
}
