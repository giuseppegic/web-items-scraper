package uk.co.gg.web.scrapers.shopping;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import uk.co.gg.web.scrapers.InvalidStructureException;
import uk.co.gg.web.scrapers.InvalidStructureSimpleException;

public class BasicScraper {
	
	protected Element extractElement(String selector, String elementName, Element itemFragment) throws InvalidStructureException {
		final Element fragment = itemFragment.select(selector).first();

		if (fragment == null) {
			throw new InvalidStructureException("Unable to find " + elementName, itemFragment.html());
		}
		
		return fragment;
	}
	
	protected String extractElementText(Element fragment, String elementName,  boolean captureNestedValues) throws InvalidStructureSimpleException {
		final String elementValue = captureNestedValues ? fragment.text() : fragment.ownText();
		if (StringUtils.isEmpty(elementValue)) {
			throw new InvalidStructureSimpleException(elementName + " cannot be empty");
		}
		
		return elementValue;
	}
	
	protected String extractElementText(String selector, String elementName, Element parentFragment, boolean captureNestedValues) throws InvalidStructureException {
		final Element elementFragment = extractElement(selector, elementName, parentFragment);

		try {
			return extractElementText(elementFragment, elementName, captureNestedValues);
		} catch (InvalidStructureSimpleException e) {
			throw new InvalidStructureException(e.getMessage(), parentFragment.html());
		}
	}
}
