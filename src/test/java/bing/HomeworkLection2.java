package bing;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;
import org.testng.annotations.AfterClass;

import static org.junit.Assert.assertTrue;


public class HomeworkLection2 {
    private WebDriver driver;
    private String baseUrl;


    @BeforeMethod
    public void setUp() throws Exception {
        System.setProperty("webdriver.firefox.marionette", "data/geckodriver.exe");
        driver = new FirefoxDriver();
        baseUrl = "http://www.bing.com/";
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testBing() throws Exception {
         System.out.println("Переходим на главную страницу поисковой системы Bing\n");
        driver.get(baseUrl + "/");
        driver.findElement(By.id("sb_form_q")).click();
        driver.findElement(By.id("sb_form_q")).clear();
        driver.findElement(By.id("sb_form_q")).sendKeys("automation");
        driver.findElement(By.id("sb_form_go")).click();
        System.out.println("Page title is: " + driver.getTitle());
        System.out.println("---------Список заголовков-----------" );
        for (int i = 1; i<=10; i++) {
            String GetURL = driver.findElement(By.xpath("//ol[@id='b_results']/li["+i+"]/div/h2/a")).getText();
            System.out.println(GetURL );
            }

    }
    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}

