package uk.co.gg.files;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Reader {
	
	/**
	 * Read a file located in a given path related to the test class location. 
	 * @param path the file to read.
	 * @param relatedClass the class indicating the base path.
	 * @return the content of the file.
	 * @throws IOException when an error occured while reading the file.
	 */
	public static String readFile(String path, Class<?> relatedClass) throws IOException {
		final InputStream stream = relatedClass.getResourceAsStream(path);
		return IOUtils.toString(stream);
	}
}
