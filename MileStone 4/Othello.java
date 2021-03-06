import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 
 * @author Joel Prakash 100910302
 * @author Himanish Kaushal 100846382
 * This class contains all the logic behind the Othello game
 */
public class Othello extends Game implements java.io.Serializable {
	
	// HELP_NEEDED is an int which is entered by the user to request help
	// PASS is an int which is entered by the user to pass their turn
	// ZERO is an int which will be used to control which player gets a turn
	// integer constant representing zero, and help needed by human
		private static final int ZERO = 0,
	            HELPNEEDED = -1,
	            REDO = -2,
				 UNDO = -3,
				 SAVE = -4,
				 LOAD = -5;
		
	private DeepClone s;
	// Booleans to indicate if the AI or Human has passed their turn
	private boolean humanPassed = false,
					AIPassed = false,
					helpRequired,
					undoRequired,
					redoRequired,
					saveRequired,
					loadRequired;

	protected  Stack<OthelloBoard> undo;
	protected Stack<OthelloBoard> redo;
	
	private int boardSize = getBoard().getSize();
	
		
		// String constants representing the draws and winners
		private static final String DRAWSTRING = "\n\nThe game has finished in a draw!\n\n",
		                            WINNERSTRING = "\n\nThe WINNER is: ",
		                            INVALIDOPTION = "Invalid Option",
		                            PASS_STRING = "The turn was passed.",
		                             FILENAME = "OthelloHelp.txt",
                                      SAVEFILE = "OhtelloFile.txt";
	
	public Othello(Board board)
	{
		super(board);
		this.helpRequired = false;
		this.undoRequired = false;
		this.redoRequired = false;
		this.saveRequired = false;
		this.loadRequired = false;
		this.undo = new Stack<OthelloBoard>();
		this.redo = new Stack<OthelloBoard>();
	}
	/**
	 * This function checks the board for all possible empty squares that is valid to make a move to
	 * and returns these squares as a list. 
	 * @param player
	 * @return ArrayList<Square>
	 */
	public ArrayList<Square> validSquares(Player player) {
		
		// the opposing player, who ever it is 
		Player opponent;
		ArrayList<Square> validSquares = new ArrayList<Square>();
		
		
		//getPlayer hold a list of all possible players. So this part finds out 
		// which player has been passed in this function's argument and then it gets
		// that player's opponent
		//This function accomplishes that in the following step:
		//1. For any given square on the board, we assigned 8 directions that represent
		//   the 8 squares that surround any given square on the board.
		//2. Find all the surrounding squares that contain the opponent's piece
		//3. Traverse in that direction until we find a empty square or if we find one
		//   of our pieces before then come back and try a different direction
		if(player.equals(getPlayerAt(0)))
			opponent = getPlayerAt(1);
		else 
			opponent = getPlayerAt(0);
		
		// check rows
		for(int r = 0; r < getBoard().getSize(); r++) {
			//check col
			for(int c = 0; c < getBoard().getSize(); c++) {
				
				
				// finds all the squares on the board that contain the player passed in the argument of this function
				if(getBoard().isSquareOccupied(r, c) && ((OthelloBoard) getBoard()).getSquare(r, c).checkPlayerOccupied().equals(player)) {
					
					// direction represents the 8 squares in a list form, surrounding a give square on the board. 
					//this is true for all squares on the board, except the squares on the edges and corners,
					boolean[] direction = validDirections(r, c, opponent);
					
					Square sq;
					for(int j = 0; j < 8; j++) {
						
						// if that direction had the opponent's piece
						if(direction[j]) {
							
							// we traverse that direction to find the empty square if it has it. Otherwise  function returns null
							sq = traverseDir(r, c, j, player);
							// if there is an empty square
							if(sq != null) {
								
						      // if our list(validSquare) doesn't already contain this particular square then we add it.
								if(!validSquares.contains(sq)) {
									validSquares.add(sq);
									
								} // end if 
							} // end if
						} // end if
					} // end for
				
							
				} // end if
			} // end for
		} // end for
		
		return validSquares;
	}
	
	public ArrayList<int[]> getCoordinates(Player p)
	{
		ArrayList<int[]> array_int = new ArrayList<int[]>();
		ArrayList<Square> sq = this.validSquares(p);

		for(Square s : sq)
		{
			int x[] = {s.getX(),s.getY()};
			array_int.add(x);
		}
		
		return array_int;
	}
	
	
	
