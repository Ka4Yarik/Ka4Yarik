package bing;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Reporter;
import org.testng.annotations.*;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class HomeworkLection3 {
    private WebDriver driver;
    private String baseUrl;
    BufferedReader br = null;
    int min;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeMethod
    public void setUp() throws Exception {

        System.setProperty("webdriver.firefox.marionette", "data/geckodriver.exe");
        driver = new FirefoxDriver();
        baseUrl = "http://www.bing.com/";
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

   public String getWordFromFile() {
        String currentLine = null;
        try {
            br = new BufferedReader(new FileReader("data/temp.txt"));
            while ((currentLine = br.readLine()) != null) {
                System.out.println("Берем фразу из текстового документа\n");
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

    @DataProvider(name = "searchword")
    public Object[][] data() {
        return new Object[][] {{getWordFromFile()}};
    }

    public void runDriver(String currentLine) {
        System.out.println("Переходим на главную страницу поисковой системы Bing\n");
        driver.get(baseUrl + "/");
        driver.findElement(By.id("sb_form_q")).click();
        driver.findElement(By.id("sb_form_q")).clear();
        driver.findElement(By.id("sb_form_q")).sendKeys(""+currentLine);
        System.out.println("Вводим полученую из текста фразу\n");
        log("Вводим полученую из текста фразу");
        driver.findElement(By.id("sb_form_go")).click();
        log("Проверяем, содержит ли заголовок искомую фразу");
        assertTrue(driver.getTitle().contains(currentLine));
        System.out.println("Заголовок страницы содержит искомую фразу\n");
        log("Проверяем, что каждый отображаемый на странице\n" +
                "результат содержит в заголовке искомую фразу");
        for (int i = 1; i<=10; i++) {
            String GetURL = driver.findElement(By.xpath("//ol[@id='b_results']/li["+i+"]/div/h2/a")).getText();
            try {
                assertTrue(GetURL.contains(currentLine));
            } catch (Error e) {
                System.out.println("Результат №" + i + " содержит в заголовке искомую фразу");
            }
        }
    }
    @Parameters(value="min")
    public void getResult(String currentLine) {
        WebElement valueResultsOfSearch = driver.findElement(By.cssSelector("span.sb_count"));
        log("Находим количество найденых результатов");
        String results = valueResultsOfSearch.getText();
        log("Отделяем числовую часть количества найденых результатов");
        String numberOnly = results.replaceAll("[^0-9]", "");
        int number = Integer.parseInt(numberOnly);
        System.out.println("Количество результатов = "+number);

        log("Проверяем на наличие минимально необходимых результатов");
        if (number < min) {
            System.out.println("Количество результатов меньше минимально необходимого ");
        } else
            System.out.println("Результатов достаточно");

    }

 @Test(dataProvider = "searchword")
 public void UseText (String line) throws Exception {
     runDriver(line);
     getResult(line);
 }

    @AfterClass
    public void tearDown() {
        driver.quit();
        }
    private void log (String message){
        Reporter.log(message + "<br>");
    }
}

