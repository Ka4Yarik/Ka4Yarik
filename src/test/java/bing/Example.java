package bing;



import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.*;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Example {
    private WebDriver driver;
    private String baseUrl;
    BufferedReader br = null;
    int min;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeMethod
    public void setUp() {

       System.setProperty("webdriver.firefox.marionette", "data/geckodriver.exe");
        System.setProperty("webdriver.chrome.driver","data/chromedriver.exe");
        System.setProperty("webdriver.ie.driver","data/IEDriverServer.exe");
        //Запустить FirefoxDriver
       //driver = new FirefoxDriver();
        //Запустить ChromeDriver
         driver = new ChromeDriver();
        //Запустить IEeDriver
        //  driver = new InternetExplorerDriver();
        baseUrl = "http://www.bing.com/";
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public String getWordFromFile() {
        String currentLine = null;
        try {
            br = new BufferedReader(new FileReader("data/word2.txt"));
            while ((currentLine = br.readLine()) != null) {
                return currentLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    @DataProvider(name = "getword")
    public Object[][] data() {
        return new Object[][] {{getWordFromFile()}};
    }

    public void runDriver(String currentLine) throws InterruptedException {
        System.out.println("Переходим на главную страницу поисковой системы Bing\n");
        driver.get(baseUrl + "/");
        driver.findElement(By.id("sb_form_q")).click();
        driver.findElement(By.id("sb_form_q")).clear();
        System.out.println("Выполняем поиск по ключевой фразе, полученной из файла\n");
        driver.findElement(By.id("sb_form_q")).sendKeys(""+currentLine);
        log("Вводим полученую из текста фразу");
        driver.findElement(By.id("sb_form_go")).click();
        log("Проверяем присутствие блока “Похожие поисковые запросы”\n");
        assertEquals("Похожие поисковые запросы", driver.findElement(By.xpath("//ol[@id='b_context']/li/h2")).getText());
        System.out.println("Блок “Похожие поисковые запросы” найден\n");
        log("Выполняем переход по случайной ссылке из этого блока.\n");
        int n = new Random().nextInt(8)+ 1;
        String Expectedtext = driver.findElement(By.xpath("//ol[@id='b_context']/li/ul/li[" + n + "]/a")).getText();
        driver.findElement(By.xpath("//ol[@id='b_context']/li/ul/li[" + n + "]/a")).click();
        driver.findElement(By.id("sb_form_q")).click();
        String Actualtext = driver.findElement(By.id("sb_form_q")).getAttribute("value");
        log("проверить, что в поисковой строке содержится текст запроса, который значился в ссылке с блока “Похожие поисковые запросы”, по которой мы перешли.\n");
        assertEquals(Expectedtext, Actualtext);
        System.out.println("Тексты совпадают\n");
        Thread.sleep(3000);
         for (int i = 1; i<10; i++) {
             log("проверить что URL соответствует адресу, который отображается на странице результатов.\n");
             driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            String GetURL1 = driver.findElement(By.xpath("//ol[@id='b_results']/li["+i+"]/div[2]/div")).getText();
       //      driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//li["+i+"]/div/h2/a")).click();
            String currentURL = driver.getCurrentUrl();
                 try {
             assertTrue(currentURL.contains(GetURL1));
             } catch (Error e) {
                 verificationErrors.append(e.toString());}
             driver.navigate().back();
             log("Если результат поиска имеет ссылку на сохраненную копию, также перейти на страницу с кэшированной версией страницы. \n");
            if(driver.findElements(By.xpath("//ol[@id='b_results']/li["+i+"]/div[2]/div/a/span/span")).size() != 0){
                driver.findElement(By.xpath("//ol[@id='b_results']/li["+i+"]/div[2]/div/a/span/span")).click();
                driver.findElement(By.xpath("//ol[@id='b_results']/li["+i+"]/div[2]/div/div/div/a")).click();
                log("Проверить что отображается кэшированная копия той страницы, адрес которой отображается на странице результатов.\n");
                String CashCopy = driver.findElement(By.xpath("//a")).getText();
                try {
                assertTrue(CashCopy.contains(GetURL1));
                } catch (Error e) {
                    verificationErrors.append(e.toString());}
                System.out.println("Оотображается кэшированная копия " +i+ "-й страницы адрес которой отображается на странице результатов.");
                driver.navigate().back();
             }else{
                 System.out.println("Кешированная страница не найдена");
             }

      }
        }


    @Test(dataProvider = "getword")
    public void UseText (String line) throws Exception {
        runDriver(line);
           }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
    private void log (String message){
        Reporter.log(message + "<br>");
    }
}

