package uk.co.gg.shopping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemList {

	private List<Item> items = new ArrayList<>();

	private BigDecimal totalPrice = new BigDecimal("0.00");

	public void add(Item item) {
		if (item != null) {
			final BigDecimal itemPrice = item.getPrice();

			if (itemPrice == null) {
				throw new IllegalStateException("Any item must have a price");
			}

			items.add(item);
			totalPrice = totalPrice.add(itemPrice);
		}
	}

	public List<Item> getItems() {
		return items;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
}
