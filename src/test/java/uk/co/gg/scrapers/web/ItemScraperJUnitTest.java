package uk.co.gg.scrapers.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.co.gg.scrapers.web.shopping.ItemScraper;
import uk.co.gg.shopping.item.Item;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.apache.commons.io.IOUtils;

import static uk.co.gg.scrapers.web.matchers.InvalidStructureExceptionMatcher.isInvalidHtmlFragmentWithError;

public class ItemScraperJUnitTest {
	
	private static MessageFormat validItemFormat;
	
	@Rule
	public ExpectedException expected= ExpectedException.none();
	
	private ItemScraper testSubject=new ItemScraper();
	
	@BeforeClass
	public static void setup() throws IOException{
		validItemFormat = new MessageFormat(readFile("valid-item-format.html"));
	}
	
	@Test
	public void shouldExtractItemTitle() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(injectTitle(validItemFormat, "sample Title"));
		
		// When
		final Item item = testSubject.scrapeItem(itemFragment);
		
		// Then
		assertThat(item.getTitle(), is("sample Title"));
	}

	@Test
	public void shouldThrowInvalidStructureExceptionWhenTitleIsEmpty() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(injectTitle(validItemFormat, ""));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Title cannot be empty"));
		
		// When
		testSubject.scrapeItem(itemFragment);
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenUnableToFindTitle() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(readFile("invalid-title-item-format.html"));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Unable to find title"));
		
		// When
		testSubject.scrapeItem(itemFragment);
	}
	
	private static String readFile(String path) throws IOException {
		final InputStream stream = ItemScraperJUnitTest.class.getResourceAsStream(path);
		return IOUtils.toString(stream);
	}
	
	private String injectTitle(MessageFormat validItemFormat, String title) {
		return validItemFormat.format(new Object[]{title});
	}
}
