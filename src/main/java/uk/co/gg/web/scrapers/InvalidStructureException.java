package uk.co.gg.web.scrapers;

/**
 * Signals that some expected information can't be scraped.
 * 
 * <p>
 * InvalidStructureException contains the details of the related html fragment.
 * </p>
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
	 * 
	 * @param errorMessage
	 *            the description of the structural error.
	 * @param cause
	 *            the cause of the exception.
	 * @param htmlFragment
	 *            the fragment (as a String in order to be serializable).
	 */
	public InvalidStructureException(String errorMessage, String htmlFragment, Throwable cause) {
		super(errorMessage, cause);

		this.htmlFragment = htmlFragment;
	}

	/**
	 * Construct an InvalidStructureException for an HTML fragment.
	 * 
	 * @param errorMessage
	 *            the description of the structural error.
	 * @param htmlFragment
	 *            the fragment (as a String in order to be serializable).
	 */
	public InvalidStructureException(String errorMessage, String htmlFragment) {
		this(errorMessage, htmlFragment, null);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		builder
			.append("InvalidStructureException: ")
			.append(System.lineSeparator()).append("Error message: ").append(getLocalizedMessage())
			.append(System.lineSeparator()).append("HTML fragment: ")
			.append(System.lineSeparator()).append(getHtmlFragment());

		return builder.toString();
	}

	public String getHtmlFragment() {
		return htmlFragment;
	}

	public void setHtmlFragment(String htmlFragment) {
		this.htmlFragment = htmlFragment;
	}
}
