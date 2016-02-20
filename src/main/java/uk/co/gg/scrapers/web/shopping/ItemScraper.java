package uk.co.gg.scrapers.web.shopping;

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

		final Element titleFragment = itemFragment.select(".productInfo > h3 > a").first();

		if (titleFragment == null) {
			throw new InvalidStructureException("Unable to find title", itemFragment.html());
		}
		final String title = titleFragment.ownText();
		if (StringUtils.isEmpty(title)) {
			throw new InvalidStructureException("Title cannot be empty", itemFragment.html());
		}

		item.setTitle(titleFragment.ownText());

		return item;
	}

}
