import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;


/**
 * This class is used to store, retrieve and delete, sort and search the information provided by
 * the user. All text will be stored into an ArrayList named list, which will be saved onto a file.
 * Data is saved onto the file after every user operation which modifies the list.  
 * 
 * Assumptions: 
 * 
 * 1) We assume that the inputs are all valid and there are only 7 user commands,
 *    namely: add, delete, display, clear, sort, search and exit.
 * 2) There can only be one deletion at a time, unless the clear command is entered. 
 * 3) User will have to provide an integer besides the command 'delete'.
 * 4) For the add command, we assume that the user provides at least one argument, after the command 'add'
 * 5) In the case of the exit, sort, clear and display commands, 
 *    we assume that there are no other arguments provided other than the command itself.
 * 6) For the search command, we assume that there is only one keyword provided by the user.
 * 
 * The command format is given by the example stated below:
 * 		Welcome to TextBuddy.file.txt is ready for use
 * 		command: hello
 * 		Error: Unrecognized command "hello"
 * 		command: add hello
 * 		added to file.txt: "hello"
 * 		command: add world
 * 		added to file.txt: "world"
 * 		command: add Hello
 * 		added to file.txt: "Hello"
 * 		command: display
 * 		1. hello
 * 		2. world
 * 		3. Hello
 * 		
 * 		command: sort
 * 		list sorted alphabetically
 * 		1. hello
 * 		2. Hello
 * 		3. world
 * 
 * 		command: delete 2
 * 		deleted from file.txt: "Hello"
 * 		command: delete 10
 * 		Error: Invalid Deletion Index
 * 		command: display
 * 		1. hello
 * 		2. world
 *		 
 * 		command: add World
 * 		added to file.txt: "World"
 * 		command: search world
 * 		1. world
 * 		2. World
 *		 
 * 		command: clear
 * 		all content deleted from file.txt
 * 		command: display
 * 		file.txt is empty
 * 		command: exit
 * 		
 * @author Ishpal Singh Gill
 */

public class TextBuddy {
	
	private static File dataFile;
	private static ArrayList<String> list;
	
	private static final int EXIT_WITH_ERROR = 1;
	private static final int EXIT_WITHOUT_ERROR = 0;
	private static final int PRINT_LIST_START_INDEX = 1;
	private static final int INCREMENT_INDEX = 1;
	private static final int EMPTY_LIST_SIZE = 0;
	private static final int NO_ARGUMENT_LENGTH = 0;
	
	private static final String NO_ARGUMENT_PROVIDED = "";
	private static final String EMPTY_STRING = "";
	
	// These are all the messages which will be shown to the user
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy.%s is ready for use";
	private static final String MESSAGE_TEXT_ADDED = "added to %s: \"%s\"";
	private static final String MESSAGE_TEXT_DELETED = "deleted from %s: \"%s\"";
	private static final String MESSAGE_LIST_CLEARED = "all content deleted from %s";
	private static final String MESSAGE_EMPTY_LIST = "%s is empty";
	private static final String MESSAGE_SORT_COMPLETE = "list sorted alphabetically";
	private static final String MESSAGE_KEYWORD_NOT_FOUND = "\"%s\" not found in list";
	
