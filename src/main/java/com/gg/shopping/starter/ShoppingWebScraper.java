package com.gg.shopping.starter;

import java.io.IOException;

import org.jsoup.Connection.Response;

import uk.co.gg.shopping.ItemList;
import uk.co.gg.shopping.marshaller.JsonMarshaller;
import uk.co.gg.web.parser.jsoup.JsoupParser;
import uk.co.gg.web.scrapers.InvalidStructureException;
import uk.co.gg.web.scrapers.shopping.ItemListScraper;


/**
 *  ShoppingWebScraper is able to scrape an item list starting from a URL.
 *  
 * @author Peppe
 *
 */
public class ShoppingWebScraper {
	private static final JsonMarshaller JSON_MARSHALLER = new JsonMarshaller();

	private final JsoupParser jsoupParser;
	
	private final ItemListScraper itemListScraper;

	public static void main(String[] args) {
		if (args.length < 0) {
			System.out.println("Usage: java com.gg.shopping.starter.ShoppingWebScraper.java <URL_WITH_ITEM_LIST>");
		} else {
			final String itemListURL = args[0];
			final ShoppingWebScraper shoppingWebScraper = new ShoppingWebScraper();

			
			try {
				final ItemList itemList = shoppingWebScraper.scrapeItemListPage(itemListURL);
				System.out.println(JSON_MARSHALLER.marshall(itemList));
			} catch (InvalidStructureException | IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	public ShoppingWebScraper(JsoupParser jsoupParser, ItemListScraper itemListScraper){
		this.jsoupParser = jsoupParser;
		this.itemListScraper = itemListScraper;
	}
	
	public ShoppingWebScraper() {
		this.jsoupParser = new JsoupParser();
		this.itemListScraper = new ItemListScraper();
	}

	/**
	 * Scrape item list present at given URL.
	 * 
	 * @param itemListURL the URL to the web page with the item list.
	 * @return the scraped ItemList.
	 * 
	 * @throws InvalidStructureException when item list or embedded items do not respect known structure.
	 * @throws IOException when it's not possible to retrieve the given web resource or a required linked additional resource.
	 */
	public ItemList scrapeItemListPage(String itemListURL) throws InvalidStructureException, IOException {
		final Response response = jsoupParser.get(itemListURL);
		return itemListScraper.scrapeItemList(response.parse());
	}
}
