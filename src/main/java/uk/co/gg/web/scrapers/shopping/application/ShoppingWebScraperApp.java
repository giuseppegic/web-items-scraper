package uk.co.gg.web.scrapers.shopping.application;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.co.gg.shopping.ItemList;
import uk.co.gg.shopping.marshaller.JsonMarshaller;
import uk.co.gg.web.scrapers.InvalidStructureException;
import uk.co.gg.web.scrapers.shopping.ShoppingWebScraper;

/**
 * ShoppingWebScraperApp is able to scrape an item list starting from a URL and output the result in JSON format.
 * 
 * @author GiuseppeG
 *
 */
@Named
public class ShoppingWebScraperApp {
	private static final JsonMarshaller JSON_MARSHALLER = new JsonMarshaller();

	@Inject
	private ShoppingWebScraper shoppingWebScraper;

	public static void main(String[] args) {
		if (args.length < 0) {
			System.out.println("Usage: java com.gg.shopping.starter.ShoppingWebScraper.java $URL_WITH_ITEM_LIST");
		} else {
			final String itemListURL = args[0];
			final Injector injector = Guice.createInjector();
			final ShoppingWebScraperApp shoppingWebScraperApp = injector.getInstance(ShoppingWebScraperApp.class);

			try {
				final String itemListJson = shoppingWebScraperApp.scrapeItemListPageToJson(itemListURL);
				System.out.println(itemListJson);
			} catch (InvalidStructureException | IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Scrape an item list starting from a URL and serialize it in JSON format.
	 * 
	 * @param itemListURL
	 *            the link to the item list.
	 * @return the item list in json format.
	 * @throws InvalidStructureException
	 *             when item list or embedded items do not respect known
	 *             structure.
	 * @throws IOExceptionwhen
	 *             it's not possible to retrieve the given web resource or a
	 *             required linked additional resource.
	 */
	public String scrapeItemListPageToJson(String itemListURL) throws InvalidStructureException, IOException {
		final ItemList itemList = shoppingWebScraper.scrapeItemListPage(itemListURL);

		return JSON_MARSHALLER.marshall(itemList);
	}
}