	// These are the possible error messages shown to the user
	private static final String ERROR_DUPLICATE_DETECTED = "Error: Duplicate content \"%s\" detected";
	private static final String ERROR_INVALID_INDEX = "Error: Invalid Deletion Index";
	private static final String ERROR_UNRECOGNIZED_COMMAND = "Error: Unrecognized command \"%s\"";
	private static final String ERROR_INVALID_EXECUTION_FORMAT = "Error: Execution format --> java progam filename.extension";
	
	
	// These are the different command types  
	enum CommandType {
		ADD_ENTRY, DISPLAY_LIST, DELETE_ENTRY, CLEAR_LIST, EXIT_PROGRAM, INVALID_COMMAND, SORT_LIST, SEARCH_LIST
	};
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		checkValidArg(args);
		checkFile(args);
		runProgram();
	}
	/*
	 * Checks if the user has input an argument after file name
	*/
	private static void checkValidArg(String[] args) {
		if (checkArgs(args) == false) {
			showToUser(ERROR_INVALID_EXECUTION_FORMAT);
			System.exit(EXIT_WITH_ERROR);
		}
	}
	
	public static boolean checkArgs(String[] args){
		if (args.length == NO_ARGUMENT_LENGTH) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * This operation checks if the filename given by the user is valid
	 * It creates a file if filename is invalid
	 */
	public static void checkFile(String[] args) throws IOException, ClassNotFoundException {
		
		File inputFile = new File (args[0]);
		boolean isFileValid = inputFile.exists();
		
		if (isFileValid == true) {
			showToUser(String.format(MESSAGE_WELCOME, args[0]));
			dataFile = inputFile;
			getListFromFile();
		} else {
			Files.createFile(inputFile.toPath());
			dataFile = inputFile;
			list = new ArrayList<String>();
			showToUser(String.format(MESSAGE_WELCOME, args[0]));
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void getListFromFile() throws IOException, ClassNotFoundException {
		FileInputStream fileInStream = new FileInputStream (dataFile.getName());
		ObjectInputStream objectInStream = new ObjectInputStream(fileInStream);
		list = (ArrayList<String>)objectInStream.readObject();
		fileInStream.close();
		objectInStream.close();
	}

	private static void runProgram() throws IOException {
		while (true) {
			printCommandLine();
			String userCommand = scanner.nextLine();
			String feedback = executeCommand(userCommand);
			showToUser(feedback);
		}
	}
	
	public static String executeCommand(String userCommand) throws IOException {

		String commandWordString = getCommandWord(userCommand);
		CommandType commandType = getCommandType(commandWordString);
		
		switch (commandType) {
		case ADD_ENTRY :
			return addEntry(userCommand);
		case DELETE_ENTRY :
			return deleteEntry(userCommand);
		case DISPLAY_LIST :
			return displayList();
		case CLEAR_LIST :
			return clearList(userCommand);
		case SORT_LIST :
			return sortList();
		case SEARCH_LIST :
			return searchList(userCommand);
		case EXIT_PROGRAM :
			exitProgram();
			break;
		default :
			return String.format(ERROR_UNRECOGNIZED_COMMAND, userCommand);
		}
		return EMPTY_STRING;
	}
	 
	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * 
	 * @param commandTypeString
	 *            is the first word of the user command
	 */
	private static CommandType getCommandType(String commandTypeString) {

		if (commandTypeString.equalsIgnoreCase("add")) {
			return CommandType.ADD_ENTRY;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return CommandType.DELETE_ENTRY;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return CommandType.CLEAR_LIST;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return CommandType.DISPLAY_LIST;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
		 	return CommandType.EXIT_PROGRAM;
		} else if (commandTypeString.equalsIgnoreCase("sort")) {
			return CommandType.SORT_LIST;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return CommandType.SEARCH_LIST;
		} else {
			return CommandType.INVALID_COMMAND;
		}
	}
	
	/**
	 * This operation adds the user's text into the list
	 * 
	 * @param wordsToInsert
	 *            is the command without the first word 'add'
	 * @throws IOException 
	 */
	
	public static String addEntry(String userCommand) throws IOException {
		String wordsToInsert = removeFirstWordFromCommand(userCommand);
		
		if (wordsToInsert.equals(NO_ARGUMENT_PROVIDED)) {
			return String.format(ERROR_UNRECOGNIZED_COMMAND, userCommand);
		} else if (list.contains(wordsToInsert)) {
			return String.format(ERROR_DUPLICATE_DETECTED, wordsToInsert);
		} else {
			list.add(wordsToInsert);
			saveFile();
			return String.format(MESSAGE_TEXT_ADDED, dataFile.getName(),wordsToInsert);
		}
	}
	
	/**
	 * This operation removes an entry from the list if the index is valid
	 * @param index
	 * 				is the index to be deleted
	 * @throws IOException 
	 * 
	 * 
	 */
	
	public static String deleteEntry(String userCommand) throws IOException {
		String index = removeFirstWordFromCommand(userCommand);
		
		if (list.size() == EMPTY_LIST_SIZE) {
			return String.format(MESSAGE_EMPTY_LIST, dataFile.getName());
		}
		// Checks if user has given a index to remove
		if (index.equals(NO_ARGUMENT_PROVIDED)) {
			return ERROR_INVALID_INDEX;
		}

		//Ensure the index given is valid for proper deletion	
		int removeIndex = Integer.parseInt(index);
		boolean canDelete = validDeleteIndex(removeIndex, list.size());
		
		if (canDelete == false) {
			return ERROR_INVALID_INDEX;
		} else {
			// -1 as we need to account for zero indexing in an ArrayList
			String deletedPhrase = list.remove(removeIndex-1);
			saveFile();
			return String.format(MESSAGE_TEXT_DELETED, dataFile.getName(),deletedPhrase);
		}
	}
	
	public static boolean validDeleteIndex(int index, int size) {
		if ((index > size) || (index <= EMPTY_LIST_SIZE)) {
			return false;
		} else { 
			return true;
		}
	}
	
	/**
	 * This operation converts the list into a String
	 * @param listInStringFormat
	 * 				is the string which contains the list
	 */
	public static String displayList() {
		String listInStringFormat;
		if (list.size() == EMPTY_LIST_SIZE) {
			return String.format(MESSAGE_EMPTY_LIST, dataFile.getName());
		} else {
			listInStringFormat = listToString();
		}
		return listInStringFormat;
	}
	
	public static String clearList(String userCommand) throws IOException {
		list.clear();
		saveFile();
		return String.format(MESSAGE_LIST_CLEARED, dataFile.getName());
	}
	
	/**
	 * This operation sorts the content in the list alphabetically
	 * @throws IOException 
	 * 
	 * 
	 */
	public static String sortList() throws IOException {
		if (list.size() == EMPTY_LIST_SIZE) {
			return String.format(MESSAGE_EMPTY_LIST, dataFile.getName());
		} else {
			Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
			showToUser(MESSAGE_SORT_COMPLETE);
			String sortedList = displayList();
			saveFile();
			return sortedList;
		}
	}
	
	
	/**
	 * This operation displays texts in the list containing the keyword given by the user
	 * It returns a String made up of lines containing the keyword or an error message
	 * @throws IOException 
	 * 
	 * 
	 */
	public static String searchList(String userCommand) {
		String wordToSearch = removeFirstWordFromCommand(userCommand);
		
		if (list.size() == EMPTY_LIST_SIZE) {
			return String.format(MESSAGE_EMPTY_LIST, dataFile.getName());
		}
		
		if (wordToSearch.equals(NO_ARGUMENT_PROVIDED)) {
			return String.format(ERROR_UNRECOGNIZED_COMMAND, userCommand);
		}
		
		boolean hasOneSearchResult = false;
		String shortList = new String();
		
		int i = PRINT_LIST_START_INDEX;	
		for (int j = 0; j < list.size(); j++) {
			if (list.get(j).toLowerCase().contains(wordToSearch.toLowerCase())) {
				shortList += printLinesWithKeyword(i, list.get(j));
				hasOneSearchResult = true;
				i += INCREMENT_INDEX;
			}
		}
		
		if (hasOneSearchResult == false) {
			return String.format(MESSAGE_KEYWORD_NOT_FOUND, wordToSearch);
		}
		
		return shortList;
	}
	
	
	private static void exitProgram() throws IOException {
		saveFile();
		System.exit(EXIT_WITHOUT_ERROR);
	}

	/**
	 * This operation writes the data in the list into the file given by the user
	 * 
	 */
	private static void saveFile() throws IOException{
		FileOutputStream fileOutStream= new FileOutputStream (dataFile.getName());
		ObjectOutputStream objOutStream = new ObjectOutputStream(fileOutStream);
		objOutStream.writeObject(list);
		objOutStream.close();
		fileOutStream.close();
	}
	
	/**
	 * This operation returns the command type keyword from the user input
	 * 
	 */
	private static String getCommandWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	/**
	 * This operation returns the text after the command type keyword
	 * 
	 */
	private static String removeFirstWordFromCommand(String userCommand) {
		return userCommand.replace(getCommandWord(userCommand), "").trim();
	}
	
	private static void showToUser(String feedback) {
		System.out.println(feedback);
	}
	
	private static void printCommandLine() {
		System.out.print("command: ");
	}
	
	private static String listToString() {
		String concatList = new String();
		for (int i = 0; i<list.size(); i++) {
			concatList += (i + INCREMENT_INDEX + ". " + list.get(i) +"\n");
		}
		return concatList;
	}
	
	private static String printLinesWithKeyword(int index, String content) {
		return index +". " + content + "\n";	
	}
	
	public static int getLineCount(){
		return list.size();
	}
	public static String getListContent(int index){
		return list.get(index);
	}
}	

