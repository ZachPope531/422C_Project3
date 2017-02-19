/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL: 
 * Spring 2017
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	private static Iterator<String> setIter; //Iterator for the dictionary
	private static ArrayList<String> ladder; //The ladder we return
	private static String[] input; //input[0] is start, input[1] is end
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		
		// TODO methods to read in words, output ladder

		//Test the keyboard parser:
		/*
		ArrayList<String> test = parse(kb);
		System.out.println(test.toString());
		*/
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.

		setIter = null;
		ladder = null;
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		ArrayList<String> ret = new ArrayList<String>();
		System.out.println("Input two words to be tested"); //May have to delete this line later
		String unformattedIn = keyboard.nextLine();
		input = unformattedIn.split(" ");
		if(input[0].equals("/quit") || input[1].equals("/quit")){
			return ret; //empty ArrayList
		} else {
			ret.add(input[0].toUpperCase());
			ret.add(input[1].toUpperCase());
		}
		return ret;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		Set<String> dict = makeDictionary();
		setIter = dict.iterator();

		if(setIter.next().equals(end)){

		}
		
		return null; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}
    
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
		for(int i = 0; !ladder.isEmpty(); i++){
			System.out.println(ladder.get(i));
		}
	}
	// TODO
	// Other private static methods here

	public static int weight(String current, String end){

		 // Takes the current word and compares it to end
		 // Returns an int of how many letters are the same and are in the same place

		int weight = 0;
		//We need indices so make character arrays
		char[] charCurrent = current.toCharArray();
		char[] charEnd = end.toCharArray();

		for(int i = 0; i < current.length(); i++){
			if(charCurrent[i] == charEnd[i]){
				weight++;
			}
		}

		return weight;
	}
}