	private boolean[] validDirections(int r, int c, Player opponent) {
		
		boolean[] direction = new boolean[8];
		int dirIndex = 0;
		// initalises all the directions to be false. the first step taken by this function is to get
		// all the surrounding squares that have opposing pieces on them than the given square.
		// if that square which surrounds the given square contains no opposing piece then that direction is
		//set to false. otherwise it will be set to true
		for(int i = 0; i < 8; i++)
			direction[i] = false;
		
		
		// iterating through all the squares surrounding one given square on the board, including the given square
		for(int r2 = r-1; r2 < r + 2; r2++) {
			for(int c2 = c-1; c2 < c + 2; c2++) {
				
				
				// only if the square lies inside the board
				if(isInBounds(r2, c2)) {
					// if that square is not the given square.
					if(!(r2 == r && c2 == c)) {
					// if the surrounding places are not empty
						if(getBoard().isSquareOccupied(r2, c2)) { 
							// if the opponent's piece lies in one of them
							if(((OthelloBoard) getBoard()).getSquare(r2, c2).checkPlayerOccupied().equals(opponent)) {
							// set the direction to true, since we will be traversing in that direction
								direction[dirIndex] = true;
								
							} // end if
						} // end if
					} // end if
				} // end if
				
				// once we look at one of the surrounding squares, go to the next one
				if(!(r2 == r && c2 == c)) {
					
					dirIndex++;
				}
				
			} // end for
		} // end for
		
		return direction;
	}
	
	/**
	 * This function traverses in just one direction to find if there is an empty square to place the player's marker
	 * @param r
	 * @param c
	 * @param dirIndex
	 * @param player
	 * @return Square
	 */
	public Square traverseDir(int r , int c, int dirIndex, Player player) {
		
	//the no that always needs to be added to travel in a particular direction on the board, form one given square
	// to the other
		int incR = calcIncR(dirIndex);
		int incC = calcIncC(dirIndex);
		
		// is in the board
		while(isInBounds(r + incR, c + incC)) {
			// we will continue to travel in that direction as long as we find opposing marker's in that direction
			r = r + incR; c = c + incC;
			
			// if we come across any marker's similar to the player's one to stop. 
			if(getBoard().isSquareOccupied(r, c) && 
					((OthelloBoard) getBoard()).getSquare(r, c).checkPlayerOccupied().equals(player)) {
				
				break;
				
			// if we find an empty space then return that	
			} else if (!getBoard().isSquareOccupied(r, c))
				return ((OthelloBoard) getBoard()).getSquare(r ,c);
			
		} // end while
		
		return null;
	}
	
	/**
	 *This function traverses in a given direction and makes sure that the player's marker is placed in the end of the path
	 *this confirms that there is a sandwich in that path
	 * @param r
	 * @param c
	 * @param dirIndex
	 * @param player
	 * @return boolean
	 */

	public boolean traverseDirForFlip(int r , int c, int dirIndex, Player player) {
		
		int incR = calcIncR(dirIndex);
		int incC = calcIncC(dirIndex);
		
		while(isInBounds(r + incR, c + incC)) {
			
			r = r + incR; c = c + incC;
			
			// at the end of the path if the player's marker is present then return true
			if(getBoard().isSquareOccupied(r, c) && 
					((OthelloBoard) getBoard()).getSquare(r, c).checkPlayerOccupied().equals(player)) {
				
				return true;
			}
			
		} // end while
		
		return false;
	}
	
	/**
	 *This function handles all the flipping in a given direction here the player is actually the opponent's marker in the game 
	 * @param r
	 * @param c
	 * @param dirIndex
	 * @param player
	 */
	
	public void flip(int r , int c, int dirIndex, Player player) {
		
		int incR = calcIncR(dirIndex);
		int incC = calcIncC(dirIndex);
		
		do {
			
			r = r + incR; c = c + incC;
			
			System.out.println("new r: " + r);
			System.out.println("new c: "+ c);
			
			System.out.println("bool 1: " + getBoard().isSquareOccupied(r, c));
			System.out.println("bool 2: " + !((OthelloBoard) getBoard()).getSquare(r, c).checkPlayerOccupied().equals(player));
		
			// calls flip square that changes a opponent's maerker residing in one square to that of the player's marker
			((OthelloBoard) getBoard()).flipSquare(r, c, player);
			// keep flipping as long as we encounter the opponent's markers
		} while (getBoard().isSquareOccupied(r, c) && 
				!((OthelloBoard) getBoard()).getSquare(r, c).checkPlayerOccupied().equals(player));
		
	}


