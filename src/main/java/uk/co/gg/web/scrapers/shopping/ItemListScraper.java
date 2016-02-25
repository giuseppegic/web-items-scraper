package uk.co.gg.web.scrapers.shopping;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.gg.shopping.Item;
import uk.co.gg.shopping.ItemList;
import uk.co.gg.web.scrapers.InvalidStructureException;

/**
 * Scraper to extract all items in a list contained in a HTML fragment.
 * 
 * @author GiuseppeG
 *
 */
@Named
public class ItemListScraper {

	private ItemScraper itemScraper;
	
	@Inject
	public ItemListScraper(ItemScraper itemScraper){
		this.itemScraper = itemScraper;
	}

	/**
	 * Scrape item list contained in given fragment.
	 * @param itemListFragment the HTML fragment containing the list.
	 * @return the scraped ItemList 
	 * @throws InvalidStructureException when item list or embedded items do not respect the known structure.
	 * @throws IOException when it's not possible to retrieve the given web resource or a required linked additional resource.
	 */
	public ItemList scrapeItemList(Element itemListFragment) throws InvalidStructureException, IOException {
		final ItemList itemList = new ItemList();
		
		Elements itemsFragments = itemListFragment.select(".product");
		
		for(Element itemFragment:itemsFragments){
			final Item item = new Item();
		
			itemScraper.scrapeItem(itemFragment, item);
			itemList.add(item);
		}
		
		return itemList;
	}
	
}
