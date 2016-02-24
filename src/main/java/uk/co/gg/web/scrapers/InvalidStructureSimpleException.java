package uk.co.gg.web.scrapers;


/**
 * Signals that some expected information can't be scraped.
 * 
 * <p>InvalidStructureSimpleException has no context of the related html fragment.</p>
 * 
 * @author GiuseppeG
 */
public class InvalidStructureSimpleException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InvalidStructureSimpleException() {
		super();
	}

	public InvalidStructureSimpleException(String errorMessage) {
		super(errorMessage);
	}
}
