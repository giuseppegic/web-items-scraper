package uk.co.gg.console;

import java.io.PrintStream;

import javax.inject.Named;

/**
 * Component to ease testing of console output.
 * 
 * @author GiuseppeG
 *
 */
@Named
public class ConsoleWriter {

	private PrintStream writer;

	public ConsoleWriter(PrintStream writer) {
		this.writer = writer;
	}

	public ConsoleWriter() {
		this.writer = System.out;
	}
	
	public void printLine(String message) {
		writer.println(message);
	}

	public void printLine() {
		writer.println();
	}

}
