package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class ProductActionFunctionalTest {

    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}.
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest(ChromeDriver driver) {
        baseUrl = String.format("%s:%d/product", testBaseUrl, serverPort);
        driver.get(baseUrl + "/create");
        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        nameInput.clear();
        quantityInput.clear();
        String productName = "pecel";
        String productQuantity = "2";
        nameInput.sendKeys(productName);
        quantityInput.sendKeys(productQuantity);

        WebElement submitButton = driver.findElement(By.tagName("button"));
        submitButton.click();
    }

    @Test
    void editProductTest(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/list");
        WebElement editButton = driver.findElement(By.id("edit_button_0"));
        editButton.click();

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        nameInput.clear();
        quantityInput.clear();
        String productName = "ayam";
        String productQuantity = "3";
        nameInput.sendKeys(productName);
        quantityInput.sendKeys(productQuantity);

        WebElement submitButton = driver.findElement(By.tagName("button"));
        submitButton.click();

        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        List<WebElement> columns = rows.getFirst().findElements(By.tagName("td"));
        String nameInList = columns.getFirst().getText();
        String quantityInList = columns.get(1).getText();

        assertEquals(productName, nameInList);
        assertEquals(productQuantity, quantityInList);
    }

    @Test
    void editProductNegativeTest(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/list");
        WebElement editButton = driver.findElement(By.id("edit_button_0"));
        editButton.click();

        WebElement nameInput = driver.findElement(By.id("nameInput"));
        WebElement quantityInput = driver.findElement(By.id("quantityInput"));
        nameInput.clear();
        quantityInput.clear();
        String productName = "ayam";
        String productQuantity = "-3";
        nameInput.sendKeys(productName);
        quantityInput.sendKeys(productQuantity);

        WebElement submitButton = driver.findElement(By.tagName("button"));
        submitButton.click();

        String title = driver.getTitle(); //still in edit page
        assertEquals("Edit Product", title);
    }

    @Test
    void deleteProductTest(ChromeDriver driver) throws Exception {
        driver.get(baseUrl + "/list");
        WebElement editButton = driver.findElement(By.id("delete_button_0"));
        editButton.click();

        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        int numberOfProducts = rows.size();

        assertEquals(0, numberOfProducts);
    }
}