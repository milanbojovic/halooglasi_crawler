package com.halooglasi;

import java.util.concurrent.TimeUnit;

import com.halooglasi.util.EmailClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.halooglasi.util.LivingAreaSearch;
import com.halooglasi.util.Smtp2GoEmailClient;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

public class HaloOglasiCrawler 
{
    public static void main( String[] args )
    {
    	WebDriver driver = null;
    	WebDriverManager.chromedriver().setup();

    	try {
			ChromeOptions chromeOptions= new ChromeOptions();
			chromeOptions.setBinary("/usr/bin/google-chrome-unstable");
			chromeOptions.addArguments("--headless");
			driver = new ChromeDriver(chromeOptions);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			String query = "https://www.halooglasi.com/nekretnine/izdavanje-stanova?grad_id_l-lokacija_id_l-mikrolokacija_id_l=40761%2C40784%2C40788%2C531368&cena_d_to=160&cena_d_unit=4&nacin_placanja_id_l=387273";

			LivingAreaSearch livingAreaSearch = new LivingAreaSearch(driver, query);
			EmailClient emaiClient = new EmailClient(	"USER", "PASS");

	        livingAreaSearch.executeQueryAndHandleResults(emaiClient);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("");
		} finally {
	        if (driver != null) {
	            driver.quit();
	        }
		}
    }
}
