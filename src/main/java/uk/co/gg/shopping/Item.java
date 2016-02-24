package uk.co.gg.shopping;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * An Item consists of a title, a price, a size and a description.
 * 
 * @author GiuseppeG
 */
@JsonPropertyOrder({"title", "size", "unit_price", "description"})
public class Item {

	private String title;
	
	private String description;
	
	@JsonProperty("size")
	private String detailsByteSize;
	
	@JsonProperty("unit_price")
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDetailsByteSize() {
		return detailsByteSize;
	}

	public void setDetailsByteSize(String detailsByteSize) {
		this.detailsByteSize = detailsByteSize;
	}
}
