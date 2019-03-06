package com.halooglasi.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class LivingAreaSearch {

    private WebDriver driver;
    private String url = "https://www.halooglasi.com/nekretnine";
    private List<Advertisement> addvertisements = new ArrayList<Advertisement>();
    private MongoClient mongoClient;
    private DB database;
    private DBCollection collection;
    
    
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

    public void executeQueryAndHandleResults(Smtp2GoEmailClient emailClient) {
    	WebElement nextPage = null;
    	
    	do {
    		WebElement footerElement = driver.findElement(By.id("pager-1"));
    		if (footerElement.findElements(By.linkText("Sledeća »")).size() > 0) {
    			nextPage = footerElement.findElement(By.linkText("Sledeća »"));
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
    	
    	emailClient.sendEmail(addvertisements);
    	System.out.println("Execution compleeted");
    }
    
    private void traversePage() {
    	System.out.println("Traversing page");
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
				
				if (!dbHasAdvertisement(advertisement)) {
					saveAdvertisement(advertisement.toDBObject());
					addvertisements.add(advertisement);					
				}
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
