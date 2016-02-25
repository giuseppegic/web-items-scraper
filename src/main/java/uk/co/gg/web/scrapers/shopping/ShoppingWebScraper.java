package uk.co.gg.web.scrapers.shopping;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.Connection.Response;

import uk.co.gg.shopping.ItemList;
import uk.co.gg.web.parser.jsoup.JsoupParser;
import uk.co.gg.web.scrapers.InvalidStructureException;

/**
 * ShoppingWebScraper is able to scrape an item list starting from a URL.
 * 
 * @author GiuseppeG
 *
 */
@Named
public class ShoppingWebScraper {

	private final JsoupParser jsoupParser;

	private final ItemListScraper itemListScraper;

	

	@Inject
	public ShoppingWebScraper(JsoupParser jsoupParser, ItemListScraper itemListScraper) {
		this.jsoupParser = jsoupParser;
		this.itemListScraper = itemListScraper;
	}

	/**
	 * Scrape item list present at given URL.
	 * 
	 * @param itemListURL
	 *            the URL to the web page with the item list.
	 * @return the scraped ItemList.
	 * 
	 * @throws InvalidStructureException
	 *             when item list or embedded items do not respect known
	 *             structure.
	 * @throws IOException
	 *             when it's not possible to retrieve the given web resource or
	 *             a required linked additional resource.
	 */
	public ItemList scrapeItemListPage(String itemListURL) throws InvalidStructureException, IOException {
		final Response response = jsoupParser.get(itemListURL);
		return itemListScraper.scrapeItemList(response.parse());
	}
}
