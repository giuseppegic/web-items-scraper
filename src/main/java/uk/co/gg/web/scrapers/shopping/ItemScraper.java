package uk.co.gg.web.scrapers.shopping;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@Named
public class ItemScraper extends BasicScraper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemScraper.class);

	private ItemDetailsScraper itemDetailsScraper;

	private JsoupParser jsoupParser;

	@Inject
	public ItemScraper(JsoupParser jsoupParser, ItemDetailsScraper itemDetailsScraper) {
		this.itemDetailsScraper = itemDetailsScraper;
		this.jsoupParser = jsoupParser;
	}

	/**
	 * Parse and extract information from an HTML fragment related to an item.
	 * 
	 * <p>
	 * Extracted information is used to populate the given Item.
	 * </p>
	 * 
	 * @param itemFragment
	 *            the fragment containing the item to scrape.
	 * @param item
	 *            the Item to populate.
	 * @throws InvalidStructureException
	 *             when the item does not respect the known structure.
	 *             
	 * @see Element
	 */
	public void scrapeItem(Element itemFragment, Item item) throws InvalidStructureException {
		final Element titleFragment = extractElement(".productInfo > h3 > a", "Title", itemFragment);

		scrapeTitle(itemFragment, item, titleFragment);
		scrapePrice(itemFragment, item);

		if (!titleFragment.hasAttr("href")) {
			throw new InvalidStructureException("Item must have link to details", itemFragment.html());
		}

		final String href = titleFragment.attr("href");
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Scraped href: " + href);
		}

		try {
			final Response detailsResponse = jsoupParser.get(href);

			scrapeSize(detailsResponse, item);
			scrapeDetails(detailsResponse, item, itemFragment);
		} catch (IOException e) {
			throw new InvalidStructureException("Unable to retrieve additional details", itemFragment.html(), e);
		}

	}

	private void scrapeSize(Response detailsResponse, Item item) throws InvalidStructureException {

		final double size = detailsResponse.bodyAsBytes().length;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Scraped size: " + size);
		}

		final String sizeInKb = toKilobytes(size);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Scraped size in kb: " + sizeInKb);
		}
		item.setDetailsByteSize(sizeInKb + "kb");
	}

	private void scrapeDetails(Response detailsResponse, Item item, Element itemFragment)
			throws InvalidStructureException {
		try {
			final Document detailsDocument = detailsResponse.parse();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Scraping details");
			}

			itemDetailsScraper.scrapeItemDetails(detailsDocument, item);
		} catch (IOException e) {
			throw new InvalidStructureException("Unable to retrieve additional details", itemFragment.html(), e);
		}
	}

	private void scrapePrice(Element itemFragment, Item item) throws InvalidStructureException {
		final String price = extractElementText(".pricePerUnit", "Price", itemFragment, false);

		if (!price.startsWith("&pound") && !price.startsWith("£")) {
			throw new InvalidStructureException("Price must be in Pounds", itemFragment.html());
		}

		try {
			final BigDecimal priceUnit = new BigDecimal(price.replace("&pound","").replace("£",""));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Scraped price unit: " + priceUnit);
			}

			item.setPrice(priceUnit);
		} catch (NumberFormatException e) {
			throw new InvalidStructureException("Price is not a number", itemFragment.html());
		}
	}

	private void scrapeTitle(Element itemFragment, Item item, final Element titleFragment)
			throws InvalidStructureException {
		try {
			final String title = extractElementText(titleFragment, "Title", false);

			item.setTitle(title);
		} catch (InvalidStructureSimpleException e) {
			throw new InvalidStructureException(e.getMessage(), itemFragment.html(), e);
		}
	}

	private String toKilobytes(double length) {
		return new DecimalFormat("###,##0.##").format(length / 1024);
	}
}
