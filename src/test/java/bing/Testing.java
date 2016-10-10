package bing;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import org.testng.annotations.AfterClass;


public class Testing {
    private WebDriver driver;
    private String baseUrl;


    @BeforeMethod
    public void setUp() throws Exception {
        System.setProperty("webdriver.firefox.marionette", "data/geckodriver.exe");
        driver = new FirefoxDriver();
        baseUrl = "http://www.bing.com/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testBing() throws Exception {
        //   System.out.println("Переходим на главную страницу поисковой системы Bing\n");
        driver.get(baseUrl + "/");
        driver.findElement(By.id("sb_form_q")).click();
        driver.findElement(By.id("sb_form_q")).clear();
        driver.findElement(By.id("sb_form_q")).sendKeys("automation");
        driver.findElement(By.id("sb_form_go")).click();
        System.out.println("Page title is: " + driver.getTitle());

        By mySelector = By.id("b_results");
        List<WebElement> myElements = driver.findElements(mySelector);
        for(WebElement e : myElements) {
            System.out.println(e.getText());
        }

    }
    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}

