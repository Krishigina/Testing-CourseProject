package task3.pages;

import base.BaseSeleniumPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class YandexMarketPage extends BaseSeleniumPage {
    private static final Logger logger = LoggerFactory.getLogger(YandexMarketPage.class);

    // Locators for page elements
    @FindBy(xpath = "//button[.//span[text()='Каталог']]")
    private WebElement catalogButton;

    @FindBy(xpath = "//a[@class='_3yHCR' and .//span[text()='Ноутбуки и компьютеры']]")
    private WebElement laptopsAndComputersCategory;

    @FindBy(xpath = "//li//a[@class='_2re3U ltlqD _2TBT0' and text()='Ноутбуки']")
    private WebElement laptopsCategory;

    @FindBy(xpath = "//div[@data-apiary-widget-name='@light/Organic']")
    private List<WebElement> laptopItems;

    public YandexMarketPage() {
        driver.get("https://market.yandex.ru/");
        PageFactory.initElements(driver, this);
    }

    public void openCatalog() {
        wait.until(ExpectedConditions.elementToBeClickable(catalogButton)).click();
        logger.info("Opened catalog menu.");
    }

    public void selectLaptopsCategory() {
        wait.until(ExpectedConditions.visibilityOf(laptopsAndComputersCategory));
        moveToElement(laptopsAndComputersCategory); // Hover over the laptops and computers category
        moveToElement(laptopsCategory);
        wait.until(ExpectedConditions.visibilityOf(laptopsCategory));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", laptopsCategory);
        wait.until(ExpectedConditions.elementToBeClickable(laptopsCategory));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", laptopsCategory);

        logger.info("Selected laptops category.");
    }

    public void logFirstFiveLaptops() {
        wait.until(ExpectedConditions.visibilityOfAllElements(laptopItems));
        for (int i = 0; i < 5 && i < laptopItems.size(); i++) {
            WebElement item = laptopItems.get(i);
            String name = item.findElement(By.xpath(".//h3[@data-auto='snippet-title']")).getText();
            String price = item.findElement(By.xpath(".//span[@data-auto='snippet-price-current']/span[1]")).getText();
            logger.info("Laptop {}: Name = {}, Price = {}", i + 1, name, price);
        }
    }

    public WebElement getSecondLaptop() {
        wait.until(ExpectedConditions.visibilityOfAllElements(laptopItems));
        return laptopItems.get(1);
    }

    public String getLaptopName(WebElement laptop) {
        return laptop.findElement(By.xpath(".//h3[@data-auto='snippet-title']")).getText();
    }

    public String getLaptopPrice(WebElement laptop) {
        return laptop.findElement(By.xpath(".//span[@data-auto='snippet-price-current']/span[1]")).getText();
    }

    public void addSecondLaptopToCart() {
        WebElement secondLaptop = getSecondLaptop();
        WebElement addToCartButton = secondLaptop.findElement(By.xpath(".//button[@aria-label='В корзину' and @data-auto='cartButton']"));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        logger.info("Added second laptop to cart.");
    }

    public void openCart() {
        WebElement cartButton = driver.findElement(By.xpath("//a[contains(@href, '/cart')]"));
        wait.until(ExpectedConditions.elementToBeClickable(cartButton)).click();
        logger.info("Opened cart.");
    }

    public boolean isLaptopInCart(String laptopName) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_3gglc']//article[@data-auto='availableCartItem']")));
                WebElement cartItem = driver.findElement(By.xpath("//div[@class='_3gglc']//article[@data-auto='availableCartItem']"));
                String cartItemName = cartItem.findElement(By.xpath(".//div[@data-baobab-name='title']")).getText();
                logger.info("Cart Item: Name = {}", cartItemName);
                return cartItemName.contains(laptopName);
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                attempts++;
                logger.error("Attempt {} failed with exception: {}", attempts, e.getMessage());
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//article[@data-auto='availableCartItem' and @aria-label='Товар']")));
            }
        }
        return false;
    }

    public void increaseQuantityInCart() {
        int attempts = 0;
        while (attempts < 2) {
            try {
                WebElement increaseButton = driver.findElement(By.xpath("//button[@aria-label='Увеличить' and @data-auto='offerAmountPlus']"));
                wait.until(ExpectedConditions.elementToBeClickable(increaseButton)).click();
                logger.info("Increased quantity in cart.");
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                logger.warn("Attempt {} failed due to stale element reference. Retrying...", attempts);
            }
        }
        logger.error("Failed to increase quantity in cart after multiple attempts due to stale element reference.");
    }

    public String getTotalPrice() {
        WebElement totalPriceElement = driver.findElement(By.xpath("//span[@data-auto='total-price']"));
        return totalPriceElement.getText();
    }

    public void removeItemFromCart() {
        WebElement removeButton = driver.findElement(By.xpath("//button[@data-auto='remove-button' and @aria-label='Удалить']"));
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        logger.info("Removed item from cart.");
    }
}
