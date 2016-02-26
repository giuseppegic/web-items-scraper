package uk.co.gg.web.scrapers.shopping.application;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;

import uk.co.gg.console.ConsoleWriter;
import uk.co.gg.shopping.ItemList;
import uk.co.gg.shopping.marshaller.JsonMarshaller;
import uk.co.gg.web.scrapers.InvalidStructureException;
import uk.co.gg.web.scrapers.shopping.ShoppingWebScraper;

/**
 * ShoppingWebScraperApp is able to scrape an item list starting from a URL and
 * output the result in JSON format.
 * 
 * @author GiuseppeG
 *
 */
@Named
public class ShoppingWebScraperApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingWebScraperApp.class);
	
	private final ConsoleWriter console;

	private JsonMarshaller jsonMarshaller;

	private ShoppingWebScraper shoppingWebScraper;

	@Inject
	public ShoppingWebScraperApp(ConsoleWriter console, JsonMarshaller jsonMarshaller, ShoppingWebScraper shoppingWebScraper){
		this.console=console;
		this.jsonMarshaller = jsonMarshaller;
		this.shoppingWebScraper = shoppingWebScraper;
	}

	public static void main(String[] args) {
		final ShoppingWebScraper shoppingWebScraper = Guice.createInjector()
				.getInstance(ShoppingWebScraper.class);

		
		new ShoppingWebScraperApp(new ConsoleWriter(System.out), new JsonMarshaller(), shoppingWebScraper).start(args);
	}
	
	public void start(String[] args) {

		printBanner();
		
		if (args.length < 1 || args.length > 2) {

			printUsage();
			
		} else {
			boolean prettyPrint = checkIfJsonIsToBeIndented(args);

			final String itemListURL = args[0];
			console.printLine("Scraping item list from: " + itemListURL);

			try {
				final String itemListJson = scrapeItemListPageToJson(itemListURL, prettyPrint);
				
				console.printLine();
				console.printLine("Scraped item list JSON: ");
				console.printLine(itemListJson);
			} catch (InvalidStructureException | IOException e) {
				if(LOGGER.isErrorEnabled()){
					LOGGER.error("Unable to scrape list", e);
				}
				
				console.printLine();
				console.printLine("Unable to scrape list: " + e.getMessage());
			}

		}
	}

	/**
	 * Scrape an item list starting from a URL and serialize it in JSON format.
	 * 
	 * @param itemListURL
	 *            the link to the item list.
	 * @param prettyPrint
	 *            if false generate a compact JSON, otherwise it generates an
	 *            indented one.
	 * @return the item list in JSON format.
	 * @throws InvalidStructureException
	 *             when item list or embedded items do not respect known
	 *             structure.
	 * @throws IOException
	 *             when it's not possible serialize the item list or to retrieve
	 *             the given web resource or a required linked additional
	 *             resource.
	 */
	public String scrapeItemListPageToJson(String itemListURL, boolean prettyPrint)
			throws InvalidStructureException, IOException {
		final ItemList itemList = shoppingWebScraper.scrapeItemListPage(itemListURL);

		return jsonMarshaller.marshall(itemList, prettyPrint);
	}
	
	private void printBanner() {
		console.printLine();
		console.printLine(" === ShoppingWebScraper - "+ShoppingWebScraper.class.getPackage().getImplementationVersion()+ " ===");
		console.printLine();
		
	}

	private boolean checkIfJsonIsToBeIndented(String[] args) {
		boolean prettyPrint = true;
		if (args.length == 2) {
			if ("true".equalsIgnoreCase(args[1])) {
				prettyPrint = false;

				console.printLine("JSON will be generated in compact format.");
			} else {
				if(!"false".equalsIgnoreCase(args[1])){
					console.printLine("Invalid value for parameter $COMPACT, using default 'false'. Valid values are 'true' or 'false'.");
				}
			}
		}
		return prettyPrint;
	}

	private void printUsage() {
		console.printLine(
				"Usage: java com.gg.shopping.starter.ShoppingWebScraper.java $URL_WITH_ITEM_LIST [$COMPACT]");
		console.printLine("By default COMPACT is false and JSON will be indented.");
		console.printLine();
	}

}
