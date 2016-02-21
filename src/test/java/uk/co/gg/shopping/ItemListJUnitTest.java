package uk.co.gg.shopping;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ItemListJUnitTest {
	
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	private ItemList testSubject = new ItemList();
	
	
	@Test
	public void shouldAddItem(){
		// Given
		final Item item = new Item();
		item.setPrice(new BigDecimal("1.20"));
		
		// When
		testSubject.add(item);
		
		// Then
		assertThat(testSubject.getItems(), contains(item));
	}
	
	@Test
	public void shouldThrowIllegalStateExceptionIfAddedItemHasNoPrice(){
		// Expect
		expected.expect(IllegalStateException.class);
		expected.expectMessage("Any item must have a price");
		
		// Given
		final Item item = new Item();
		
		// When
		testSubject.add(item);
	}
	
	@Test
	public void shouldIgnoreAddedItemIfNull(){
		// When
		testSubject.add(null);
		
		// Then
		assertThat(testSubject.getItems(), hasSize(0));
	}
	
	@Test
	public void shouldHaveInitalTotalPriceAsZero(){
		// When
		BigDecimal totalPrice = testSubject.getTotalPrice();
		
		// Then
		assertThat(totalPrice, is(new BigDecimal("0.00")));
	}
	
	@Test
	public void shouldUpdateTotalPrice(){
		// Given
		final Item item1 = new Item();
		item1.setPrice(new BigDecimal("1.30"));
		
		final Item item2 = new Item();
		item2.setPrice(new BigDecimal("2.50"));
		
		// When
		testSubject.add(item1);
		testSubject.add(item2);
		
		// Then
		assertThat(testSubject.getTotalPrice(), is(new BigDecimal("3.80")));
	}
}
