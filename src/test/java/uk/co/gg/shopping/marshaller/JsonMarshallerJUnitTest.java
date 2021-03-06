package uk.co.gg.shopping.marshaller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.gg.files.Reader.readFile;

import java.math.BigDecimal;

import org.junit.Test;

import uk.co.gg.shopping.Item;
import uk.co.gg.shopping.ItemList;


public class JsonMarshallerJUnitTest {

	private JsonMarshaller testSubject = new JsonMarshaller();
	
	@Test
	public void shouldMarshallItemListToProperCompactFormat() throws Exception{
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
		final String itemListJson = testSubject.marshall(itemList, false);
		
		// Then
		assertThat(itemListJson, is(readFile("test-item-list.json", JsonMarshallerJUnitTest.class)));
	}
	
	@Test
	public void shouldMarshallItemListToProperFormat() throws Exception{
		// Given
		final Item item1=new Item();
		item1.setTitle("Item 1 title");
		item1.setPrice(new BigDecimal("1.80"));
		item1.setDetailsByteSize("100kb");
		item1.setDescription("Item 1 description");
		
		final ItemList itemList = new ItemList();
		itemList.add(item1);
		
		// When
		final String itemListJson = testSubject.marshall(itemList, true);
		
		// Then
		assertThat(itemListJson, is(readFile("test-pretty-item-list.json", JsonMarshallerJUnitTest.class)));
	}
}
