package task4.pages;

import base.BaseSeleniumPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BigGeekPage extends BaseSeleniumPage {
    private static final Logger logger = LoggerFactory.getLogger(BigGeekPage.class);

    // Locators for page elements
    @FindBy(xpath = "//input[@id='search-header-middle']")
    private WebElement searchField;

    @FindBy(xpath = "//button[@class='search-header-middle__submit']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[@class='digi-product digi-has-not-color']")
    private List<WebElement> productItems;

    @FindBy(xpath = "//input[@inputmode='decimal' and @name='min']")
    private WebElement minPriceField;

    @FindBy(xpath = "//input[@inputmode='decimal' and @name='max']")
    private WebElement maxPriceField;

    @FindBy(xpath = "//a[@class='digi-product-addCard catalog-card__cart cart-modal-open']")
    private WebElement addToCartButton;

    @FindBy(xpath = "//span[contains(@class,'digi-product-price-variant_actual')]")
    private List<WebElement> priceList;

    @FindBy(xpath = ".//a[@class='digi-product__label']")
    private WebElement productNameElement;

    @FindBy(xpath = "//a[@class='button button--medium cart-modal__cart-link' and @href='/cart']")
    private WebElement goToCartButton;

    @FindBy(xpath = "//h2[@class='product-cart__title']")
    private WebElement cartProductName;

    public BigGeekPage() {
        driver.get("https://biggeek.ru/");
        PageFactory.initElements(driver, this);
    }

    public void openSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchField)).click();
        logger.info("Opened search field.");
    }

    public void enterSearchQuery(String searchQuery) {
        wait.until(ExpectedConditions.visibilityOf(searchField));
        searchField.clear();
        searchField.sendKeys(searchQuery);
        logger.info("Entered searchQuery: {}", searchQuery);
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        logger.info("Clicked search button.");
    }

    public boolean isProductListDisplayed(String searchQuery) {
        wait.until(ExpectedConditions.visibilityOfAllElements(productItems));
        for (WebElement item : productItems) {
            if (item.getText().contains(searchQuery)) {
                return true;
            }
        }
        return false;
    }

    public void setPriceFilter(String minPrice, String maxPrice) throws InterruptedException {
        minPriceField.sendKeys(minPrice);
        Thread.sleep(4000);
        maxPriceField.sendKeys(maxPrice);
        logger.info("Set price filter: {} - {}", minPrice, maxPrice);
    }

    public boolean areFilteredProductsDisplayed(int minPrice, int maxPrice, int nItems) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartButton);
        try {
            int count = 0;
            for (WebElement priceElement : priceList) {
                if (count >= nItems) {
                    break;
                }

                // Use a relative XPath to find the product name within the current priceElement context
                WebElement productContainer = priceElement.findElement(By.xpath("./ancestor::div[@class='digi-product digi-has-not-color']"));
                WebElement productNameElement = productContainer.findElement(By.xpath(".//a[@class='digi-product__label']"));
                String productName = productNameElement.getText();

                String priceText = priceElement.getText().replaceAll("\\s+", "").replace("₽", "");
                if (priceText.isEmpty()) {
                    logger.warn("Price text is empty for item: {}", productName);
                    continue;
                }
                try {
                    int price = Integer.parseInt(priceText);
                    logger.info("Product: {}, Parsed price: {}", productName, price);
                    if (price < minPrice || price > maxPrice) {
                        logger.error("Product: {}, Price {} is out of the expected range ({} - {})", productName, price, minPrice, maxPrice);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    logger.error("Product: {}, Failed to parse price text '{}' as integer", productName, priceText, e);
                    return false;
                }
                count++;
            }
            return true;
        } catch (Exception e) {
            logger.error("Unexpected error during price verification", e);
            return false;
        }
    }


    public String addToCart() {
        // Get the first product in the list
        WebElement firstProduct = productItems.get(0);
        // Get the product name
        WebElement productNameElement = firstProduct.findElement(By.xpath(".//a[@class='digi-product__label']"));
        String productName = productNameElement.getText();
        // Scroll to the "Add to Cart" button
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartButton);
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        logger.info("Added product to cart: {}", productName);
        // Click the "Go to Cart" button
        wait.until(ExpectedConditions.visibilityOf(goToCartButton)).click();
        logger.info("Navigated to cart page.");
        // Return the product name for later verification
        return productName;
    }

    public boolean verifyProductInCart(String expectedProductName) {
        wait.until(ExpectedConditions.visibilityOf(cartProductName));
        String actualProductName = cartProductName.getText();
        logger.info("Expected product: {}, Actual product: {}", expectedProductName, actualProductName);
        return actualProductName.contains(expectedProductName);
    }

}
