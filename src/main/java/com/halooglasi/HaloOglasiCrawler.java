package com.halooglasi;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.halooglasi.util.LivingAreaSearch;
import com.halooglasi.util.Smtp2GoEmailClient;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Hello world!
 *
 */
public class HaloOglasiCrawler 
{
    public static void main( String[] args )
    {
    	WebDriver driver = null;
    	WebDriverManager.chromedriver().setup();
    	
    	try {
			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			String query = "https://www.halooglasi.com/nekretnine/izdavanje-stanova?grad_id_l-lokacija_id_l-mikrolokacija_id_l=40574%2C40787&cena_d_to=400&cena_d_unit=4&broj_soba_order_i_from=3&oglasivac_nekretnine_id_l=387237&sa_fotografijom=true";
			
			LivingAreaSearch livingAreaSearch = new LivingAreaSearch(driver, query);
			Smtp2GoEmailClient emaiClient = new Smtp2GoEmailClient(	"milan.bojovic@braintribe.com", "9okUD5F1oKSL");
			
	        livingAreaSearch.executeQueryAndHandleResults(emaiClient);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	        if (driver != null) {
	            driver.quit();
	        }
		}
    }
}
