import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

public class TextBuddyTest {
	public static final String TEXT1 = "Jam and bread";
	public static final String TEXT2 = "Oranges";
	public static final String TEXT3 = "Hello";
	public static final String TEXT4 = "Night time";
	public static final String TEXT5 = "Cookies n cream";
	public static final String TEXT6 = "Elephant";
	public static final String TEXT7 = "Night hawk";
	public static final String TEXT8 = "Apple";
	public static final String TESTFILENAME = "file.txt";
	
	@BeforeClass
	public static void setUpBeforeClass() throws ClassNotFoundException, IOException{
		String[] argArray = new String[1];
		argArray[0] = TESTFILENAME;
		TextBuddy.checkFile(argArray);
	}
	
	@Test
	public void checkValidArgs(){
		String[] arr = new String[0];
		assertEquals(false, TextBuddy.checkArgs(arr));
		arr = new String[3];
		assertEquals(true, TextBuddy.checkArgs(arr));
	}
	
	@Test
	public void testAdd() throws Exception{
		TextBuddy.executeCommand("clear");
		assertEquals("Error: Unrecognized command \"add  \"", TextBuddy.addEntry("add  "));
		assertEquals("added to file.txt: \""+TEXT1+"\"", TextBuddy.addEntry("add " + TEXT1));
		assertEquals("added to file.txt: \""+TEXT2+"\"", TextBuddy.addEntry("add " + TEXT2));
		assertEquals("added to file.txt: \""+TEXT3+"\"", TextBuddy.addEntry("add " + TEXT3));
		assertEquals(3, TextBuddy.getLineCount());
		assertEquals("Error: Duplicate content \""+TEXT3+"\" detected", TextBuddy.addEntry("add " +TEXT3));
		assertEquals(3, TextBuddy.getLineCount());
	}	
	
	@Test
	public void testClear() throws IOException{
		assertEquals("all content deleted from file.txt", TextBuddy.executeCommand("clear"));
		TextBuddy.executeCommand("add" + TEXT1);
		assertEquals("all content deleted from file.txt", TextBuddy.executeCommand("clear"));
		assertEquals("Error: Unrecognized command \"" +"clearer" + "\"", TextBuddy.executeCommand("clearer"));
		assertEquals(0, TextBuddy.getLineCount());
	}
	
	@Test
	public void testDelete() throws Exception{
		testClear();
		assertEquals(0,TextBuddy.getLineCount());
		assertEquals("file.txt is empty", TextBuddy.deleteEntry("4"));
		addLines();
		assertEquals(8, TextBuddy.getLineCount());
		assertEquals("Error: Invalid Deletion Index", TextBuddy.deleteEntry("0"));
		assertEquals("Error: Invalid Deletion Index", TextBuddy.deleteEntry("9"));
		assertEquals("Error: Invalid Deletion Index", TextBuddy.deleteEntry("-1"));
		assertEquals("Error: Invalid Deletion Index", TextBuddy.executeCommand("delete"));
		assertEquals("deleted from file.txt: \""+TEXT3+"\"",TextBuddy.executeCommand("delete 3"));
		assertEquals(7, TextBuddy.getLineCount());
	}
	
	@Test
	public void invalidDelete(){
		assertFalse(TextBuddy.validDeleteIndex(-10, 3));
		assertFalse(TextBuddy.validDeleteIndex(-1, 3));
		assertTrue(TextBuddy.validDeleteIndex(1, 3));
		assertFalse(TextBuddy.validDeleteIndex(1000, 3));
		assertTrue(TextBuddy.validDeleteIndex(2, 3));
		assertFalse(TextBuddy.validDeleteIndex(0, 3));
	}
	
	@Test
	public void testSort() throws Exception{
		assertEquals(0, TextBuddy.getLineCount());
		addLines();
		TextBuddy.sortList();
		assertEquals(TEXT8, TextBuddy.getListContent(0));
		assertEquals(TEXT5, TextBuddy.getListContent(1));
		assertEquals(TEXT6, TextBuddy.getListContent(2));
		assertEquals(TEXT3, TextBuddy.getListContent(3));
		assertEquals(TEXT1, TextBuddy.getListContent(4));
		assertEquals(TEXT7, TextBuddy.getListContent(5));
		assertEquals(TEXT4, TextBuddy.getListContent(6));
		assertEquals(TEXT2, TextBuddy.getListContent(7));
		assertEquals(8, TextBuddy.getLineCount());
	}
	
	@Test
	public void testSearch(){
		
	}

	public void addLines() throws Exception{
		TextBuddy.executeCommand("add " + TEXT1 );
		TextBuddy.executeCommand("add " + TEXT2 );
		TextBuddy.executeCommand("add " + TEXT3 );
		TextBuddy.executeCommand("add " + TEXT4 );
		TextBuddy.executeCommand("add " + TEXT5 );
		TextBuddy.executeCommand("add " + TEXT6 );
		TextBuddy.executeCommand("add " + TEXT7 );
		TextBuddy.executeCommand("add " + TEXT8 );
	}

}
