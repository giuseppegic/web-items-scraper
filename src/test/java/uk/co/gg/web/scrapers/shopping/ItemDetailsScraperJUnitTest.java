package uk.co.gg.web.scrapers.shopping;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.gg.files.Reader.readFile;
import static uk.co.gg.web.scrapers.matchers.InvalidStructureExceptionMatcher.isInvalidHtmlFragmentWithError;

import java.io.IOException;
import java.text.MessageFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.co.gg.shopping.Item;

public class ItemDetailsScraperJUnitTest {

	private static MessageFormat validItemFormat;

	@Rule
	public ExpectedException expected = ExpectedException.none();

	private ItemDetailsScraper testSubject = new ItemDetailsScraper();

	@BeforeClass
	public static void setup() throws IOException {
		validItemFormat = new MessageFormat(readFile("valid-item-details-format.html", ItemDetailsScraperJUnitTest.class));
	}

	@Test
	public void shouldExtractItemSimpleDescription() throws Exception {
		// Given
		final Element itemFragment = Jsoup.parseBodyFragment(formatDetailsInjectingDescription("Item description", ""));
		final Item item = new Item();
		
		// When
		testSubject.scrapeItemDetails(itemFragment, item);

		// Then
		assertThat(item.getDescription(), is("Item description"));
	}
	
	@Test
	public void shouldExtractItemComposedDescription() throws Exception {
		// Given
		final Element itemFragment = Jsoup.parseBodyFragment(formatDetailsInjectingDescription("Item", "description"));
		final Item item = new Item();
		
		// When
		testSubject.scrapeItemDetails(itemFragment, item);

		// Then
		assertThat(item.getDescription(), is("Item description"));
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenDescriptionIsEmpty() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatDetailsInjectingDescription("",""));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Error while extracting text from element 'Description'"));
		
		// When
		testSubject.scrapeItemDetails(itemFragment, new Item());
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenUnableToFindDescription() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(readFile("invalid-description-item-details-format.html", ItemDetailsScraperJUnitTest.class));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Unable to find Description"));
		
		// When
		testSubject.scrapeItemDetails(itemFragment, new Item());
	}
	
	private String formatDetailsInjectingDescription(String... description) {
		return validItemFormat.format(description);
	}
}
