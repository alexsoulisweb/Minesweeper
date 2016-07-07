
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class represents a Minesweeper game.
 *
 * @author Brandon Canaday <blc58251@uga.edu>
 */
public class Minesweeper {
	
	private String[][] minesweeperArray;
	private boolean[][] doesHaveMineArray;
	private String[][] noFogArray;
	private int roundsCompleted = 0;
	private int totalMines = 0;
	private boolean noFog = false;
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// CONSTRUCTORS AND MAIN ////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
// __________________________________BELOW IS SEED FILE CONSTRUCTOR__________________________________////
    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * information provided in <code>seedFile</code>. Documentation about the 
     * format of seed files can be found in the project's <code>README.md</code>
     * file.
     *
     * @param seedFile the seed file used to construct the game
     * @see            <a href="https://github.com/mepcotterell-cs1302/cs1302-minesweeper-alpha/blob/master/README.md#seed-files">README.md#seed-files</a>
     */
    public Minesweeper(File seedFile) throws FileNotFoundException {
    	try {
    		Scanner keyboard = new Scanner(seedFile);
    		int rows = 0, cols = 0;
    		
    		if(keyboard.hasNextInt()) {
    			rows = keyboard.nextInt();
    			if(keyboard.hasNextInt()) {
    				cols = keyboard.nextInt();
    				if(keyboard.hasNextInt()) {
    					this.totalMines = keyboard.nextInt();
    				}
    			}
    		} else {
    			System.out.println("Cannot create game with " + seedFile + ", because it is not formatted correctly.");
    			System.out.println();
    			System.exit(0);
    		} // formatting check
    		
    		if(!(rows > 0 && rows <= 10 && cols > 0 && cols <= 10) || (this.totalMines > rows*cols)) {
    			System.out.println("Cannot create game with " + seedFile + ", because it is not formatted correctly.");
    			System.out.println();
    			System.exit(0);
    		} // formatting check
    		
    		doesHaveMineArray = new boolean[rows][cols]; // all initialized to false
			minesweeperArray = new String[rows][cols]; // all initialized to "   "
			noFogArray = new String[rows][cols]; // all initialized to "   "
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < cols; j++) {
					doesHaveMineArray[i][j] = false;
					minesweeperArray[i][j] = "   ";
					noFogArray[i][j] = "   ";
				}
			} // initialization of 2d arrays
			
			int count = 0;
			for(int i = 0; i < this.totalMines; i++) {
				int row = 0, col = 0;
				if(keyboard.hasNextInt()) {
					row = keyboard.nextInt();
					count++;
					if(keyboard.hasNextInt()) {
						col = keyboard.nextInt();
						count++;
						if((row >= 0 && row < rows) && (col >= 0 && col < cols)) {
							doesHaveMineArray[row][col] = true;
							count++;
						}
					}
				} 
			} 
			if(count != 3*totalMines) {
				System.out.println("Cannot create game with " + seedFile + ", because it is not formatted correctly.");
    			System.out.println();
    			System.exit(0);
			}
			// formatting check. if correct, sets mine positions from file
    		
    	} catch(FileNotFoundException ex) {
    		System.out.println("ERROR: File not found/invalid file.");
    		System.exit(0);
    	}
    } // Minesweeper
    
