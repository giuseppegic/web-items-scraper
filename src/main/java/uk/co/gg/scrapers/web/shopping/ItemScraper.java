package uk.co.gg.scrapers.web.shopping;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import uk.co.gg.scrapers.web.InvalidStructureException;
import uk.co.gg.shopping.item.Item;

/**
 * Scraper to extract all relevant information out of a HTML fragment.
 * 
 * @author GiuseppeG
 *
 */
public class ItemScraper {

	/**
	 * Parse and extract information from an HTML fragment related to an item.
	 * 
	 * @param itemFragment the fragment containing the item to scrape.
	 * 
	 * @return the Item containing all scraped information.
	 * @throws InvalidStructureException
	 * @see Element
	 */
	public Item scrapeItem(Element itemFragment) throws InvalidStructureException {
		final Item item = new Item();

		item.setTitle(extractElement(".productInfo > h3 > a", "Title", itemFragment));
		
		final String price = extractElement(".pricePerUnit", "Price", itemFragment);
		if(!price.startsWith("£")){
			throw new InvalidStructureException("Price must be in Pounds", itemFragment.html());
		}
		try {
			item.setPrice(new BigDecimal(price.substring(1)));
		} catch (NumberFormatException e) {
			throw new InvalidStructureException("Price is not a number", itemFragment.html());
		}
		return item;
	}

	private String extractElement(String selector, String elementName, Element itemFragment) throws InvalidStructureException {
		final Element priceFragment = itemFragment.select(selector).first();

		if (priceFragment == null) {
			throw new InvalidStructureException("Unable to find " + elementName, itemFragment.html());
		}
		final String price = priceFragment.ownText();
		if (StringUtils.isEmpty(price)) {
			throw new InvalidStructureException(elementName + " cannot be empty", itemFragment.html());
		}
		return priceFragment.ownText();
	}
}
