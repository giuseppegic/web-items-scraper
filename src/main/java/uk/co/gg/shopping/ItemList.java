package uk.co.gg.shopping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * An ItemList consists of a list of items and a total price.
 * 
 * <p>ItemList can be serialized to JSON using Jackson. Json format sample can be found <a href="file:///D:/NetBeans/HMan/easy.txt">here</a>.</p>
 * 
 * @author GiuseppeG
 */
@JsonPropertyOrder({"results", "total"})
public class ItemList {
	
	@JsonProperty("results")
	private List<Item> items = new ArrayList<>();

	@JsonProperty("total")
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
