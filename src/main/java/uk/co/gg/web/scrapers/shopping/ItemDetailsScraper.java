package uk.co.gg.web.scrapers.shopping;

import javax.inject.Named;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.gg.shopping.Item;
import uk.co.gg.web.scrapers.InvalidStructureException;

/**
 * Scraper to extract all relevant detailed information out of a HTML fragment.
 * 
 * @author GiuseppeG
 *
 */
@Named
public class ItemDetailsScraper extends BasicScraper{
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemDetailsScraper.class);
	
	/**
	 * Parse and extract detailed information from an HTML fragment related to an item.
	 * 
	 * <p> Extracted information is used to populate the given Item. </p>
	 * 
	 * @param itemFragment the fragment containing the item to scrape.
	 * @param item the Item to populate.
	 * @throws InvalidStructureException when item details do not respect the known structure.
	 * 
	 * @see Element
	 */
	public void scrapeItemDetails(Element itemFragment, Item item) throws InvalidStructureException {
		final String description = extractElementText(".productDataItemHeader:containsOwn(Description) + .productText", "Description", itemFragment, true);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Scraped description: " + description);
		}
		
		item.setDescription(description);
	}

}
