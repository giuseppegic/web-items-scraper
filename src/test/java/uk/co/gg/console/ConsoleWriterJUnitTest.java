package uk.co.gg.console;

import static org.mockito.Mockito.verify;

import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleWriterJUnitTest {

	private static final String TEST_MESSAGE = "TEST_MESSAGE";

	@Mock
	private PrintStream writerMock;
	
	private ConsoleWriter testSubject;
	
	@Before
	public void setup(){
		testSubject = new ConsoleWriter(writerMock);
	}
	
	@Test
	public void shouldPrintMessage(){
		
		// When
		testSubject.printLine(TEST_MESSAGE);
		
		// Then
		verify(writerMock).println(TEST_MESSAGE);
	}
	
	@Test
	public void shouldPrintBlankLine(){
		
		// When
		testSubject.printLine();
		
		// Then
		verify(writerMock).println();
	}
}
