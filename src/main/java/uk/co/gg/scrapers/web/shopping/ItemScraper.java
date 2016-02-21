package uk.co.gg.scrapers.web.shopping;

import java.math.BigDecimal;

import org.jsoup.nodes.Element;

import uk.co.gg.scrapers.web.InvalidStructureException;
import uk.co.gg.shopping.Item;

/**
 * Scraper to extract all relevant information out of a HTML fragment.
 * 
 * @author GiuseppeG
 *
 */
public class ItemScraper extends BasicScraper{

	/**
	 * Parse and extract information from an HTML fragment related to an item.
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
		item.setTitle(extractElement(".productInfo > h3 > a", "Title", itemFragment, false));
		
		final String price = extractElement(".pricePerUnit", "Price", itemFragment, false);
		if(!price.startsWith("£")){
			throw new InvalidStructureException("Price must be in Pounds", itemFragment.html());
		}
		try {
			item.setPrice(new BigDecimal(price.substring(1)));
		} catch (NumberFormatException e) {
			throw new InvalidStructureException("Price is not a number", itemFragment.html());
		}
	}
}
