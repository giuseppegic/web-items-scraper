package uk.co.gg.web.scrapers.shopping.application;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static uk.co.gg.files.Reader.readFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import uk.co.gg.console.ConsoleWriter;
import uk.co.gg.web.parser.jsoup.JsoupParser;
import uk.co.gg.web.scrapers.shopping.ShoppingWebScraper;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingWebScraperAppIntegrationTest {

	private static final String LIST_ITEM_URL = "5_products.html";
	
	@Mock
	private JsoupParser jsoupParserMock;
	
	@Mock
	private ConsoleWriter consoleMock;
	
	@Captor
	private ArgumentCaptor<String> messageCaptor;
	
	private Injector injector;

	private ShoppingWebScraperApp testSubject;
	
	@Before
	public void setup() throws IOException{
		injector = Guice.createInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(JsoupParser.class).toInstance(jsoupParserMock);
				binder.bind(ConsoleWriter.class).toInstance(consoleMock);
			}
		});
		
		mockJsoupParser();
		
		testSubject = injector.getInstance(ShoppingWebScraperApp.class);
	}
	
	@Test
	public void shouldInjectShoppingWebScraper(){
		// When
		final ShoppingWebScraper shoppingWebScraper = injector.getInstance(ShoppingWebScraper.class);
		
		// Then
		assertThat(shoppingWebScraper, is(not(nullValue())));
	}
	
	@Test
	public void shouldParseTestItemListAndGenerateCompactJson() throws Exception{
		// When
		final String itemListJson = testSubject.scrapeItemListPageToJson(LIST_ITEM_URL, false);
		
		// Then
		assertThat(itemListJson, is(readFile("itest-item-list.json", ShoppingWebScraperAppIntegrationTest.class)));
	}
	
	private void mockJsoupParser() throws IOException {
		final InputStream testHtmlDirectoryStream = ShoppingWebScraperAppIntegrationTest.class.getResourceAsStream("test-html");
		final List<String> testHtmlFilenames = IOUtils.readLines(testHtmlDirectoryStream, Charsets.UTF_8);
		
		for(final String testHtmlFilename:testHtmlFilenames){
			doAnswer(new Answer<Response>() {

				@Override
				public Response answer(InvocationOnMock invocation) throws Throwable {
					
					// For each testHtmlFile mock Jsoup classes to return the correct html fragment
					final Response testResponseMock = Mockito.mock(Response.class);
					final String testHtml = readFile("test-html/"+testHtmlFilename, ShoppingWebScraperAppIntegrationTest.class);
					final Document testDocument = Jsoup.parse(testHtml );
					
					when(testResponseMock.parse()).thenReturn(testDocument);
					when(testResponseMock.bodyAsBytes()).thenReturn(testHtml.getBytes());
					
					return testResponseMock;
				}
			}).when(jsoupParserMock).get(contains(testHtmlFilename));
		}
		
	}
}
