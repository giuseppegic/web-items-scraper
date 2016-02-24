package uk.co.gg.web.parser.jsoup;

import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class JsoupParser {

	public Response get(String href) throws IOException{
		return Jsoup.connect(href).execute();
	}
	
}
