package com.gg.shopping.starter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.gg.shopping.ItemList;
import uk.co.gg.web.parser.jsoup.JsoupParser;
import uk.co.gg.web.scrapers.shopping.ItemListScraper;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingWebScraperJUnitTest {

	private static final String ITEM_LIST_URL = "ITEM_LIST_URL";

	private static final ItemList EXPECTED_ITEM_LIST = null;

	@Mock
	private ItemListScraper itemListScraperMock;
	
	@Mock
	private JsoupParser jsoupParserMock;
	
	@Mock
	private Document documentMock;
	
	@Mock
	private Response responseMock;
	
	private ShoppingWebScraper testSubject;
	
	@Before
	public void setupTestSubject(){
		testSubject = new ShoppingWebScraper(jsoupParserMock, itemListScraperMock);
	}
	
	@Test
	public void shouldScrapeItemListFromURL() throws Exception{
		// Given
		when(jsoupParserMock.get(ITEM_LIST_URL)).thenReturn(responseMock);
		when(responseMock.parse()).thenReturn(documentMock);
		when(itemListScraperMock.scrapeItemList(documentMock)).thenReturn(EXPECTED_ITEM_LIST);
		
		// When
		final ItemList itemList = testSubject.scrapeItemListPage(ITEM_LIST_URL);
		
		// Then
		assertThat(itemList, is(EXPECTED_ITEM_LIST));
	}
}
