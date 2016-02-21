package uk.co.gg.scrapers.web.shopping;

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
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

import static uk.co.gg.scrapers.web.matchers.InvalidStructureExceptionMatcher.isInvalidHtmlFragmentWithError;

public class ItemScraperJUnitTest {
	
	private static MessageFormat validItemFormat;
	
	private static final String[] defaultItemValues={"title", "£0.10"};
	
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
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingTitle("sample Title"));
		final Item item = new Item();
		
		// When
		testSubject.scrapeItem(itemFragment, item);
		
		// Then
		assertThat(item.getTitle(), is("sample Title"));
	}

	@Test
	public void shouldThrowInvalidStructureExceptionWhenTitleIsEmpty() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingTitle(""));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Title cannot be empty"));
		
		// When
		testSubject.scrapeItem(itemFragment, new Item());
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenUnableToFindTitle() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(readFile("invalid-title-item-format.html"));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Unable to find Title"));
		
		// When
		testSubject.scrapeItem(itemFragment, new Item());
	}
	
	@Test
	public void shouldExtractItemPrice() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingPrice("£1.80"));
		final Item item = new Item();
		
		// When
		testSubject.scrapeItem(itemFragment, item);
		
		// Then
		assertThat(item.getPrice(), is(new BigDecimal("1.80")));
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenItemPriceIsNotInPounds() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingPrice("1.80"));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Price must be in Pounds"));
				
		// When
		testSubject.scrapeItem(itemFragment, new Item());
	}

	@Test
	public void shouldThrowInvalidStructureExceptionWhenItemPriceIsNotANumber() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingPrice("£item"));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Price is not a number"));
				
		// When
		testSubject.scrapeItem(itemFragment, new Item());
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenItemPriceIsEmpty() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingPrice(""));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Price cannot be empty"));
				
		// When
		testSubject.scrapeItem(itemFragment, new Item());
	}
	
	@Test
	public void shouldThrowInvalidStructureExceptionWhenItemPriceIsNotFound() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(readFile("invalid-price-item-format.html"));
		
		// Expect
		expected.expect(isInvalidHtmlFragmentWithError(itemFragment.html(), "Unable to find Price"));
				
		// When
		testSubject.scrapeItem(itemFragment, new Item());
	}
	
	private static String readFile(String path) throws IOException {
		final InputStream stream = ItemScraperJUnitTest.class.getResourceAsStream(path);
		return IOUtils.toString(stream);
	}
	
	private String formatItemInjectingPrice(String price) {
		final String[] itemValues = Arrays.copyOf(defaultItemValues, defaultItemValues.length);
		itemValues[1] = price;
		
		return validItemFormat.format(itemValues);
	}
	
	private String formatItemInjectingTitle(String title) {
		final String[] itemValues = Arrays.copyOf(defaultItemValues, defaultItemValues.length);
		itemValues[0] = title;
		
		return validItemFormat.format(itemValues);
	}
}
