package uk.co.gg.web.parser.jsoup;

import java.io.IOException;

import javax.inject.Named;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * Wrapper around Jsoup to facilitate testing Jsoup static methods.
 * 
 * @author GiuseppeG
 *
 */
@Named
public class JsoupParser {

	public Response get(String href) throws IOException{
		return Jsoup.connect(href).execute();
	}
	
}
