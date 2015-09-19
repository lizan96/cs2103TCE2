import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class TextBuddyTest {
	

	public void add() throws IOException{
		TextBuddy.executeCommand("add lala");
	    TextBuddy.executeCommand("add heihei");
	    TextBuddy.executeCommand("add haha");
	}
	
	@Test
	public void addTest() throws IOException{
		TextBuddy.fileName = "2.txt";
		TextBuddy.readFile(TextBuddy.fileName);
		assertEquals("added to 2.txt: “lala”",
				    TextBuddy.executeCommand("add lala")); 
		assertEquals("added to 2.txt: “heihei”",
			    TextBuddy.executeCommand("add heihei")); 
	    assertEquals("added to 2.txt: “haha”",
			    TextBuddy.executeCommand("add haha")); 
	}
	
	@Test
	public void clearDisplayTest() throws IOException {
		TextBuddy.fileName = "2.txt";
		TextBuddy.readFile(TextBuddy.fileName);
		assertEquals("all content deleted from 2.txt",
                TextBuddy.executeCommand("clear"));
		assertEquals("2.txt is empty",
                TextBuddy.executeCommand("display"));
		add();
		assertEquals("1. lala"+ "\n" + "2. heihei" + "\n"  + "3. haha", 
			    TextBuddy.executeCommand("display"));
	}
	
	@Test
	public void deleteTest() throws IOException{
		TextBuddy.fileName = "2.txt";
		TextBuddy.executeCommand("clear");
		add();
		assertEquals("1. lala"+ "\n" + "2. heihei" + "\n"  + "3. haha", 
			    TextBuddy.executeCommand("display"));
		assertEquals("deleted from 2.txt: “heihei”", 
			    TextBuddy.executeCommand("delete 2"));
		assertEquals("1. lala"+ "\n"  + "2. haha", 
			    TextBuddy.executeCommand("display"));
	}
	
	
	@Test
	public void sortTest() throws IOException {
		TextBuddy.executeCommand("clear");
		addTest();
		assertEquals("all content from 2.txt has been sorted", 
			    TextBuddy.executeCommand("sort"));
		assertEquals("1. haha"+ "\n" + "2. heihei" + "\n"  + "3. lala", 
			    TextBuddy.executeCommand("display"));
	}
	
	@Test
	public void searchTest() throws IOException{
		TextBuddy.executeCommand("clear");
		addTest();
		assertEquals("2. heihei" + "\n" + "3. haha", 
			    TextBuddy.executeCommand("search h"));
	}
	
	@Test
	public void searchFailedTest() throws IOException{
		TextBuddy.fileName = "2.txt";
		TextBuddy.executeCommand("clear");
		add();
		assertEquals("2.txt does not contain none", 
			    TextBuddy.executeCommand("search none"));
	}
	
}
