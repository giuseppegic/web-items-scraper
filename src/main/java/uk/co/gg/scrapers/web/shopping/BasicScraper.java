package uk.co.gg.scrapers.web.shopping;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import uk.co.gg.scrapers.web.InvalidStructureException;

public class BasicScraper {
	
	protected String extractElement(String selector, String elementName, Element itemFragment, boolean captureNestedValues) throws InvalidStructureException {
		final Element fragment = itemFragment.select(selector).first();

		if (fragment == null) {
			throw new InvalidStructureException("Unable to find " + elementName, itemFragment.html());
		}
		
		final String elementValue = captureNestedValues ? fragment.text() : fragment.ownText();
		if (StringUtils.isEmpty(elementValue)) {
			throw new InvalidStructureException(elementName + " cannot be empty", itemFragment.html());
		}
		
		return elementValue;
	}
}
