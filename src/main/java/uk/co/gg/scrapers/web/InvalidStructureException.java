package uk.co.gg.scrapers.web;

import org.jsoup.nodes.Element;

/**
 * Signals that some expected information can't be scraped.
 * 
 * @author GiuseppeG
 */
public class InvalidStructureException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String htmlFragment;

	public InvalidStructureException() {
		super();
	}

	/**
	 * Construct an InvalidStructureException for an HTML fragment.
	 * @param errorMessage the description of the structural error.
	 * @param htmlFragment the fragment (as a String in order to be serializable).
	 */
	public InvalidStructureException(String errorMessage, String htmlFragment) {
		super(errorMessage);
		
		this.htmlFragment = htmlFragment;
	}
	
	public String getHtmlFragment() {
		return htmlFragment;
	}
}
