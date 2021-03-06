***************************************************************
**************** SYSC 3110 Project - 3rd Milestone ************
********************* Team - GangOfFour ***********************
***************************************************************

Team members:
Jeton Sinoimeri 100875046
Joel Prakash 100910302
Himanish Kaushal 100846382
Varun Sriram 100869591

***NOTE***
3rd strategy explanation: The third strategy is implemented in a way that the AI
can choose from two sets of moves. The first (primary) set contains squares on
the board that are favorable according to the rules of the game and thus give the
player an advantage if occupied. If the AI is unable to occupy any of these squares
then it chooses from the second (secondary) set of squares that contain all other
squares where the AI player can move under the rules of the game.

AUTHORS and DELIVERABLES

AI.java - Varun
Inherits from Player class. Mainly used to make a move for the AI player
that the human is playing against.

AITest.java - Jeton
JUnit testing of the AI class.

*new* Alt_HoundsAndHareAI.java - Varun
The AI class that implements the third strategy for HoundsAndHare.

*new* Alt_OthelloAI.java - Varun
The AI class that implements the third strategy for Othello.

*new* Alt_tictactoe.java - Varun
The AI class that implements the third strategy for TicTacToe.

Board.java - Himanish
Specifics boards for TicTacToe and Othello inherit from this class. Contains 
information about the general behaviour of a board. Declares a 2-D array of sqaures
that represents the board, contains a method that prints a String representation of
the board. Other methods included.

BoardTest.java - Jeton
JUnit testing of the Board class.

Controller.java - Varun, Himanish
This is the controller class that is part of the MVC game set-up. It controls
the actions that are performed when a square on the board (represented by
black/white/green panels) is clicked. It also helps update the score board.

DocumentationHELP.java - Jeton
Javadoc document.

Game.java - Jeton
Othello and TicTacToe extend from this abstract class. Contains the board and a list
of players that will play the game. Contains all necessary methods that any game might
need.

GameMain.java - Varun
This class contains the main method that asks the user which game it wants to play
and initiate the game accordingly.

GameTest.java - Jeton
JUnit testing of the Game class.

*new* HelpListener.java - Varun
Implements ActionListener. This is a listener class that listens to the 'help' button in the Othello
GUI frame. 

*new* HoundsAndHare.java - Himanish, Varun
This class extends Game. Contains methods that follow and implement the rules of the game.

*new* HoundsAndHareBoard.java - Himanish
This class extends Board. Contains a different constructor that restricts certain squares according to the
hounds and hare game. Also contains other methods to move the pawns and check if the given
coordinates are in the bounds of the board.

*new* HoundsAndHareSetup.java - Varun
This class initiates the human and AI players and calls the play method on the game.

Human.java - Jeton
Implements the Player interface. Represents the human player.

HumanTest.java - Jeton
JUnit testing of the Human class.

Main.java - Varun
This is the main class.

MiniMaxAI.java - Joel, Varun
This class contains all the code that is needed to choose the AI player's next
move using mini-max FOR OTHELLO.

NewGameListener.java - Varun
Implements ActionListener. This is a listener class that listens to the 'new game' button in the Othello
GUI frame. 

Node.java - Joel, Varun
A brand new node class created to support the tree in mini-ma used to decide the 
AI player's next move.

Othello.java - Himanish, Joel
This class extends from Game. contains information about the behaviour of Othello.
It still needs refactoring due to heavy code duplication. The HELP string will return 
rules of a TicTacToe game. 

OthelloBoard.java - Himanish, Joel
Inherits from Board class. Contains Othello specific tasks to be carried out on the
board during game play. 

OthelloBoardTest.java - 
JUnit testing of the OthelloBoard class.

OthelloGrid.java - 
JUnit testing of the OthelloGrid class.

OthelloMVCSetup.java - Varun
This is the main class that links all the GUI components together and sets up the game
using the MVC pattern.

OthelloPanel.java - Varun
The panel that contains the squares of the board.

OthelloTest.java - Himanish
JUnit testing of the Othello class.

PassListener.java - Varun
Implements ActionListener. This is a listener class that listens to the 'pass' button in the Othello
GUI frame.  

Player.java - Varun
Interface contains methods needed by any player.

ScoreLabel.java - Varun
Extends JLabel. 

Square.java - Joel
Contains information about the behaviour of each square on a board.

SquareTest.java - Jeton
JUnit testing of the Square class.

TicTacToe.java - Jeton, Varun
Inherits from Game and contains information about the behaviour of the tic tac toe.

TicTacToeMiniMaxAI.java - Joel
This class contains all the code that is needed to choose the AI player's next
move using mini-max for tic tac toe.

TicTacToeTerminalSetup.java - Varun
This class initiates the human and AI players and calls the play method on the game. 

TicTacToeTest.java - Jeton
JUnit testing of the TicTacToe class.

TicTacToeBoard.java - Jeton, Varun
Inherits from Board class. Contains TicTacToe specific tasks to be carried out on the
board during game play.

TicTacToeBoardTest.java - Jeton
JUnit testing of the TicTacToeBoard class.


KNOWN ISSUES

- If the board in Othello is too big, after few turns the board gives an error that we have not
been able to figure out yet. We are still working on that. 

- Since the hounds and hare game works a little differently, it was hard to get the minimax for
the game to work. The work is still in progress and hence is not included in this 
submission.

ROADMAP AHEAD

We are going to get the mini-max implementation of the AI working perfectly. We are going to
further seperate the model, view and controller by putting them in differnt folders. We are 
going to decide on the new game that we are going to do and design the GUI component and implementation
of the rules of that game. 