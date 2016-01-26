import java.io.*;
import java.nio.file.Files;
import java.util.*;


public class TextBuddy {
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	private static File dataFile;
	private static FileOutputStream fos;
	private static ArrayList<String> list;
	//these are the different command types
	enum COMMAND_TYPE {
		ADD_TEXT, DISPLAY_LIST, DELETE_TEXT, CLEAR_LIST, INVALID, EXIT
	};
	
	// These are the correct number of parameters for each command
		private static final int PARAM_SIZE_DELETE = 1;
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		checkArg(args);
		checkFile(args);
		
		while(true){
			System.out.print("Enter command:");
			String command = scanner.nextLine();
			String userCommand = command;
			String feedback = executeCommand(userCommand);
			//System.out.println(feedback);
		}
	}

	private static void checkFile(String[] args) throws IOException, ClassNotFoundException {
		File f = new File(args[0]);
		boolean fileExist = f.exists();
		if(fileExist == true){
			printWelcomeMessage(args[0]);
			dataFile = f;
			list = getListFromFile(f);
		}else{
			Files.createFile(f.toPath());
			dataFile = f;
			printWelcomeMessage(args[0]);
			
		}
	}

	private static ArrayList<String> getListFromFile(File f) throws IOException, ClassNotFoundException {
		 /*Scanner s;
		 ArrayList<String> list = new ArrayList<String>();
		 s = new Scanner(f);
		 while (s.hasNext()) {
			 list.add(s.next());
	     }	
		 s.close();*/
		ArrayList<String> list = new ArrayList<String>();
		FileInputStream fin= new FileInputStream (f.getName());
		ObjectInputStream ois = new ObjectInputStream(fin);
		list= (ArrayList<String>)ois.readObject();
		fin.close();
		 return list;
	}

	private static void checkArg(String[] args) {
		if(args.length==0){
			System.err.println("Please input filename");
		}
	}

	private static void printWelcomeMessage(String filename) {
		System.out.println("Welcome to TextBuddy. "+filename+" is ready to use");
	}
	
	private static String executeCommand(String userCommand) throws IOException {
		if (userCommand.trim().equals(""))
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);

		String commandTypeString = getFirstWord(userCommand);

		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD_TEXT:
			return addText(userCommand);
		case DELETE_TEXT:
			return deleteText(userCommand);
		case DISPLAY_LIST:
			return displayList(userCommand);
		case CLEAR_LIST:
			return clearList(userCommand);
		case INVALID:
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		case EXIT:
			return exitProg();
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
	}
	
	private static String exitProg() throws IOException {
		saveFile();
		System.exit(0);
		return null;
	}

	private static void saveFile() throws IOException{
		FileOutputStream fout= new FileOutputStream (dataFile.getName());
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(list);
		oos.close();
		fout.close();
	}

	private static String deleteText(String userCommand) {
		String text = removeFirstWord(userCommand);
		list.remove(Integer.parseInt(text)-1);
		System.out.println("deleted from "+ dataFile.getName() + ": \"" + text +"\"");
		return null;
	}

	private static String clearList(String userCommand) {
		list.clear();
		System.out.println("all content deleted from " + dataFile.getName());
		return "";
	}

	private static String displayList(String userCommand) {
		if(list.size()==0){
			System.out.println(dataFile.getName()+" is empty");
		}
		
		for(int i=0;i<list.size();i++){
			System.out.println(i+1+". "+list.get(i));
		}
		return null;
	}

	private static String addText(String userCommand) {
		String text = removeFirstWord(userCommand);
		list.add(text);
		System.out.println("added to "+ dataFile.getName() + ": \"" + text +"\"");
		return "added to file :"+text;
	}
	
	
	
	
	
	
	
	
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD_TEXT;
		} else if(commandTypeString.equalsIgnoreCase("delete")){
			return COMMAND_TYPE.DELETE_TEXT;
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
	
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}
}
