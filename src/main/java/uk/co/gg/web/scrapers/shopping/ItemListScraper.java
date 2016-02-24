package uk.co.gg.web.scrapers.shopping;

import java.io.IOException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.co.gg.shopping.Item;
import uk.co.gg.shopping.ItemList;
import uk.co.gg.web.scrapers.InvalidStructureException;

public class ItemListScraper {

	private ItemScraper itemScraper;
	
	public ItemListScraper(){
		itemScraper = new ItemScraper();
	}
	
	public ItemListScraper(ItemScraper itemScraper){
		this.itemScraper = itemScraper;
	}

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