// ____________________________BELOW IS USER INPUT CONSTRUCTOR_______________________________///
    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * <code>rows</code> and <code>cols</code> values as the game grid's number
     * of rows and columns respectively. Additionally, One quarter (rounded up) 
     * of the squares in the grid will will be assigned mines, randomly.
     *
     * @param rows the number of rows in the game grid
     * @param cols the number of cols in the game grid
     */
    public Minesweeper(int rows, int cols) {
    	Random xPosGenerator = new Random();
    	Random yPosGenerator = new Random();
    	if(!(rows > 0 && rows <= 10 && cols > 0 && cols <= 10)) {
    		System.out.println();
    		System.out.println("ಠ_ಠ says, \"Cannot create a mine field with that many rows and/or columns!\"");
    		System.exit(0);
    	} else {
    		doesHaveMineArray = new boolean[rows][cols]; // all initialized to false
			minesweeperArray = new String[rows][cols]; // all initialized to "   "
			noFogArray = new String[rows][cols]; // all initialized to "   "
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < cols; j++) {
					doesHaveMineArray[i][j] = false;
					minesweeperArray[i][j] = "   ";
					noFogArray[i][j] = "   ";
				}
			} // initialization of 2d arrays
    	}
    	
    	this.totalMines = (int)Math.ceil(rows*cols*.10);
    	int oldRow = 0;
    	int oldCol = 0;
    	for(int i = 0; i < this.totalMines; i++) {
    		int row = xPosGenerator.nextInt(rows);
    		int col = yPosGenerator.nextInt(cols);
    		if(i > 0) {
    			if(row == oldRow && col == oldCol) {
    				i--;
    				continue;
    			}
    		}
    		oldRow = row;
    		oldCol = col;
    		doesHaveMineArray[row][col] = true;
    	} // populates with mines in random locations. total num of mines == 10% of num of spaces

    } // Minesweeper
    

// _____________________________BELOW IS THE main() METHOD________________________________///
    /**
     * The entry point into the program. This main method does implement some
     * logic for handling command line arguments. If two integers are provided
     * as arguments, then a Minesweeper game is created and started with a 
     * grid size corresponding to the integers provided and with 10% (rounded
     * up) of the squares containing mines, placed randomly. If a single word 
     * string is provided as an argument then it is treated as a seed file and 
     * a Minesweeper game is created and started using the information contained
     * in the seed file. If none of the above applies, then a usage statement
     * is displayed and the program exits gracefully. 
     *
     * @param args the shell arguments provided to the program
     */
    public static void main(String[] args) {

	/*
	  The following switch statement has been designed in such a way that if
	  errors occur within the first two cases, the default case still gets
	  executed. This was accomplished by special placement of the break
	  statements.
	*/
 
	Minesweeper game = null;

	switch (args.length) {

        // random game
	case 2: 

	    int rows, cols;

	    // try to parse the arguments and create a game
	    try {
		rows = Integer.parseInt(args[0]);
		cols = Integer.parseInt(args[1]);
		game = new Minesweeper(rows, cols);
		break;
	    } catch (NumberFormatException nfe) { 
		// line intentionally left blank
	    } // try

	// seed file game
	case 1: 

	    String filename = args[0];
	    File file = new File(filename);

	    if (file.isFile()) {
		try {
			game = new Minesweeper(file);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found/invalid file.");
			System.exit(0);
		}
		break;
	    } // if
    
        // display usage statement
	default:

	    System.out.println("Usage: java Minesweeper [FILE]");
	    System.out.println("Usage: java Minesweeper [ROWS] [COLS]");
	    System.exit(0);

	} // switch

	// if all is good, then run the game
    game.run();
    	
    } // main
    
// ___________________________BELOW IS THE run() METHOD________________________________///    
    
    /**
     * Starts the game and execute the game loop.
     */
    public void run() {
    	welcome();
    	while(true) {
    		newTurn();
    		if(hasWon()) {
    			break;
    		}
    	}
    	win();
    } // run
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// HELPER METHODS ///////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
// ________________BELOW ARE ALL THE DRAWING METHODS AND THE score() METHOD__________________///
    
    
// _________________________________welcome()   
    /**
     * Prints the welcome message for the Minesweeper game.
     */
    private void welcome() {
    	System.out.println("        _");
    	System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __");
    	System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|");
    	System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |");
    	System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|");
    	System.out.println("                                     ALPHA |_| EDITION");
    	System.out.println();
    }
    
// _________________________________gameOver()    
    /**
     * Prints the game over message, exits game.
     */
    private void gameOver() {
    	System.out.println();
    	System.out.println(" Oh no... You revealed a mine!");
    	System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __ ");
    	System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
    	System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |   ");
    	System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|   ");
    	System.out.println(" |___/                                            ");
    	System.out.println();
    	System.exit(0);
    }
    
