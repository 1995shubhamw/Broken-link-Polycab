package package1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrokenLinks 
{
    private static WebDriverWait wait;

    public static void main(String[] args) 
    {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://polycab.com/");
        
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(50));

        try 
        {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='rejectBtn']"))).click();
        } 
        catch (Exception e) 
        {
            System.out.println("Cookie rejection button not found or couldn't be clicked.");
        }

        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("a")));

        List<WebElement> links = driver.findElements(By.tagName("a"));

        for (WebElement link : links) 
        {
            String url = link.getAttribute("href");

            // Check if the link is valid
            if (url != null && !url.isEmpty() && !url.startsWith("javascript") && !url.equals("#") 
            	    && !url.startsWith("tel:") && !url.startsWith("mailto:")) 
            
            {
                try 
                {
                    
                    URL linkUrl = new URL(url);
                    // Open a connection to the URL
                    HttpURLConnection httpURLConnection = (HttpURLConnection) linkUrl.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.connect();

                    // Get the response code
                    int responseCode = httpURLConnection.getResponseCode();

                    
                    if (responseCode >=400) 
                    {
                        System.out.println("Broken Link: " + url + " (Response Code: " + responseCode + ")");
                    }
                    else 
                    {
                      //System.out.println("Valid Link: " + url + " (Response Code: " + responseCode + ")");
                    }
                } 
                
                catch (Exception e) 
                
                {
                    System.out.println("Error checking link: " + url + " (" + e.getMessage() + ")");
                }
            }
        }

        
        driver.quit();
    }
}
