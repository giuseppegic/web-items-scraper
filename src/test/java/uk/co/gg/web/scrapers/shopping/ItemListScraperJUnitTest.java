package uk.co.gg.web.scrapers.shopping;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static uk.co.gg.files.Reader.readFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jsoup.Jsoup;
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
import uk.co.gg.shopping.ItemList;

@RunWith(MockitoJUnitRunner.class)
public class ItemListScraperJUnitTest {

	private static MessageFormat validItemFormat;

	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Mock
	private ItemScraper itemScraperMock;

	private ItemListScraper testSubject;

	@BeforeClass
	public static void setup() throws IOException {
		validItemFormat = new MessageFormat(readFile("valid-item-list-format.html", ItemListScraperJUnitTest.class));
	}

	@Before
	public void initTestSubject() {
		testSubject = new ItemListScraper(itemScraperMock);
	}

	@Test
	public void shouldExtractListOfItems() throws Exception {
		// Given
		final Element itemListFragment = Jsoup.parseBodyFragment(formatItemListInjectingItem("item1", "item2"));
		
		doAnswer(anItemWithTitle("Item One"))
			.when(itemScraperMock).scrapeItem(argThat(anElementWithText("item1")), any(Item.class));
		doAnswer(anItemWithTitle("Item Two"))
			.when(itemScraperMock).scrapeItem(argThat(anElementWithText("item2")), any(Item.class));

		// When
		final ItemList itemList = testSubject.scrapeItemList(itemListFragment);

		// Then
		assertThat(itemList.getItems(), hasSize(2));
		assertThat(itemList.getItems(),
				contains(
						anActualItemWithTitle("Item One"), 
						anActualItemWithTitle("Item Two")));
	}
	
	@Test
	public void shouldReturnEmptyListIfNoProductIsPresent() throws Exception {
		// Given
		final Element itemListFragment=Jsoup.parseBodyFragment(readFile("item-list-with-no-item.html", ItemListScraperJUnitTest.class));
		
		// When
		final ItemList itemList = testSubject.scrapeItemList(itemListFragment);

		// Then
		assertThat(itemList.getItems(), hasSize(0));
	}

	private Matcher<Item> anActualItemWithTitle(String title) {
		return hasProperty("title", is(title));
	}

	private Answer<Void> anItemWithTitle(final String title) {
		return new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Item item = (Item) invocation.getArguments()[1];

				item.setPrice(new BigDecimal("1.15"));
				item.setTitle(title);
				return null;
			}
		};
	}

	private Matcher<Element> anElementWithText(final String expectedText) {
		return new BaseMatcher<Element>() {

			@Override
			public boolean matches(Object actual) {
				if (actual == null || !(actual instanceof Element)) {
					return false;
				}
				final Element actualElement = (Element) actual;

				if (!actualElement.ownText().equals(expectedText)) {
					return false;
				}

				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("expected text: " + expectedText);
			}
		};
	}

	private String formatItemListInjectingItem(String... itemFragments) {
		return validItemFormat.format(itemFragments);
	}
}
