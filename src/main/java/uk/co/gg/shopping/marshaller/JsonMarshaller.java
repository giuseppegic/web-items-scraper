package uk.co.gg.shopping.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.gg.shopping.ItemList;

/**
 * Wrapper around Jackson's ObjectMapper to handle it as a singleton.
 * 
 * @author GiuseppeG.
 *
 */
public class JsonMarshaller {

	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	/**
	 * Serialize the given item list to JSON.
	 * @param itemList the item list to serialize.
	 * @param prettyPrint if false generate a compact JSON, otherwise it generates an indented one.
	 * @return the serialized list.
	 * @throws JsonProcessingException if an error occurs while trying to serialize the list.
	 */
	public String marshall(ItemList itemList, boolean prettyPrint) throws JsonProcessingException {
		if(prettyPrint){
			return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(itemList);
		} else {
			return jsonMapper.writeValueAsString(itemList);
		}
	}

}
