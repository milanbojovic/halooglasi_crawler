package com.halooglasi.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class LivingAreaSearch {

    private WebDriver driver;
    private String url = "https://www.halooglasi.com/nekretnine";
    private List<Advertisement> allAddvertisements = new ArrayList<Advertisement>();
    private List<Advertisement> newAddvertisements = new ArrayList<Advertisement>();
    private MongoClient mongoClient;
    private DB database;
    private DBCollection collection;
    private int pageCnt = 0;
    
    
    public LivingAreaSearch(WebDriver driver, String request) {
        this.driver = driver;
        driver.manage().window().maximize();
        driver.get(request);
        
        try {
            mongoClient = new MongoClient();
            database = mongoClient.getDB("local");
        	collection = database.getCollection("advertisements");
        } catch(UnknownHostException e){
        	
        }
    }

    public void executeQueryAndHandleResults(EmailClient emailClient) throws InterruptedException {
    	WebElement nextPage = null;

		Thread.sleep(30000);
		WebElement cookieAcceptBtn = driver.findElement(By.className("cookie-policy-btn"));
		cookieAcceptBtn.click();

		do {
    		WebElement footerElement = driver.findElement(By.id("pager-1"));
    		if (footerElement.findElements(By.linkText("Sledeća »")).size() > 0) {
    			nextPage = footerElement.findElement(By.linkText("Sledeća »"));
				Thread.sleep(1000);
    			traversePage();
    			
    			//Scroll to bottom
    			JavascriptExecutor jse = (JavascriptExecutor)driver;
        		jse.executeScript("scroll(0, 5000);");
    			
        		nextPage.click();
    		} else {
    			nextPage = null;
    			traversePage();
    		}
		} while (nextPage != null);

		for(Advertisement advertisement : allAddvertisements) {
			if (!dbHasAdvertisement(advertisement)) {
				saveAdvertisement(advertisement.toDBObject());
				newAddvertisements.add(advertisement);
			}
		}

		System.out.println("All advertisements: " + allAddvertisements.size());
		System.out.println("New advertisements: " + newAddvertisements.size());
		emailClient.sendEmail(new String[]{"porodicausrcu4@gmail.com", "marjan.jankovich@gmail.com"}, "Novi oglasi na za vas kriterijum", newAddvertisements);
		System.out.println("Execution compleeted");
	}
    
    private void traversePage() {
    	System.out.println("Traversing page " + ++pageCnt);
        WebElement listParent = driver.findElement(By.id("ad-list-3"));
        
        if(listParent != null) {
        	List<WebElement> addList = listParent.findElements(By.cssSelector("div[class='col-md-12 col-sm-12 col-xs-12 col-lg-12']"));
        	System.out.println("Found " + addList.size() + " elements in page.");
        	
        	for (WebElement listElem : addList) {
				String id = listElem.findElement(By.className("product-item")).getAttribute("data-id");
				
				WebElement priceElem = listElem.findElement(By.className("central-feature"));
				String price = priceElem.findElement(By.tagName("span")).getAttribute("data-value");
				
				WebElement titleElem = listElem.findElement(By.className("ad-title"));
				WebElement titleLinkElem = titleElem.findElement(By.tagName("a"));
				
				Advertisement advertisement = new Advertisement(id, price, titleLinkElem.getText(), titleLinkElem.getAttribute("href"));
				allAddvertisements.add(advertisement);
				System.out.println("Elem: \n" + advertisement.toString());
        	}
        }
    }
    
    public void saveAdvertisement(DBObject advertisement) {
    	collection.insert(advertisement);    	
    }

    public boolean dbHasAdvertisement(Advertisement advertisement) {
    	BasicDBObject query = new BasicDBObject();
    	query.put("_id", advertisement.getId());
    	
    	DBObject result = collection.findOne(query);

    	return (result != null) ? true : false;
    }
}
