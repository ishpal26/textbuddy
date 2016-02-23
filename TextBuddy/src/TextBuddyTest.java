import static org.junit.Assert.*;

import java.io.IOException;

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
	
	public static final String SORTED_LIST = "1. Apple\n2. Cookies n cream\n3. Elephant\n4. Hello\n5. Jam and bread\n"
											  + "6. Night hawk\n7. Night time\n8. Oranges\n";
	public static final String SEARCH_RESULT ="1. Night time\n2. Night hawk\n";
	
	
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
		assertEquals("all content deleted from file.txt", TextBuddy.clearList("clear"));
		TextBuddy.executeCommand("add" + TEXT1);
		assertEquals("all content deleted from file.txt", TextBuddy.clearList("clear"));
		assertEquals("Error: Unrecognized command \"" +"clearer" + "\"", TextBuddy.executeCommand("clearer"));
		assertEquals(0, TextBuddy.getLineCount());
	}
	
	@Test
	public void testDelete() throws Exception{
		testClear();
		assertEquals(0,TextBuddy.getLineCount());
		assertEquals("file.txt is empty", TextBuddy.deleteEntry("delete 4"));
		addLines();
		assertEquals(8, TextBuddy.getLineCount());
		assertEquals("Error: Invalid Deletion Index", TextBuddy.deleteEntry("delete 0"));
		assertEquals("Error: Invalid Deletion Index", TextBuddy.deleteEntry("delete 9"));
		assertEquals("Error: Invalid Deletion Index", TextBuddy.deleteEntry("delete -1"));
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
		testClear();
		assertEquals(TESTFILENAME +" is empty",TextBuddy.sortList());
		assertEquals(0, TextBuddy.getLineCount());
		addLines();
		assertEquals(SORTED_LIST,TextBuddy.sortList());
		assertEquals(8, TextBuddy.getLineCount());
	}
	
	@Test  
	public void testSearch() throws IOException{
		assertEquals(SEARCH_RESULT, TextBuddy.searchList("search night"));
		assertEquals("\"cs2103\" not found in list", TextBuddy.searchList("search cs2103"));
		assertEquals("Error: Unrecognized command \"search \"", TextBuddy.searchList("search "));
		testClear();
		assertEquals(TESTFILENAME +" is empty", TextBuddy.searchList("Hello"));
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