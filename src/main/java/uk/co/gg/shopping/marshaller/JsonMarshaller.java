package uk.co.gg.shopping.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.gg.shopping.ItemList;

public class JsonMarshaller {

	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	public String marshall(ItemList itemList) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(itemList);
	}

}
