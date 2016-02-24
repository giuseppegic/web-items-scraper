package uk.co.gg.web.scrapers.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import uk.co.gg.web.scrapers.InvalidStructureException;

public class InvalidStructureExceptionMatcher extends BaseMatcher<Exception>{
	
	private final String fragmentHtml;
	
	private final String error;
	
	public static InvalidStructureExceptionMatcher isInvalidHtmlFragmentWithError(String fragmentHtml, String error){
		return new InvalidStructureExceptionMatcher(fragmentHtml, error);
	}
	
	public InvalidStructureExceptionMatcher(String fragmentHtml, String error) {
		this.fragmentHtml = fragmentHtml;
		this.error = error;
	}
	
	public boolean matches(Object item) {
		if(item==null || !(item instanceof InvalidStructureException)){
			return false;
		}
		final InvalidStructureException e = (InvalidStructureException) item;
		
		if(!e.getHtmlFragment().equals(fragmentHtml)){
			return false;
		}
		if(!e.getMessage().equals(error)){
			return false;
		}
		return true;
	}

	public void describeTo(Description description) {
		description.appendText("expected InvalidStructureException with fragment: " + fragmentHtml);
	}
}
