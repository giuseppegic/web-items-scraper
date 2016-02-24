package uk.co.gg.shopping.marshaller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import uk.co.gg.shopping.Item;
import uk.co.gg.shopping.ItemList;
import uk.co.gg.web.scrapers.shopping.ItemListScraperJUnitTest;

public class JsonMarshallerJUnitTest {

	private JsonMarshaller testSubject = new JsonMarshaller();
	
	@Test
	public void shouldMarshallItemListToProperFormat() throws Exception{
		// Given
		final Item item1=new Item();
		item1.setTitle("Item 1 title");
		item1.setPrice(new BigDecimal("1.80"));
		item1.setDetailsByteSize("100kb");
		item1.setDescription("Item 1 description");
		
		final Item item2=new Item();
		item2.setTitle("Item 2 title");
		item2.setPrice(new BigDecimal("2.00"));
		item2.setDetailsByteSize("200.5kb");
		item2.setDescription("Item 2 description");
		
		final ItemList itemList = new ItemList();
		itemList.add(item1);
		itemList.add(item2);
		
		// When
		final String itemListJson = testSubject.marshall(itemList);
		
		// Then
		assertThat(itemListJson, is(readFile("test-item-list.json")));
	}
	
	
	private static String readFile(String path) throws IOException {
		final InputStream stream = JsonMarshallerJUnitTest.class.getResourceAsStream(path);
		return IOUtils.toString(stream);
	}
}
