package uk.co.gg.web.scrapers.shopping.application;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
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
import uk.co.gg.shopping.ItemList;
import uk.co.gg.shopping.marshaller.JsonMarshaller;
import uk.co.gg.web.parser.jsoup.JsoupParser;
import uk.co.gg.web.scrapers.InvalidStructureException;
import uk.co.gg.web.scrapers.shopping.ShoppingWebScraper;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingWebScraperAppJunitTest {

	private static final String LIST_ITEM_URL = "5_products.html";

	private static final ItemList TEST_ITEM_LIST = new ItemList();

	private static final String TEST_ITEM_LIST_JSON_COMPACT = "TEST_ITEM_LIST_JSON_COMPACT";
	
	private static final String TEST_ITEM_LIST_JSON_INDENTED = "TEST_ITEM_LIST_JSON_INDENTED";

	private static final String TEST_ERROR_MSG = "TEST_ERROR_MSG";

	private static final String TEST_HTML_FRAGMENT = "TEST_HTML_FRAGMENT";

	@Mock
	private JsoupParser jsoupParserMock;

	@Mock
	private ConsoleWriter consoleMock;

	@Mock
	private ShoppingWebScraper shoppingWebScraperMock;

	@Mock
	private JsonMarshaller jsonMarshallerMock;

	@Captor
	private ArgumentCaptor<String> messageCaptor;

	private Injector injector;

	private ShoppingWebScraperApp testSubject;

	@Before
	public void setup() throws IOException {
		injector = Guice.createInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(JsoupParser.class).toInstance(jsoupParserMock);
				binder.bind(ShoppingWebScraper.class).toInstance(shoppingWebScraperMock);
				binder.bind(JsonMarshaller.class).toInstance(jsonMarshallerMock);
				binder.bind(ConsoleWriter.class).toInstance(consoleMock);
			}
		});

		mockJsoupParser();
		
		testSubject = injector.getInstance(ShoppingWebScraperApp.class);
	}

	@Test
	public void shouldPrintUsageWhenGivenTooFewArguments() throws Exception {
		// When
		testSubject.start(new String[0]);

		// Then
		verify(consoleMock).printLine(startsWith("Usage"));
	}

	@Test
	public void shouldPrintUsageWhenGivenTooManyArguments() throws Exception {
		// When
		testSubject.start(new String[3]);

		// Then
		verify(consoleMock).printLine(startsWith("Usage"));
	}

	@Test
	public void shouldSignalCompactMode() throws Exception {
		// When
		testSubject.start(new String[] { LIST_ITEM_URL, "true" });

		// Then
		verify(consoleMock, atLeast(1)).printLine(messageCaptor.capture());

		final List<String> messages = messageCaptor.getAllValues();
		assertThat(messages, hasItem(containsString("JSON will be generated in compact format.")));
	}

	@Test
	public void shouldSignalErrorInCompactParameter() throws Exception {
		// When
		testSubject.start(new String[] { LIST_ITEM_URL, "trfue" });

		// Then
		verify(consoleMock, atLeast(1)).printLine(messageCaptor.capture());

		final List<String> messages = messageCaptor.getAllValues();
		assertThat(messages, hasItem(containsString("Invalid value")));
	}

	@Test
	public void shouldPrintBannerWithValidUsage() throws Exception {
		// When
		testSubject.start(new String[] { LIST_ITEM_URL, "trfue" });

		// Then
		verify(consoleMock, atLeast(1)).printLine(messageCaptor.capture());

		final List<String> messages = messageCaptor.getAllValues();
		assertThat(messages, hasItem(containsString("=== ShoppingWebScraper - "+ShoppingWebScraperAppJunitTest.class.getPackage().getImplementationVersion()+" ===")));
	}
	
	@Test
	public void shouldPrintBannerWithInvalidUsage() throws Exception {
		// When
		testSubject.start(new String[0]);

		// Then
		verify(consoleMock, atLeast(1)).printLine(messageCaptor.capture());

		final List<String> messages = messageCaptor.getAllValues();
		assertThat(messages, hasItem(containsString("=== ShoppingWebScraper - "+ShoppingWebScraperAppJunitTest.class.getPackage().getImplementationVersion()+" ===")));
	}
	
	@Test
	public void shouldPrintScraperItemListWhenDefaultingIndentedMode() throws Exception {
		// Given
		when(shoppingWebScraperMock.scrapeItemListPage(anyString())).thenReturn(TEST_ITEM_LIST);
		when(jsonMarshallerMock.marshall(TEST_ITEM_LIST, true)).thenReturn(TEST_ITEM_LIST_JSON_COMPACT);
		
		// When
		testSubject.start(new String[]{LIST_ITEM_URL});

		// Then
		verify(consoleMock, atLeast(1)).printLine(messageCaptor.capture());

		final List<String> messages = messageCaptor.getAllValues();
		assertThat(messages, hasItem(TEST_ITEM_LIST_JSON_COMPACT));
	}
	
	@Test
	public void shouldPrintScraperItemListWhenKeepingIndentedMode() throws Exception {
		// Given
		when(shoppingWebScraperMock.scrapeItemListPage(anyString())).thenReturn(TEST_ITEM_LIST);
		when(jsonMarshallerMock.marshall(TEST_ITEM_LIST, true)).thenReturn(TEST_ITEM_LIST_JSON_INDENTED);
		
		// When
		testSubject.start(new String[]{LIST_ITEM_URL, "false"});

		// Then
		verify(consoleMock, atLeast(1)).printLine(messageCaptor.capture());

		final List<String> messages = messageCaptor.getAllValues();
		assertThat(messages, hasItem(TEST_ITEM_LIST_JSON_INDENTED));
	}
	
	@Test
	public void shouldPrintScraperItemListWhenSpecifyingCompactMode() throws Exception {
		// Given
		when(shoppingWebScraperMock.scrapeItemListPage(anyString())).thenReturn(TEST_ITEM_LIST);
		when(jsonMarshallerMock.marshall(TEST_ITEM_LIST, true)).thenReturn(TEST_ITEM_LIST_JSON_COMPACT);
		
		// When
		testSubject.start(new String[]{LIST_ITEM_URL, "false"});

		// Then
		verify(consoleMock, atLeast(1)).printLine(messageCaptor.capture());

		final List<String> messages = messageCaptor.getAllValues();
		assertThat(messages, hasItem(TEST_ITEM_LIST_JSON_COMPACT));
	}

	@Test
	public void shouldPrintErrorMessageWhenItemListStructureIsNotRecognised() throws Exception {
		// Given
		when(shoppingWebScraperMock.scrapeItemListPage(anyString())).thenThrow(new InvalidStructureException(TEST_ERROR_MSG, TEST_HTML_FRAGMENT));
		when(jsonMarshallerMock.marshall(TEST_ITEM_LIST, true)).thenReturn(TEST_ITEM_LIST_JSON_COMPACT);
		
		// When
		testSubject.start(new String[]{LIST_ITEM_URL, "false"});

		// Then
		verify(consoleMock, atLeast(1)).printLine(messageCaptor.capture());

		final List<String> messages = messageCaptor.getAllValues();
		assertThat(messages, hasItem(containsString(TEST_ERROR_MSG)));
	}
	
	private void mockJsoupParser() throws IOException {
		final InputStream testHtmlDirectoryStream = ShoppingWebScraperAppJunitTest.class
				.getResourceAsStream("test-html");
		final List<String> testHtmlFilenames = IOUtils.readLines(testHtmlDirectoryStream, Charsets.UTF_8);

		for (final String testHtmlFilename : testHtmlFilenames) {
			doAnswer(new Answer<Response>() {

				@Override
				public Response answer(InvocationOnMock invocation) throws Throwable {

					// For each testHtmlFile mock Jsoup classes to return the
					// correct html fragment
					final Response testResponseMock = Mockito.mock(Response.class);
					final String testHtml = readFile("test-html/" + testHtmlFilename,
							ShoppingWebScraperAppJunitTest.class);
					final Document testDocument = Jsoup.parse(testHtml);

					when(testResponseMock.parse()).thenReturn(testDocument);
					when(testResponseMock.bodyAsBytes()).thenReturn(testHtml.getBytes());

					return testResponseMock;
				}
			}).when(jsoupParserMock).get(contains(testHtmlFilename));
		}

	}
}
