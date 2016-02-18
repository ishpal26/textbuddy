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
 * This class is used to store, retrieve and delete the information provided by
 * the user. We assume that the inputs are all valid and there are only 5 user commands,
 * namely: add, delete, display, clear and exit. There can only be one deletion at a time, unless
 * the clear command is entered. User will have to provide an integer besides the command 'delete'.
 * For the add command, we assume that the user provides at least one argument, after the command 'add'
 * In the case of the exit and clear commands, we assume that there are no other arguments provided
 * other than the command itself.
 */

public class TextBuddy {
	
	private static File dataFile;
	private static ArrayList<String> list;
	
	private static final int EXIT_WITH_ERROR = 1;
	private static final int EXIT_WITHOUT_ERROR = 0;
	private static final int INCREMENT_INDEX = 1;
	private static final int EMPTY_LIST_SIZE = 0;
	
	private static final String NO_ARGUMENT_PROVIDED = "";
	private static final int NO_ARGUMENT_LENGTH = 0;
	
	//these are the different command types
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
		boolean isValidArgs = checkArgs(args);     // added for junit testing only
		if (args.length == NO_ARGUMENT_LENGTH){
			printCorrectFormat();
			System.exit(EXIT_WITH_ERROR);
		}
	}
	
	//added for junit testing
	public static boolean checkArgs(String[] args){
		if (args.length == NO_ARGUMENT_LENGTH)
			return false;
		else
			return true;
	}

	private static void checkFile(String[] args) throws IOException, ClassNotFoundException {
		
		File inputFile = new File (args[0]);
		boolean isFileValid = inputFile.exists();
		
		if (isFileValid == true) {
			printWelcomeMessage(args[0]);
			dataFile = inputFile;
			getListFromFile();
		} else {
			Files.createFile(inputFile.toPath());
			dataFile = inputFile;
			list = new ArrayList<String>();
			printWelcomeMessage(args[0]);
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
			Boolean isCommandValid = checkValid(getFirstWordFromCommand(userCommand)); // added just for unit test
			executeCommand(userCommand);
		}
	}
	
	// added in just for unit test
	public static boolean checkValid(String userCommand){
		switch(userCommand){
			case "add" :
				return true;
			case "display" :
				return true;
			case "delete" :
				return true;
			case "clear" :
				return true;
			case "exit" :
				return true;
			case "sort" :
				return true;
			case "search" :
				return true;
			default :
				return false;
		}
	
	}
	private static void executeCommand(String userCommand) throws IOException {

		String commandTypeString = getFirstWordFromCommand(userCommand);
		CommandType commandType = getCommandType(commandTypeString);

		switch (commandType) {
		case ADD_ENTRY :
			addEntry(userCommand);
			break;
		case DELETE_ENTRY :
			deleteEntry(userCommand);
			break;
		case DISPLAY_LIST :
			displayList();
			break;
		case CLEAR_LIST :
			clearList();
			break;
		case SORT_LIST :
			sortList();
			break;
		case SEARCH_LIST :
			searchList(userCommand);
			break;
		case EXIT_PROGRAM :
			exitProgram();
			break;
		default :
			printUnrecognisedCommand(userCommand);
		}
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
	
	private static void addEntry(String userCommand) throws IOException {
		String wordsToInsert = removeFirstWordFromCommand(userCommand);
		
		if (wordsToInsert.equals(NO_ARGUMENT_PROVIDED)) {
			printUnrecognisedCommand(userCommand);
			return;
		}
		list.add(wordsToInsert);
		printInsertedText(wordsToInsert);
		saveFile();
	}
	
	/**
	 * This operation removes an entry from the list if the index is valid
	 * @throws IOException 
	 * 
	 * 
	 */
	
	private static void deleteEntry(String userCommand) throws IOException {
		String index = removeFirstWordFromCommand(userCommand);
		
		// Checks if user has given a index to remove
		if (index.equals(NO_ARGUMENT_PROVIDED)) {
			printUnrecognisedCommand(userCommand);
			return;
		}
		
		int removeIndex = Integer.parseInt(index);
		
		//Ensure the index given is valid for proper deletion
		
		boolean candelete = canDelete(removeIndex, list.size());      // also for junit testing
		
		if ((removeIndex > list.size()) || (removeIndex <= EMPTY_LIST_SIZE)) {
			printInvalidIndex();
		} else {
			// -1 as we need to account for zero indexing in an ArrayList
			String deletedPhrase = list.remove(removeIndex-1);
			printDeletedText(deletedPhrase);
			saveFile();
		}
	}
	
	// just for junit test
	public static boolean canDelete(int index, int size){
		if(index > size)
			return false;
		else if(index <=0)
			return false;
		else 
			return true;
	}
	
	private static void displayList() {
		if (list.size() == EMPTY_LIST_SIZE) {
			printEmptyList();
		} else {
			printList();
		}
	}
	
	private static void clearList() throws IOException {
		list.clear();
		System.out.println("all content deleted from " + dataFile.getName());
		saveFile();
	}
	
	private static void sortList() throws IOException {
		Collections.sort(list);
		displayList();
		saveFile();
	}
	
	private static void searchList(String userCommand) {
		String wordToSearch = removeFirstWordFromCommand(userCommand);
		
		if (wordToSearch.equals(NO_ARGUMENT_PROVIDED)) {
			printUnrecognisedCommand(userCommand);
			return;
		}
		int i = 1;
		for(int j = 0; j < list.size(); j++){
			if (list.get(j).contains(wordToSearch)) {
					printLineWithKeyword(i, list.get(j));
					i += INCREMENT_INDEX;
			}
		}
		
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
	
	private static String getFirstWordFromCommand(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private static String removeFirstWordFromCommand(String userCommand) {
		return userCommand.replace(getFirstWordFromCommand(userCommand), "").trim();
	}
	
	private static void printWelcomeMessage(String filename) {
		System.out.println("Welcome to TextBuddy. " + filename + " is ready for use");
	}
	
	private static void printCorrectFormat() {
		System.out.println("Execution format: java progam filename.extension ");
	}
	
	private static void printCommandLine() {
		System.out.print("command: ");
	}
	
	private static void printUnrecognisedCommand(String userCommand) {
		System.out.println("Error: Unrecognized command \"" + userCommand + "\" ");
	}
	private static void printInsertedText(String wordsToInsert) {
		System.out.println("added to " + dataFile.getName() + ": \"" + wordsToInsert + "\"");
	}
	private static void printInvalidIndex() {
		System.out.println("Invalid deletion index");
	}
	private static void printDeletedText(String deletedPhrase) {
		System.out.println("deleted from " + dataFile.getName() + ": \"" + deletedPhrase + "\"");
	}
	private static void printEmptyList() {
		System.out.println(dataFile.getName() + " is empty");
	}
	private static void printList() {
		for (int i = 0; i<list.size(); i++) {
			System.out.println(i + INCREMENT_INDEX + ". " + list.get(i));
		}
	}
	private static void printLineWithKeyword(int i, String string) {
		System.out.println(i +". " + string );	
	}
}