// _________________________________win()    
    /**
     * Prints the you win message, exits game.
     */
    private void win(){
    	System.out.println();
    	System.out.println(" ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░	\"So Doge\"");
    	System.out.println(" ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░");
    	System.out.println(" ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░	\"Such Score\"");
    	System.out.println(" ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░");
    	System.out.println(" ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░	\"Much Minesweeping\"");
    	System.out.println(" ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░");
    	System.out.println(" ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░	\"Wow\"");
    	System.out.println(" ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░");
    	System.out.println(" ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░");
    	System.out.println(" ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░");
    	System.out.println(" ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░");
    	System.out.println(" ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌");
    	System.out.println(" ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░");
    	System.out.println(" ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░");
    	System.out.println(" ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░");
    	System.out.println(" ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░");
    	System.out.println(" ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░	CONGRATULATIONS!");
    	System.out.println(" ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░	YOU HAVE WON!");
    	System.out.println(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░	SCORE: " + score());
    	System.out.println("");
    	System.exit(0);
    }
 
// _________________________________score()
    /**
     * Calculates score.
     */
    private int score() {
    	return minesweeperArray.length*minesweeperArray[0].length - totalMines - roundsCompleted;
    }

// ________________BELOW ARE ALL THE HELPER METHODS FOR newTurn() AND newTurn() METHOD_________________////
    
    
// _________________________________newTurn()    
    /**
     * Prints user interface needed for each turn.
     */
    private void newTurn() {
    	printRoundsCompleted();
//    	printDoesHaveMineGrid(); // commented out b/c only implemented for testing
    	if(noFog) {
    		printNoFogGrid();
    		noFog = false;
    	} else {
    		printMinesweeperGrid();
    	}
    	printBash();
    	takeCommand();
    }
    
// _________________________________printMinesweeperGrid()    
    /**
     * Prints the minesweeper grid the user sees. 
     */
    private void printMinesweeperGrid() {
    	for(int i = 0; i < minesweeperArray.length; i++) {
    		System.out.print(i + " |");
    		for(int j = 0; j < minesweeperArray[i].length; j++) {
    			System.out.print(minesweeperArray[i][j]);
    			if(j < minesweeperArray[i].length-1) {
    				System.out.print("|");
    			}
    		}
    		System.out.println("|");
    	}
    	System.out.print("    ");
    	for(int i = 0; i < minesweeperArray[0].length; i++) {
    		System.out.print(i + "   ");
    	}
    	System.out.println();
    }
    
// _________________________________printRoundsCompleted()    
    /**
     * Prints the total num of rounds completed
     */
    private void printRoundsCompleted() {
    	System.out.println("Rounds Completed: " + roundsCompleted);
    	System.out.println();
    }
    
// _________________________________printBash()    
    /**
     * Prints pseudo-bash line
     */
    private void printBash() {
    	System.out.println();
    	System.out.print("minesweeper-alpha$ ");
    }
    
// _________________________________isInBounds()   
    /**
     * Indicates whether or not the square is in the game grid.
     * @param row the row index of the square
     * @param col the column index of the square
     * @return true if the square is in the game grid; false otherwise
     */
    private boolean isInBounds(int row, int col) {
    	return (row >= 0 && row < doesHaveMineArray.length && col >= 0 && col < doesHaveMineArray[0].length);
    }
    
// _________________________________printCommandNotRecognized()    
    /**
     * Prints command not recognized message.
     */
    private void printCommandNotRecognized() {
    	System.out.println();
		System.out.println("ಠ_ಠ says, \"Command not recognized!\"");
		System.out.println();
    }
    
// _________________________________getNumAdjMines()    
    /**
     * Returns the number of mines adjacent to the specified
     * square in the grid. 
     * @param row the row index of the square
     * @param col the column index of the square
     * @return the number of adjacent mines
     */
    private int getNumAdjMines(int row, int col) {
    	int numAdjMines = 0;
    	for(int i = row-1; i <= row+1; i++) {
    		if(!(i >= 0 && i < minesweeperArray.length)) {
    			continue;
    		}
    		for(int j = col-1; j <= col+1; j++) {
    			if((i == row && j == col) || (!(j >= 0 && j < minesweeperArray[0].length))) {
    				continue;
    			} else {
    				if(doesHaveMineArray[i][j]) {
    					numAdjMines++;
    				}
    			}
    		}
    	}
    	return numAdjMines;
    }
    
// _________________________________hasWon()   
    /**
     * Checks to see if the user has met the criteria for winning the game.
     */
    private boolean hasWon() {
    	boolean allMinesRevealed = true;
    	boolean allSquaresRevealed = true;
    	for(int i = 0; i < minesweeperArray.length; i++) {
    		for(int j = 0; j < minesweeperArray[0].length; j++) {
    			if(doesHaveMineArray[i][j]) {
    				if(minesweeperArray[i][j] != " F ") {
    					allMinesRevealed = false;
    				}
    			} else {
    				if(minesweeperArray[i][j] == " F " || minesweeperArray[i][j] == " ? " || minesweeperArray[i][j] == "   ") {
    					allSquaresRevealed = false;
    				}
    			}
    		}
    	}
    	return allMinesRevealed && allSquaresRevealed;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////// TESTING USE ONLY /////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
   
// _________________________________takeCommand()    
    /**
     * Depending on command, different actions are taken on the minefield/in the game.
     */
    public void takeCommand() {
    	Scanner kb = new Scanner(System.in);
    	String inputString = kb.nextLine().trim();
    	Scanner keyboard = new Scanner(inputString);
    	String command = keyboard.next().trim();
    	
    	switch(command) {
    	case "h":
    	case "help":
    		help();
    		break;
    	case "q":
    	case "quit":
    		quit();
    		break;
    	case "g":
    	case "guess":
    		if(!guess(keyboard)) {
    			roundsCompleted++;
    			printCommandNotRecognized();
    		}
    		break;
    	case "m":
    	case "mark":
    		if(!mark(keyboard)) {
    			roundsCompleted++;
    			printCommandNotRecognized();
    		}
    		break;
    	case "r":
    	case "reveal":
    		if(!reveal(keyboard)) {
    			roundsCompleted++;
    			printCommandNotRecognized();
    		}
    		break;
    	case "nofog":
    		noFog();
    		break;
    	default:
    		this.roundsCompleted++;
    		printCommandNotRecognized();
    	}
    }
    
// _______________________________________help()    
    /**
     * Shows all the options available to the user.
     */
    private void help() {
    	this.roundsCompleted++;
    	System.out.println();
    	System.out.println("Commands Available...");
    	System.out.println(" - Reveal: r/reveal row col");
    	System.out.println(" -   Mark: m/mark   row col");
    	System.out.println(" -  Guess: g/guess  row col");
    	System.out.println(" -   Help: h/help");
    	System.out.println(" -   Quit: q/quit");
    	System.out.println();
    }

//____________________________________________quit()    
    /**
     * Quits the game and displays a quit message.
     */
    private void quit() {
    	System.out.println();
		System.out.println("ლ(ಠ_ಠლ)");
		System.out.println("Y U NO PLAY MORE?");
		System.out.println("BYE!");
		System.exit(0);
    }
    
//____________________________________________guess()
    /**
     * Marks the indicated square with a question mark, indicating a possible mine location. 
     * @param keyboard the keyboard that is taking the command line input
     * @return false if the coordinates were formatted incorrectly or out of bounds
     */
    private boolean guess(Scanner keyboard) {
    	boolean didAccomplishGuess = false;
    	int guessedRow = 0;
		int guessedCol = 0;
		if(keyboard.hasNextInt()) {
			guessedRow = keyboard.nextInt();
			if(keyboard.hasNextInt()) {
				guessedCol = keyboard.nextInt();
				if(!keyboard.hasNextInt() && isInBounds(guessedRow, guessedCol)) {
					didAccomplishGuess = true;
					roundsCompleted++;
					minesweeperArray[guessedRow][guessedCol] = " ? ";
					System.out.println();
				}
			}
		}
		return didAccomplishGuess;
    }
    
//____________________________________________mark()    
    /**
     * Marks the indicated square with the letter F, indicating a definite mine location.
     * @param keyboard the keyboard that is taking the command line input
     * @return false if the coordinates were formatted incorrectly or out of bounds
     */
    private boolean mark(Scanner keyboard) {
    	boolean didAccomplishMark = false;
    	int markedRow = 0;
		int markedCol = 0;
		if(keyboard.hasNextInt()) {
			markedRow = keyboard.nextInt();
			if(keyboard.hasNextInt()) {
				markedCol = keyboard.nextInt();
				if(!keyboard.hasNextInt() && isInBounds(markedRow, markedCol)) {
					didAccomplishMark = true;
					roundsCompleted++;
					minesweeperArray[markedRow][markedCol] = " F ";
					System.out.println();
				}
			}
		}
		return didAccomplishMark;
    }
    
//____________________________________________reveal()   
    /**
     * Reveals a square, showing either the num of adjacent mines, or a mine itself which would end the game.
     * @param keyboard the keyboard that is taking the command line input
     * @return false if the coordinates were formatted incorrectly or out of bounds
     */
    private boolean reveal(Scanner keyboard) {
    	boolean didAccomplishReveal = false;
    	int revealedRow = 0;
		int revealedCol = 0;
		if(keyboard.hasNextInt()) {
			revealedRow = keyboard.nextInt();
			if(keyboard.hasNextInt()) {
				revealedCol = keyboard.nextInt();
				if(!keyboard.hasNextInt() && isInBounds(revealedRow, revealedCol)) {
					didAccomplishReveal = true;
					roundsCompleted++;
					if(!doesHaveMineArray[revealedRow][revealedCol]) {
						minesweeperArray[revealedRow][revealedCol] = " " + getNumAdjMines(revealedRow, revealedCol) + " ";
						System.out.println();
					} else {
						gameOver();
					}
				}
			}
		}
		return didAccomplishReveal;
    }
    
//____________________________________________noFog()    
    /**
     * Shows positions of all of the mines, for the next round only.
     */
    private void noFog() {
    	roundsCompleted++;
    	noFog = true;
    	System.out.println();
    }
    
//____________________________________________printNoFogGrid()    
    /**
     * Prints noFogArray.
     */
    private void printNoFogGrid() {
    	for(int i = 0; i < noFogArray.length; i++) {
    		System.out.print(i + " |");
    		for(int j = 0; j < noFogArray[i].length; j++) {
    			if(doesHaveMineArray[i][j]) {
    				System.out.print("<" + minesweeperArray[i][j].substring(1, 2) + ">");
    			} else {
    				System.out.print(minesweeperArray[i][j]);
    			}
    			if(j < noFogArray[i].length-1) {
    				System.out.print("|");
    			}
    		}
    		System.out.println("|");
    	}
    	System.out.print("    ");
    	for(int i = 0; i < minesweeperArray[0].length; i++) {
    		System.out.print(i + "   ");
    	}
    	System.out.println();
    }


// _________________________________TESTING USE ONLY________________________________///
   
    
//_____________________________________printDoesHaveMineGrid()    
    /**
     * Used for testing. Prints doesHaveMineArray.
     */
    private void printDoesHaveMineGrid() {
    	for(int i = 0; i < doesHaveMineArray.length; i++) {
    		System.out.print(i + "|");
    		for(int j = 0; j < doesHaveMineArray[i].length; j++) {
    			if(doesHaveMineArray[i][j] == true) {
    				System.out.print(1);
    			} else {
    				System.out.print(0);
    			}
    			if(j < doesHaveMineArray[i].length-1) {
    				System.out.print("|");
    			}
    		}
    		System.out.println("|");
    	}
    	System.out.print("  ");
    	for(int i = 0; i < doesHaveMineArray[0].length; i++) {
    		System.out.print(i + " ");
    	}
    	System.out.println();
    	System.out.println();
    }


} // Minesweeper