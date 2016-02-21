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
	public static final String TEXT8 = "Arsenal";
	public static final String TESTFILENAME = "file.txt";
	
	@BeforeClass
	public static void setUpBeforeClass() throws ClassNotFoundException, IOException{
		String[] argArray = new String[1];
		argArray[0] = TESTFILENAME;
		TextBuddy.checkFile(argArray);
	}
	
	@Test
	public void invalidCommand() {
		assertEquals(false, TextBuddy.checkValid("hello"));
		assertEquals(true, TextBuddy.checkValid("add"));
		assertEquals(true, TextBuddy.checkValid("display"));
		assertEquals(false, TextBuddy.checkValid("world"));
		assertEquals(true, TextBuddy.checkValid("exit"));
		assertEquals(true, TextBuddy.checkValid("delete"));
		assertEquals(false, TextBuddy.checkValid("negative"));
		assertEquals(false, TextBuddy.checkValid(""));
	}
	
	@Test
	public void checkValidArgs(){
		String[] arr = new String[0];
		assertEquals(false, TextBuddy.checkArgs(arr));
		arr = new String[3];
		assertEquals(true, TextBuddy.checkArgs(arr));
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
	public void testAdd() throws Exception{
		TextBuddy.executeCommand("clear");
		assertEquals("added to file.txt: \""+TEXT1+"\"", TextBuddy.executeCommand("add "+ TEXT1));
		assertEquals("added to file.txt: \""+TEXT2+"\"", TextBuddy.executeCommand("add "+ TEXT2));
		assertEquals("added to file.txt: \""+TEXT3+"\"", TextBuddy.executeCommand("add "+ TEXT3));
		assertEquals(3, TextBuddy.getLineCount());
		assertEquals("Error: Duplicate content \""+TEXT3+"\" detected", TextBuddy.executeCommand("add " + TEXT3));
		assertEquals(3, TextBuddy.getLineCount());
	}	
	
	@Test
	public void testDelete() throws Exception{
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
		assertFalse(TextBuddy.canDelete(-10, 3));
		assertFalse(TextBuddy.canDelete(-1, 3));
		assertTrue(TextBuddy.canDelete(1, 3));
		assertFalse(TextBuddy.canDelete(1000, 3));
		assertTrue(TextBuddy.canDelete(2, 3));
		assertFalse(TextBuddy.canDelete(0, 3));
	}
	
	@Test
	public void testSort(){
		//TextBuddy.sortList("");
		ArrayList<String> testList = new ArrayList();
		testList.add(TEXT1);
		testList.add(TEXT2);
		testList.add(TEXT3);
		testList.add(TEXT4);
		testList.add(TEXT5);
		testList.add(TEXT6);
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
