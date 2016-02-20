package uk.co.gg.shopping.item;

import java.math.BigDecimal;

/**
 * An Item consists of a title and a price.
 * 
 * @author GiuseppeG
 */
public class Item {

	private String title;
	
	private BigDecimal price;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
