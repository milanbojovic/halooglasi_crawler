package com.halooglasi.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Advertisement {

	private String id;
	private String price;
	private String title;
	private String link;

	public Advertisement(String id, String price, String title, String link) {
		super();
		this.id = id;
		this.price = price;
		this.title = title;
		this.link = link;
	}

	public String getId() {
		return id;
	}

	public String toString() {
		return "ID: '" + id + "', PRICE: '" + price + "', TITLE: " + title + "', LINK: '" + link + "'";
	}

	public DBObject toDBObject() {
		DBObject dbObject = new BasicDBObject().append("_id", id).append("price", price).append("title", title)
				.append("link", link);
		return dbObject;
	}
}
