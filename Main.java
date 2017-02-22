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
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	private static Iterator<String> dictIter; //Iterator for the dictionary
	private static ArrayList<String> ladder; //The ladder we return
	private static String[] input; //input[0] is start, input[1] is end
	private static ArrayList<String> isExplored;
	private static ArrayList<String> isVisited;
	private static ArrayList<String> dictArray;
	private static ArrayList<String> dfsPotential;
	private static ArrayList<String> bfsladder;
	private static int oldDifference;
	private static int dfsIterations = 0;
    private static int counter = 0;
    private static ArrayList<String> parsedInput;
    private static Queue<String> q;

    
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

		//Test the isExplored method
		/*
		isExplored.add("Hello");
		isExplored.add("World");
		System.out.println(isExplored("Hello") + " " + isExplored("Blah"));
		*/

		parsedInput = parse(kb);
		ArrayList<String> output = getWordLadderBFS(parsedInput.get(0), parsedInput.get(1));
		printLadder(output);
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		ladder = new ArrayList<String>();
		isExplored = new ArrayList<String>();
		isVisited = new ArrayList<String>();
		Set<String> dict = makeDictionary();
		dictArray = new ArrayList<String>(dict);
		dfsPotential = new ArrayList<String>();
		bfsladder = new ArrayList<String>();
		oldDifference = 0;
		q = new LinkedList<String>();
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
		// Return the start and end words if no ladder

		isExplored.add(start);

		if(start.equals(end)) {
			return ladder;
		} else {
            dfsPotential.clear();
            populateDFSNeighborhood(start);
            int index = 0;
            int minDifference = end.length();
            for (int i = 0; i < dfsPotential.size(); i++) {
                String word = dfsPotential.get(i);
                int difference = difference(word, end);
                if (difference < minDifference) {
                    minDifference = difference;
                    index = i;
                }
            }
            if(dfsPotential.size() > 0){
                String word = dfsPotential.get(index);
                if (!isExplored(word)) {
                    counter++;
                    getWordLadderDFS(word, end);

                    dfsPotential.clear();
                    dfsIterations++;
                    populateDFSNeighborhood(word);
                    if(isExplored(end)){
                        ladder.add(word);
                        counter--;
                        if(counter == 0) {
                            ladder.add(start);
                            Collections.reverse(ladder);
                            return ladder;
                        }
                        return ladder;
                    }
                }
            }
            return ladder;
		}
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {

		Iterator<String> iter = dictArray.iterator();
		int number = 0;
		
		isVisited.add(start);
		bfsladder.add(start);
		String newWord;
		String iterWord;
		
		while(iter.hasNext()){
			newWord = iter.next();
			
			if(!newWord.equals(start) && difference(newWord, start) == 1 && !isVisited.contains(newWord)){
				q.add(Integer.toString(number)+newWord);
				number++;
				isVisited.add(newWord);
			}
		}

		
		while(q.size() > 0){
			newWord = q.remove();
			if(newWord.equals(end))
			{
				bfsladder.add(newWord);
				return bfsladder;
			}
		//	bfsladder.add(newWord);
			
			iter = dictArray.iterator();
			while(iter.hasNext())
			{
				iterWord = iter.next();
				if(!iterWord.equals(newWord) && difference(iterWord, newWord) == 1 && !isVisited.contains(iterWord)){
						isVisited.add(iterWord);
						q.add(iterWord);
				}
					
			}
		}

		
		
		
		return bfsladder; // replace this line later with real return
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
		if(ladder.size() == 0){
			System.out.println("no word ladder can be found between " + parsedInput.get(0).toLowerCase() + " and " + parsedInput.get(1).toLowerCase() + ".");
		} else {
			System.out.println("a " + (dfsIterations + 1) + "-rung ladder exists between " + parsedInput.get(0).toLowerCase() + " and " + parsedInput.get(1).toLowerCase() + ".");
			for (int i = 0; i < ladder.size(); i++) {
				System.out.println(ladder.get(i));
			}
		}
	}
	// TODO
	// Other private static methods here

	public static int difference(String current, String end){

		 // Takes the current word and compares it to end
		 // Returns an int of how many letters are different

		int weight = 0;
		//We need indices so make character arrays
		char[] charCurrent = current.toCharArray();
		char[] charEnd = end.toCharArray();

		for(int i = 0; i < current.length(); i++){
			if(charCurrent[i] != charEnd[i]){
				weight++;
			}
		}

		return weight;
	}

	public static boolean isExplored(String current){
		if(isExplored.contains(current)){
			return true;
		} else {
			return false;
		}
	}

	public static void populateDFSNeighborhood(String word){
		Iterator<String> neighborIter = dictArray.iterator();

		while(neighborIter.hasNext()){
			String nextWord = neighborIter.next();
			if(!nextWord.equals(word) && difference(nextWord, word) == 1 && !isExplored(nextWord)){
				dfsPotential.add(nextWord);
			}
		}
	}
}
