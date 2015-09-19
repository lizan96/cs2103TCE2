import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 *  This class is used to store and retrieve the text that the user adds in.
 * The user can create a file with any name. If the file name has already existed, then all the 
 * following action will be done on the existed file. Otherwise, a new file will be created.
 * The user can add, delete, clear, sort and search the texts as well as see the texts by display it.
 * The user will receive a feedback message after each command.
 * The file will be saved after every action.
 * 
 * The command format is given by the example interaction below:

    Welcome to TextBuddy. 2.txt is ready for use
	command:add 123
	added to 2.txt: “123”
	command:add 456
	added to 2.txt: “456”
	command:add 789
	added to 2.txt: “789”
	command:display
	1. 123
	2. 456
	3. 789
	command:delete 2
	deleted from 2.txt: “456”
	command:display
	1. 123
	2. 789
	command: add 133
	added to 2.txt: "133"
	command: search 3
	1. 123
	3. 133
	command: sort
	all content from 2.txt has been sorted
	command: display
	1. 123
	2. 133
	3. 789
	command:clear
	all content deleted from 2.txt
	command:display
	2.txt is empty
	command:exit
 * @author Li Zan
 *
 */
public class TextBuddy {
	// standard text message
	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	
	private static final String ENTER_COMMAND = "command:";
	private static final String NO_INPUT_FILE_NAME = "NO_INPUT_FILE_NAME";
	
	private static final String TEXT_ADDED = "added to %1$s: “%2$s”";
	private static final String TEXT_DELETED = "deleted from %1$s: “%2$s”";
	private static final String TEXT_CLEARED = "all content deleted from %1$s";
	private static final String TEXT_EMPTY = "%1$s is empty";
	private static final String TEXT_SORTED = "all content from %1$s has been sorted";
	private static final String SEARCH_FAILED = "%1$s does not contain %2$s";
	
	
	private static ArrayList<String> allText = new ArrayList<>();
	static String fileName;
	private static String linesContainPara;
	
	// These are the possible command types
	enum COMMAND_TYPE {
		ADD, DISPLAY, DELETE, CLEAR, SORT, SEARCH, EXIT, INVALID
	};
	
	private static Scanner scanner = new Scanner(System.in);
	

	/**
	 * Check whether the file with the specific file name has existed.
	 * If so, read the file. 
	 * Otherwise create a new file.
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	static void readFile(String fileName) throws FileNotFoundException{
		File file = new File(fileName); 
		try {
			file.createNewFile(); 
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		
		Scanner scanFile = new Scanner(file);
		String newLine;
		
		while (scanFile.hasNext()) {
			newLine = removeFirstWord(scanFile.nextLine());  // delete the index number
			if (!newLine.isEmpty()){
				allText.add(newLine);
			}
		}
		scanFile.close(); 
	}
	
	/**
	 * Save the texts in the ArrayList to the file in the disk.
	 * Add the index number for each line.
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	private static void saveTextToFile(String fileName) throws IOException{
		PrintWriter writer = new PrintWriter(fileName);
		
		int textIndex;
		for (int i = 0; i < allText.size(); i++){
			textIndex = i + 1; 
			writer.print(textIndex + ". ");
			writer.println(allText.get(i)); 
		}
		writer.close(); 
	}
	
	static String readFileName(String[] Args) throws IOException {
		if (Args.length == 0) {
			showToUser(NO_INPUT_FILE_NAME);
			System.exit(0);
		} else {
			fileName = Args[0];	
		}
		return fileName;
	}
	
	public static void main(String[] args) throws IOException{
		fileName = readFileName(args);
		readFile(fileName);
		String welcome = String.format(WELCOME_MESSAGE, fileName);
		showToUser(welcome);
		while (true) {
			String userCommand = readUserCommand();
			String feedback = executeCommand(userCommand);
			showToUser(feedback);
		}
	}
	
	public static String readUserCommand() throws IOException{
		showToUser(ENTER_COMMAND);
		String userCommand = scanner.nextLine();
		return userCommand;
	}
	
	private static void showToUser(String text) {
		System.out.println(text);
	}
	
	public static String executeCommand(String userCommand) throws IOException {
		if (userCommand.trim().equals("")){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		String commandTypeString = getFirstWord(userCommand);

		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
		
		String parameters = removeFirstWord(userCommand);

		switch (commandType) {
			case ADD:
				return addText(parameters);
			case DISPLAY:
				return display();
			case DELETE:
				return delete(parameters);
			case CLEAR:
				return clear();
			case SORT:
				return sort();
			case SEARCH:
				return search(parameters);
			case EXIT:
				saveTextToFile(fileName);
				System.exit(0);
			default:
				throw new Error("Unrecognized command type");
		}
	}

	public static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null) {
			throw new Error("command type string cannot be null!");
		}
		
		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("delete")){
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("sort")){
			return COMMAND_TYPE.SORT;
		} else if (commandTypeString.equalsIgnoreCase("search")){
			return COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("clear")){	
			return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
		 	return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	private static String addText(String parameters) throws IOException{
		allText.add(parameters);
		saveTextToFile(fileName);
		return String.format(TEXT_ADDED, fileName, parameters);
	}
	
	/**
	 * Display the existing texts with index.
	 * If there is nothing in the text, return text is empty
	 */