	/**
	 * This function deals with all the flipping that get's done once a player places his marker on the board
	 * @param row
	 * @param col
	 * @param player
	 */
	// the starting point for this function is the square that the marker was places
	public void chainReaction(int row, int col, Player player) {
		//this part is similar to validSquare function
		Player opponent;
		
		System.out.println("Row: "+ row);
		System.out.println("Col: "+ col);
		
		if(player.equals(this.getPlayerAt(0)))
			opponent = getPlayerAt(1);
		else 
			opponent = getPlayerAt(0);
		
		boolean[] direction = validDirections(row, col, opponent);
		
		for(int j = 0; j < 8; j++) {
			
			if(direction[j]) {
				// traverses in a given direction flipping all opponet's markers, which then turn into the player's markers
				if(traverseDirForFlip(row, col, j, player)) {
					
					flip(row, col, j, player);
			
				} // end if
			} // end if
		} // end for
	}
	
	/**
	 * This function along with calcIncC is responsible for getting the values needed for getting from one square to the 
	 * other in a particular direction
	 * @param dirIndex
	 * @return int 
	 */
	
	public int calcIncR(int dirIndex) {
		
		if(dirIndex == 0 || dirIndex == 1 || dirIndex == 2)
			return -1;
		
		if(dirIndex == 3 || dirIndex == 4)
			return 0;
		
		return 1;
	}
	
	/**
	 *This function along with calcIncR is responsible for getting the values needed for getting from one square to the
	 *other in a particular direction
	 * @param dirIndex
	 * @return int
	 */
	
    public int calcIncC(int dirIndex) {
		
    	if(dirIndex == 0 || dirIndex == 3 || dirIndex == 5)
    		return -1;
    	
    	if(dirIndex == 1 || dirIndex == 6)
    		return 0;
    	
    	return 1;
		
	}
    
	/**
	 * this function checks to see if the square lies inside the board
	 * @param row
	 * @param col
	 * @return boolean
	 */
	
	public boolean isInBounds(int row, int col) {
		
		return (row >= 0 && row < getBoard().getSize() && col >= 0 && col < getBoard().getSize());
	}
	
	
	/**
	 * This function counts the score of any given player by counting the number of that player's marker's on the board
	 * @param player
	 * @return int
	 */
	public int countScore(Player player) {
		
		int count = 0;
		for(int r = 0; r < boardSize; r++)
			for(int c = 0; c < boardSize; c++)
				// count all the player's marker's on the board
				if(((OthelloBoard) getBoard()).getSquare(r, c).checkOccupied() 
						&& ((OthelloBoard) getBoard()).getSquare(r, c).checkPlayerOccupied().equals(player))
					count++;
		
		return count;
	}
	
	public int countScore2()
	{
		int AI;
		int Human;
		AI = countScore(getAllPlayers().get(1));
		Human = countScore(getAllPlayers().get(0));
		return AI - Human;
	}
	
	/**
	 * Fuction handles the printing of the score of both players in the game
	 */
	public void printScore() {
		
		System.out.println("Score Board: \n"
				+ getPlayerAt(0).toString() + "(O)" + " = " + countScore(getPlayerAt(0)) + "\n"
				+ getPlayerAt(1).toString() + "(X)" + " = " + countScore(getPlayerAt(1))); 
							
	}
	/**
	 * Sets up the board when a new game is played.
	 */
	public void setup(){
		Board b = this.getBoard();
		for(int i =0; i<b.getSize();i++){
			for(int j=0; j<b.getSize();j++){
				
				b.resetSquare(i, j);
			}
		}
		Player human = this.getPlayerAt(0);
		Player ai = this.getPlayerAt(1);
		int i = this.boardSize/2 - 1;
		b.setSquare(i, i, human);
		b.setSquare(i + 1, i + 1,human );
		
		b.setSquare(i + 1, i,ai );
		b.setSquare(i,  i + 1, ai);
		
	}
	
	public void load_Board(Board board)
	{
		Board b = this.getBoard();
		Player human = this.getPlayerAt(0);
		Player ai = this.getPlayerAt(1);
		
		for(int i =0; i<b.getSize();i++){
			for(int j=0; j<b.getSize();j++){
				b.resetSquare(i, j);
				
				if(board.getSquare(i, j).checkOccupied() == true)
				{
				Player p =board.getSquare(i, j).checkPlayerOccupied();
				if(p instanceof Human)
				{
				b.setSquare(i, j, human);
				}
				
				else if(p instanceof OthelloMiniMaxAI || p instanceof AI || p instanceof Alt_OthelloAI)
				{
					b.setSquare(i, j, ai);
				}
				
				}
				
				
			
			}
		}
		
	}
	
