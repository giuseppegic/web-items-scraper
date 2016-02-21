package uk.co.gg.scrapers.web.shopping;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.gg.scrapers.web.matchers.InvalidStructureExceptionMatcher.isInvalidHtmlFragmentWithError;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.apache.commons.io.IOUtils;
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
		validItemFormat = new MessageFormat(readFile("valid-item-details-format.html"));
	}

	@Test
	public void shouldExtractItemSimpleDescription() throws Exception {
		// Given
		final Element itemFragment = Jsoup.parseBodyFragment(formatDetailsInjectingDescription("Item description", ""));
		final Item item = new Item();
		
		// When
		testSubject.scrapeItem(itemFragment, item);

		// Then
		assertThat(item.getDescription(), is("Item description"));
	}
	
	@Test
	public void shouldExtractItemComposedDescription() throws Exception {
		// Given
		final Element itemFragment = Jsoup.parseBodyFragment(formatDetailsInjectingDescription("Item", "description"));
		final Item item = new Item();
		
		// When
		testSubject.scrapeItem(itemFragment, item);

		// Then
		assertThat(item.getDescription(), is("Item description"));
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenDescriptionIsEmpty() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatDetailsInjectingDescription("",""));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Description cannot be empty"));
		
		// When
		testSubject.scrapeItem(itemFragment, new Item());
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenUnableToFindDescription() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(readFile("invalid-description-item-details-format.html"));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Unable to find Description"));
		
		// When
		testSubject.scrapeItem(itemFragment, new Item());
	}
	
	private static String readFile(String path) throws IOException {
		final InputStream stream = ItemScraperJUnitTest.class.getResourceAsStream(path);
		return IOUtils.toString(stream);
	}
	
	private String formatDetailsInjectingDescription(String... description) {
		return validItemFormat.format(description);
	}
}
