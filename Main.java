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
	private static ArrayList<String> ladder; //The ladder we return
	private static String[] input; //input[0] is start, input[1] is end
	private static ArrayList<String> isExplored;
	private static ArrayList<String> dictArray;
	private static ArrayList<String> potential;
    private static ArrayList<String> parsedInput;
	private static Queue<Node> q;
	private static ArrayList<Node> potentialNodes;
	private static ArrayList<Node> nodeLadder;
	private static int counter = 0;

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

		while(true) {
			parsedInput = parse(kb);
			if(parsedInput.isEmpty()){
				break;
			}
			ArrayList<String> output = getWordLadderDFS(parsedInput.get(0), parsedInput.get(1));
			printLadder(output);
			//output = getWordLadderBFS(parsedInput.get(0), parsedInput.get(1));
			//printLadder(output);
		}
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		ladder = new ArrayList<String>();
		isExplored = new ArrayList<String>();
		Set<String> dict = makeDictionary();
		dictArray = new ArrayList<String>(dict);
		potential = new ArrayList<String>();
		q = new LinkedList<Node>();
		potentialNodes = new ArrayList<Node>();
		nodeLadder = new ArrayList<Node>();
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

		if(parsedInput == null){
			parsedInput = new ArrayList<String>();
			parsedInput.add(start);
			parsedInput.add(end);
		}

		int minDifference = end.length();
		int index = 0;

		start = start.toUpperCase();
		end = end.toUpperCase();

		Node word = new Node();
		word.value = start;
		isExplored.add(word.value);

		ArrayList<Node> localPotentialNodes = new ArrayList<Node>();
		populateNodeNeighborhood(word, localPotentialNodes);

		if(word.value.equals(end)){
			for(Node temp : nodeLadder){
				ladder.add(temp.value);
			}
			ladder.add(end);
			return ladder;
		}
		for (int i = 0; i < localPotentialNodes.size(); i++) {
			Node minWord = localPotentialNodes.get(i);
			int difference = difference(minWord.value, end);
			if (difference < minDifference) {
				minDifference = difference;
				index = i;
			}
		}
		if(localPotentialNodes.size() > 0){
			Node node = localPotentialNodes.get(index);
			if(!isExplored.contains(node.value)){
				nodeLadder.add(word);
				counter++;
				getWordLadderDFS(node.value, end);
				counter--;
			}
		}

		for(Node node : localPotentialNodes){
			if(!isExplored.contains(node.value)){
				nodeLadder.add(word);
				counter++;
				getWordLadderDFS(node.value, end);
				counter--;
			}
		}

		if(counter==0){
			ArrayList<String> ret = new ArrayList<>(ladder);
			ladder.clear();
			isExplored.clear();
			return ret;
		}
		return ladder;

		/*
		if(start.equals(end)) {
			return ladder;
		} else {
            potential.clear();
            populateNeighborhood(start);
            int index = 0;
            int minDifference = end.length();
            for (int i = 0; i < potential.size(); i++) {
                String word = potential.get(i);
                int difference = difference(word, end);
                if (difference < minDifference) {
                    minDifference = difference;
                    index = i;
                }
            }
            if(potential.size() > 0){
                String word = potential.get(index);
                if (!isExplored(word)) {
                    counter++;
                    getWordLadderDFS(word, end);

                    potential.clear();
                    iterations++;
                    populateNeighborhood(word);
                    if(isExplored(end)){
                        ladder.add(word);
                        counter--;
                        if(counter == 0) {
                        	ladder.add(start);
                            Collections.reverse(ladder);
                            iterations--;
                            ArrayList<String> ret = new ArrayList<String>(ladder);
                            ladder.clear();
                            isExplored.clear();
                            return ret;
                        }
                        return ladder;
                    }
                }
            } else {
            	ladder.clear();
            	return ladder;
			}
            return ladder;
		}
		*/


	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {

		if(parsedInput == null){
			parsedInput = new ArrayList<String>();
			parsedInput.add(start);
			parsedInput.add(end);
		}

		start = start.toUpperCase();
		end = end.toUpperCase();

		if(!ladder.isEmpty()){
			ladder.clear();
		}

		Node word = new Node();
		word.value = start;

		q.add(word);
		isExplored.add(word.value);

		while(!q.isEmpty()){
			Node node = q.poll();
			if(node.value.equals(end)){
				Node temp = new Node();
				temp = node;
				while(temp.previous != null){
					ladder.add(temp.value);
					temp = temp.previous;
				}
				ladder.add(temp.value);
				Collections.reverse(ladder);
				ArrayList<String> ret = new ArrayList<String>(ladder);
				ladder.clear();
				q.clear();
				isExplored.clear();
				return ret;
			}

			potentialNodes.clear();
			populateNodeNeighborhood(node);

			for(int i = 0; i < potentialNodes.size(); i++){
				Node newNode = potentialNodes.get(i);
				if(!isExplored.contains(newNode.value)){
					newNode.previous = node;
					isExplored.add(newNode.value);
					q.add(newNode);
				}
			}
		}


		return null; // The ladder is empty
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
		if(ladder == null || ladder.size() == 0){
			System.out.println("no word ladder can be found between " + parsedInput.get(0).toLowerCase() + " and " + parsedInput.get(1).toLowerCase() + ".");
		} else {
			System.out.println("a " + (ladder.size() - 2) + "-rung ladder exists between " + parsedInput.get(0).toLowerCase() + " and " + parsedInput.get(1).toLowerCase() + ".");
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

	public static void populateNeighborhood(String word){
		if(dictArray.isEmpty()){
			dictArray = new ArrayList<String>(makeDictionary());
		}
		Iterator<String> neighborIter = dictArray.iterator();

		while(neighborIter.hasNext()){
			String nextWord = neighborIter.next();
			if(!nextWord.equals(word) && difference(nextWord, word) == 1 && !isExplored(nextWord)){
				potential.add(nextWord);
			}
		}
	}

	public static void populateNodeNeighborhood(Node node){
		if(dictArray.isEmpty()){
			dictArray = new ArrayList<String>(makeDictionary());
		}
		Iterator<String> neighborIter = dictArray.iterator();

		while(neighborIter.hasNext()){
			String nextWord = neighborIter.next();
			if(!nextWord.equals(node.value) && difference(nextWord, node.value) == 1 && !isExplored.contains(nextWord)){
				Node nextNode = new Node();
				nextNode.value = nextWord;
				potentialNodes.add(nextNode);
			}
		}
	}

	public static void populateNodeNeighborhood(Node node, ArrayList<Node> nodeNeighborhood){
		if(dictArray.isEmpty()){
			dictArray = new ArrayList<String>(makeDictionary());
		}
		Iterator<String> neighborIter = dictArray.iterator();

		while(neighborIter.hasNext()){
			String nextWord = neighborIter.next();
			if(!nextWord.equals(node.value) && difference(nextWord, node.value) == 1 && !isExplored.contains(nextWord)){
				Node nextNode = new Node();
				nextNode.value = nextWord;
				nodeNeighborhood.add(nextNode);
			}
		}
	}
}