	public static String display(){
		if (allText.isEmpty()){
			return String.format(TEXT_EMPTY, fileName);
		} else {
			return textContent();
		}
	}
	
	/**
	 * This method will return a string contains all the texts according to adding in sequence.
	 * 
	 * @return displayTextContent
	 */
	private static String textContent(){
		String displayTextContent = "";
		int textIndex;
		
		// add text expect the last line which does not to start a new line.
		for (int i = 0;i < (allText.size() - 1); i++){
			textIndex = i + 1;
			displayTextContent += (textIndex + ". " + allText.get(i) + "\n");
		}
		
		//add the last line
		textIndex = (allText.size() - 1);
		displayTextContent += (allText.size() + ". " + allText.get(textIndex));
		return displayTextContent;
	}
	
	/**
	 * Delete the respect number of text
	 * 
	 * @param parameters
	 * @return TEXT_DELETED message
	 * @throws IOException
	 */
	private static String delete(String parameters) throws IOException{
		int deleteIndex = (Integer.valueOf(parameters) - 1);
		String deleteText = allText.get(deleteIndex);
		allText.remove(deleteIndex);
		saveTextToFile(fileName);
		return String.format(TEXT_DELETED, fileName, deleteText);
	}
	
	/**
	 * Clear all the text in arrayList and the file.
	 * 
	 * @return TEXT_CLEARED message
	 */
	private static String clear() throws IOException{
		allText.clear();
		saveTextToFile(fileName);
		return String.format(TEXT_CLEARED, fileName);
	}
	
	/**
	 * sort lines alphabetically.
	 * 
	 */
	private static String sort() throws IOException{
		allText.sort(null);
		saveTextToFile(fileName);
		return String.format(TEXT_SORTED, fileName);
	}
	
	private static String search(String parameters) throws IOException{
		if (hasPara(parameters)) {
			return linesContainPara;
		} else{
			return String.format(SEARCH_FAILED, fileName, parameters);
		}	
	}
	
	private static boolean hasPara(String parameters){
		linesContainPara = "";
		String thisLine;
		int textIndex;
		boolean isContain = false;
		for (int i = 0; i < allText.size() - 1; i++){
			thisLine = allText.get(i);
			if (thisLine.contains(parameters)){
				isContain = true;
				textIndex = i + 1;
				linesContainPara += (textIndex + ". " + thisLine + "\n");
			}
		}
		
		textIndex = allText.size() - 1;
		thisLine = allText.get(textIndex);
		if (thisLine.contains(parameters)){
			isContain = true;
			linesContainPara += (allText.size() + ". " + thisLine);
		}
		
		return isContain;
		
	}
	
	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
}
	