	@Override
	/**
	 * This function call all the other functions in the class inorder to play the game
	 */
	
	public void play()
	{ 
	}

	
	/**
	 * Checks the winner of the game.
	 * 
	 * @return player -> Player instance representing the winner of 
	 *                   the game or null if no winner.
	 *                   
	 */

	@Override
	protected Player checkWinner()
	{
		Player human = getPlayerAt(0);
		Player ai = getPlayerAt(1);
		
		
		// once both the players pass in a row then the game ends and the winner is found
		if(getBoard().isFull() || (validSquares(human).isEmpty() && validSquares(ai).isEmpty())
				|| countScore(human) == 0 || countScore(ai) == 0) {
			
			int humanScore = countScore(human),
				AIScore = countScore(ai);
			
			if(humanScore > AIScore)
				return human;
			else
				return ai;
		}
		
		return null;
	}
	

	
	
	
		
		
	@Override	
	public void makeMove(int currRow,int  currCol,Player player, Game game)
	{		
		super.makeMove(currRow,currCol,player,game);
		chainReaction(currCol, currCol, player);
	}
	

	
	/**
	 * Checks if a move is valid or not
	 * 
	 * @return move -> Boolean representing if the move is
	 *                 valid or not.
	 * 
	 */
	
	protected boolean checkMove(int move_row, int move_col,Player player)
	{	
		ArrayList<Square> validSquares = validSquares(player); 
		
	
		
		
		// if the row and column are less then board size and the square isn't occupied, then
		if (isInBounds(move_row, move_col) && validSquares.contains(((OthelloBoard) getBoard()).getSquare(move_row, move_col)))
		{
			
			// set the appropriate square to be occupied by the player
			this.getBoard().setSquare(move_row, move_col, player);
		
			chainReaction(move_row, move_col, player);
			return true;
		}
		
		
		//System.out.println(move_row + " " + move_col);
		return false;
	}
	
	protected boolean correctMove(int move_row, int move_col,Player player)
	{
      ArrayList<Square> validSquares = validSquares(player); 
		
	
		
		
		// if the row and column are less then board size and the square isn't occupied, then
		if (isInBounds(move_row, move_col) && validSquares.contains(((OthelloBoard) getBoard()).getSquare(move_row, move_col)))
		{
			
			return true;
		}
		
	
		return false;
	}
	
	public void makeAIMove(){
		
		int row, col;
		do {
			
			row = this.getPlayerAt(1).chooseRow();
			col = this.getPlayerAt(1).chooseCol();
			System.out.println("3:\n" + new OthelloBoard(this.getBoard()).toString());
		} while (!this.checkMove(row,col,this.getPlayerAt(1)));
	
		System.out.println("4:\n" + new OthelloBoard(this.getBoard()).toString());
		
	}
	
	public boolean canAIPass(){
		if(validSquares(getPlayerAt(1)).isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if there is a draw in the game
	 * 
	 * @return draw -> Boolean representing if there is 
	 *                 a draw in the game.
	 *                 
	 */

	@Override
	protected boolean checkDraw()
	{
		// get the board
		Board b = this.getBoard();
		
		int humanScore = countScore(getPlayerAt(0)),
				AIScore = countScore(getPlayerAt(1));
		
		// check if the board is full and there is no winners
		if (b.isFull() && humanScore == AIScore || (humanPassed && AIPassed && humanScore == AIScore && this.checkWinner() == null))
			return true;
		
		return false;
	}
	@Override
	public void undo() {
		// TODO Auto-generated method stub
	
		if(this.canUndo()){
			this.getRedo().push(new OthelloBoard(this.getBoard()));
		 
	
			this.load_Board(this.getUndo().pop());
		
		}
	}


	@Override
	public void redo() {
		
		if(this.canRedo()){
			this.getUndo().push(new OthelloBoard(this.getBoard()));
			
			this.load_Board(this.getRedo().pop());
			
		}
	}
	
	public  Stack<OthelloBoard> getUndo() {
		return undo;
	}


	public Stack<OthelloBoard> getRedo() {
		return redo;
	}

	public boolean canUndo(){
		return !this.undo.isEmpty();
	}
	
	public boolean canRedo(){
		return !this.redo.isEmpty();
	}
	

}
