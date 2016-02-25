package uk.co.gg.web.scrapers.shopping;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static uk.co.gg.web.scrapers.matchers.InvalidStructureExceptionMatcher.isInvalidHtmlFragmentWithError;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matcher;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import uk.co.gg.shopping.Item;
import uk.co.gg.web.parser.jsoup.JsoupParser;

@RunWith(MockitoJUnitRunner.class)
public class ItemScraperJUnitTest {
	
	private static MessageFormat validItemFormat;
	
	private static final String[] defaultItemValues={"title", "£0.10", "linkToDetails"};
	
	@Rule
	public ExpectedException expected= ExpectedException.none();
	
	@Mock
	private ItemDetailsScraper itemDetailsScraperMock;
	
	@Mock
	private JsoupParser jsoupParserMock;
	
	@Mock
	private Response responseMock;
	
	@Mock
	private Document documentMock;

	private ItemScraper testSubject;
	
	@BeforeClass
	public static void setup() throws IOException{
		validItemFormat = new MessageFormat(readFile("valid-item-format.html"));
	}
	
	@Before
	public void initTestSubject() throws IOException{
		when(jsoupParserMock.get(anyString())).thenReturn(responseMock);
		when(responseMock.bodyAsBytes()).thenReturn(new byte[1]);
		
		testSubject=new ItemScraper(jsoupParserMock, itemDetailsScraperMock);
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
	
	@Test
	public void shouldExtractDetailsSizeWithDecimal() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingLinkToDetails("HREF1"));

		when(jsoupParserMock.get(anyString())).thenReturn(responseMock);
		when(responseMock.bodyAsBytes()).thenReturn(new byte[1512]);
		
		final Item item = new Item();
		
		// When
		testSubject.scrapeItem(itemFragment, item);
		
		// Then
		assertThat(item, is(anActualItemWithDetailsByteSize("1.48kb")));
	}
	
	@Test
	public void anActualItemWithRoundDetailsByteSize() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingLinkToDetails("HREF1"));

		when(jsoupParserMock.get(anyString())).thenReturn(responseMock);
		when(responseMock.bodyAsBytes()).thenReturn(new byte[8*1024]);
		
		final Item item = new Item();
		
		// When
		testSubject.scrapeItem(itemFragment, item);
		
		// Then
		assertThat(item, is(anActualItemWithDetailsByteSize("8kb")));
	}
	
	@Test
	public void shouldExtractItemDetails() throws Exception{
		// Given
		final Element itemFragment=Jsoup.parseBodyFragment(formatItemInjectingLinkToDetails("HREF1"));

		when(jsoupParserMock.get(anyString())).thenReturn(responseMock);
		when(responseMock.parse()).thenReturn(documentMock);
		
		doAnswer(anItemWithDescription("Item One Description"))
			.when(itemDetailsScraperMock).scrapeItemDetails(eq(documentMock), any(Item.class));

		final Item item = new Item();
		
		// When
		testSubject.scrapeItem(itemFragment, item);
		
		// Then
		assertThat(item, is(anActualItemWithDescription("Item One Description")));
	}
	
	private Matcher<Item> anActualItemWithDescription(String expectedDescription) {
		return hasProperty("description", is(expectedDescription));
	}

	private Matcher<Item> anActualItemWithDetailsByteSize(String expectedSize) {
		return hasProperty("detailsByteSize", is(expectedSize));
	}
	
	private Answer<Void> anItemWithDescription(final String description) {
		return new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Item item = (Item) invocation.getArguments()[1];

				item.setDescription(description);
				return null;
			}
		};
	}

	
	private static String readFile(String path) throws IOException {
		final InputStream stream = ItemScraperJUnitTest.class.getResourceAsStream(path);
		return IOUtils.toString(stream);
	}
	
	private String formatItemInjectingTitle(String title) {
		final String[] itemValues = Arrays.copyOf(defaultItemValues, defaultItemValues.length);
		itemValues[0] = title;
		
		return validItemFormat.format(itemValues);
	}
	
	private String formatItemInjectingPrice(String price) {
		final String[] itemValues = Arrays.copyOf(defaultItemValues, defaultItemValues.length);
		itemValues[1] = price;
		
		return validItemFormat.format(itemValues);
	}
	
	private String formatItemInjectingLinkToDetails(String href) {
		final String[] itemValues = Arrays.copyOf(defaultItemValues, defaultItemValues.length);
		itemValues[2] = href;
		
		return validItemFormat.format(itemValues);
	}
}
