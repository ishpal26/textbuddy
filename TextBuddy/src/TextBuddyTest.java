import static org.junit.Assert.*;

import org.junit.Test;

public class TextBuddyTest {
	
	
	@Test
	public void invalidCommand() {
		assertEquals(false, TextBuddy.checkValid("hello"));
		assertEquals(true, TextBuddy.checkValid("add"));
		assertEquals(true, TextBuddy.checkValid("display"));
		assertEquals(false, TextBuddy.checkValid("world"));
		assertEquals(true, TextBuddy.checkValid("exit"));
		assertEquals(true, TextBuddy.checkValid("delete"));
		assertEquals(false, TextBuddy.checkValid("negative"));
	}
	
	@Test
	public void checkValidArgs(){
		String[] arr = new String[0];
		assertEquals(false, TextBuddy.checkArgs(arr));
		arr = new String[3];
		assertEquals(true, TextBuddy.checkArgs(arr));
	}
	
	@Test
	public void invalidDelete(){
		assertEquals(false, TextBuddy.canDelete(-10, 3));
		assertEquals(false, TextBuddy.canDelete(-1, 3));
		assertEquals(true, TextBuddy.canDelete(1, 3));
		assertEquals(false, TextBuddy.canDelete(1000, 3));
	}

}
