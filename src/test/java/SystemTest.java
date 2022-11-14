import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SystemTest {
    WebDriver webDriver;

    @BeforeAll
    public void driverSetup() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver_win32/chromedriver.exe");
    }

    @BeforeEach
    public void setup() {
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
    }

    @AfterEach
    public void closeDriver() {
        webDriver.close();
    }

    /* Tries to directly open Steam Store main page and checks if it's open by
    * matching URLs */
    @Test
    public void expectsToOpenSteamStoreWebsiteTest() {
        webDriver.get("https://store.steampowered.com/");
        Assertions.assertEquals("https://store.steampowered.com/", webDriver.getCurrentUrl());
    }

    /* Tries to directly open my Steam Profile page and checks if it's open by
    * matching page titles. */
    @Test
    public void expectsToBeInGigaSteamProfilePageTest() {
        webDriver.get("https://steamcommunity.com/id/gigacs");
        Assertions.assertEquals("Steam Community :: giga", webDriver.getTitle());
    }

    /* From Steam Store main page, searches for the searchbar element, sends
    * "Stardew Valley" as the keywords to be searched and enter the search.
    * Then finally checks if the actual page URL contains the targeted keywords. */
    @Test
    public void expectsToSearchStardewValleyOnSteamStoreTest() {
        webDriver.get("https://store.steampowered.com/");
        webDriver.findElement(By.xpath("//*[@id=\"store_nav_search_term\"]")).sendKeys("Stardew Valley", Keys.ENTER);
        Assertions.assertTrue(webDriver.getCurrentUrl().contains("/search/?term=Stardew+Valley"), webDriver.getCurrentUrl());
    }

    /* From the search page inside Steam Store after searching for Stardew Valley,
    * goes step by step until opening the target page and finally checks if it's open by
    * matching URLs. */
    @Test
    public void expectsToOpenStardewValleyStorePageFromSearchTest() throws InterruptedException {
        webDriver.get("https://store.steampowered.com/search/?term=Stardew+Valley");
        webDriver.findElement(By.xpath("//*[@id=\"search_resultsRows\"]/a[1]")).click();

        /* Steam prompted a page with age check before opening target game store page */
        if (webDriver.getCurrentUrl().equals("https://store.steampowered.com/agecheck/app/413150/")) {

            /* Select day of birth */
            webDriver.findElement(By.xpath("//*[@id=\"ageDay\"]")).click();
            webDriver.findElement(By.xpath("//*[@id=\"ageDay\"]/option[13]")).click();

            /* Select month of birth */
            webDriver.findElement(By.xpath("//*[@id=\"ageMonth\"]")).click();
            webDriver.findElement(By.xpath("//*[@id=\"ageMonth\"]/option[7]")).click();

            /* Select year of birth */
            webDriver.findElement(By.xpath("//*[@id=\"ageYear\"]")).click();
            webDriver.findElement(By.xpath("//*[@id=\"ageYear\"]/option[102]")).click();

            /* Click on the "access page" button to open the target page */
            webDriver.findElement(By.xpath("//*[@id=\"view_product_page_btn\"]")).click();
        }

        Thread.sleep(2000); // Waiting for target page to load
        Assertions.assertEquals("https://store.steampowered.com/app/413150/Stardew_Valley/", webDriver.getCurrentUrl());
    }
}
