package uk.co.gg.scrapers.web.shopping;

import org.jsoup.nodes.Element;

import uk.co.gg.scrapers.web.InvalidStructureException;
import uk.co.gg.shopping.Item;

/**
 * Scraper to extract all relevant detailed information out of a HTML fragment.
 * 
 * @author GiuseppeG
 *
 */
public class ItemDetailsScraper extends BasicScraper{

	/**
	 * Parse and extract detailed information from an HTML fragment related to an item.
	 * 
	 * <p> Extracted information is used to populate the given Item. </p>
	 * 
	 * @param itemFragment the fragment containing the item to scrape.
	 * @param item the Item to populate.
	 * @throws InvalidStructureException
	 * 
	 * @see Element
	 */
	public void scrapeItem(Element itemFragment, Item item) throws InvalidStructureException {
		item.setDescription(extractElement(".productDataItemHeader:containsOwn(Description) + .productText", "Description", itemFragment, true));
	}

}
