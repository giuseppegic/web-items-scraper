package uk.co.gg.web.scrapers.shopping;

import java.io.IOException;
import java.math.BigDecimal;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import uk.co.gg.shopping.Item;
import uk.co.gg.web.parser.jsoup.JsoupParser;
import uk.co.gg.web.scrapers.InvalidStructureException;
import uk.co.gg.web.scrapers.InvalidStructureSimpleException;

/**
 * Scraper to extract all relevant information out of a HTML fragment.
 * 
 * @author GiuseppeG
 *
 */
public class ItemScraper extends BasicScraper{

	private ItemDetailsScraper itemDetailsScraper;
	
	private JsoupParser jsoupParser;
	
	public ItemScraper(){
		itemDetailsScraper = new ItemDetailsScraper();
	}
	
	public ItemScraper(JsoupParser jsoupParser, ItemDetailsScraper itemDetailsScraper){
		this.itemDetailsScraper = itemDetailsScraper;
		this.jsoupParser = jsoupParser;
	}
	/**
	 * Parse and extract information from an HTML fragment related to an item.
	 * 
	 * <p> Extracted information is used to populate the given Item. </p>
	 * 
	 * @param itemFragment the fragment containing the item to scrape.
	 * @param item the Item to populate.
	 * @throws InvalidStructureException
	 * @throws IOException 
	 * 
	 * @see Element
	 */
	public void scrapeItem(Element itemFragment, Item item) throws InvalidStructureException {
		final Element titleFragment = extractElement(".productInfo > h3 > a", "Title", itemFragment);
		
		try {
			item.setTitle(extractElementText(titleFragment, "Title", false));
		} catch (InvalidStructureSimpleException e) {
			throw new InvalidStructureException(e.getMessage(), itemFragment.html());
		}
		
		final String price = extractElementText(".pricePerUnit", "Price", itemFragment, false);
		if(!price.startsWith("£")){
			throw new InvalidStructureException("Price must be in Pounds", itemFragment.html());
		}
		try {
			item.setPrice(new BigDecimal(price.substring(1)));
		} catch (NumberFormatException e) {
			throw new InvalidStructureException("Price is not a number", itemFragment.html());
		}
		
		if(!titleFragment.hasAttr("href")){
			throw new InvalidStructureException("Item must have link to details", itemFragment.html());
		}
		
		try {
			final Response detailsResponse = jsoupParser.get(titleFragment.attr("href"));
			
			if(detailsResponse != null){
				final Document detailsDocument = detailsResponse.parse();
				
				itemDetailsScraper.scrapeItemDetails(detailsDocument, item);
			}
		} catch (IOException e) {
			throw new InvalidStructureException("Unable to retrieve additional details", itemFragment.html());
		}
		
	}
}
