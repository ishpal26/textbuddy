import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.ArrayList;


public class TextBuddy {
	
	private static File dataFile;
	private static ArrayList<String> list;
	//these are the different command types
	enum COMMAND_TYPE {
		ADD_ENTRY, DISPLAY_LIST, DELETE_ENTRY, CLEAR_LIST, EXIT, INVALID
	};
	
	// These are the correct number of parameters for each command
		private static final int PARAM_SIZE_DELETE = 1;
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		
		checkValidArg(args);
		checkFile(args);
		runProgram();
	}

	private static void checkValidArg(String[] args) {
		
		if(args.length == 0){
			printCorrectFormat();
			System.exit(1);
		}
	}

	private static void checkFile(String[] args) throws IOException, ClassNotFoundException {
		
		File inputFile = new File (args[0]);
		boolean isFileValid = inputFile.exists();
		
		if(isFileValid == true){
			printWelcomeMessage(args[0]);
			dataFile = inputFile;
			getListFromFile();
		}else{
			Files.createFile(inputFile.toPath());
			dataFile = inputFile;
			printWelcomeMessage(args[0]);
		}
	}
	
	private static void getListFromFile() throws IOException, ClassNotFoundException {
	
		FileInputStream fileInStream = new FileInputStream (dataFile.getName());
		ObjectInputStream objectInStream = new ObjectInputStream(fileInStream);
		list = (ArrayList<String>)objectInStream.readObject();
		fileInStream.close();
		objectInStream.close();
	}

	private static void runProgram() throws IOException {
		while(true){
			printCommandLine();
			String userCommand = scanner.nextLine();
			executeCommand(userCommand);
		}
	}
	
	private static void executeCommand(String userCommand) throws IOException {

		String commandTypeString = getFirstWord(userCommand);
		COMMAND_TYPE commandType = getCommandType(commandTypeString);

		switch (commandType) {
		case ADD_ENTRY :
			addEntry(userCommand);
			break;
		case DELETE_ENTRY :
			deleteEntry(userCommand);
			break;
		case DISPLAY_LIST :
			displayList(userCommand);
			break;
		case CLEAR_LIST :
			clearList(userCommand);
			break;
		case EXIT :
			exitProgram();
			break;
		default :
			printUnrecognisedCommand(userCommand);
		}
	}
	
	private static COMMAND_TYPE getCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD_ENTRY;
		} else if(commandTypeString.equalsIgnoreCase("delete")){
			return COMMAND_TYPE.DELETE_ENTRY;
		} else if(commandTypeString.equalsIgnoreCase("clear")){
			return COMMAND_TYPE.CLEAR_LIST;
		}else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY_LIST;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
		 	return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	private static void addEntry(String userCommand) {
		String wordsToInsert = removeFirstWord(userCommand);
		list.add(wordsToInsert);
		printInsertedText(wordsToInsert);
	}
	
	private static void deleteEntry(String userCommand) {
		String index = removeFirstWord(userCommand);
		
		if(index.equals("")){
			printUnrecognisedCommand(userCommand);
			return;
		}
		
		int removeIndex = Integer.parseInt(index);
		
		if((removeIndex > list.size()) || (removeIndex <= 0)){
			printInvalidIndex();
		}else{
			String deletedPhrase = list.remove(removeIndex-1);
			printDeletedText(deletedPhrase);
		}
	}
	
	private static void displayList(String userCommand) {
		if(list.size()==0){
			printEmptyList();
		}else{
			printList();
		}
	}
	
	private static void clearList(String userCommand) {
		list.clear();
		System.out.println("all content deleted from " + dataFile.getName());
	}
	
	private static void exitProgram() throws IOException {
		saveFile();
		System.exit(0);
	}

	private static void saveFile() throws IOException{
		FileOutputStream fileOutStream= new FileOutputStream (dataFile.getName());
		ObjectOutputStream objOutStream = new ObjectOutputStream(fileOutStream);
		objOutStream.writeObject(list);
		objOutStream.close();
		fileOutStream.close();
	}
	
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
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
		for(int i=0; i<list.size(); i++){
			System.out.println(i+1 + ". " + list.get(i));
		}
	}
}
