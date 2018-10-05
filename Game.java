import java.awt.Point;
import java.util.*;
import java.lang.Math;

public class Game{

	public Player Black;
	public Player White;
	public Board gameBoard;
	public int turnCounter;

	//Constructor for the game, which will initalise the players, and the count.
	
	public Game(String gameType) throws InterruptedException{

		Black = new NamedPlayer('B');
		System.out.println("You are playing with Black\n");

		Thread.sleep(2000);

		if(gameType.equals("H")){ 
			White = new NamedPlayer('W'); 
			System.out.println("You are playing with White\n");
		}else{ 
			White = new ComputerPlayer('W'); 
			System.out.println("Computer playing with White\n");
		}

		Thread.sleep(2000);
		
		System.out.println("White to move first.\n");

		Thread.sleep(1000);

		gameBoard = new Board(Black, White); 

		//This needs to be reset to 0 when we start, just 1 for testing purposes.
		turnCounter = 1;

	}

	public void Turn(Player myPlayer, Player Oppo) throws InterruptedException{

		//Find out the move wanting to be made, check if it is a move or a jump and executing accordingly.
		//Calling methods from the Board class to see if the move is allowed, and then preforming it.

		boolean moveBeenMade = false;

		while(moveBeenMade == false){

			Move M = myPlayer.getMove(gameBoard, Oppo);

			boolean moveIsPoss = gameBoard.isMovePossible(myPlayer,M);
			boolean jumpIsPoss = gameBoard.isJumpPossible(myPlayer,M);

			if(moveIsPoss == true){

				System.out.println("\nMoving " + M.c.getColour() + " from " + (M.start.x+1) + "," + (M.start.y+1) + " to " 
				+ (M.end.x+1) + "," + (M.end.y+1) + "\n");

				Thread.sleep(500);

				this.gameBoard.executeMove(M);
				moveBeenMade = true;

			}
			else if(jumpIsPoss == true){

				System.out.println("\nJumping " + M.c.getColour() + " from " + (M.start.x+1) + "," + (M.start.y+1) + " to " 
				+ (M.end.x+1) + "," + (M.end.y+1) + "\n");

				Thread.sleep(500);

				this.gameBoard.executeJump(M);

				//Check for a second jump being possible.

				boolean doubleJumpPossible = false;

				do{
					
					doubleJumpPossible = false;
					ArrayList<Move> doubleJumpOptions = this.gameBoard.possibleJumps(myPlayer);

					if(doubleJumpOptions.size() != 0){

						for(Move SecondJump: doubleJumpOptions){

							//If there is a double jump posible, execute it, and loop to check there are 

							if(SecondJump.start.equals(M.end)){

								doubleJumpPossible = true;

								System.out.println("\nJumping " + SecondJump.c.getColour() + " from " + (SecondJump.start.x+1) + "," + (SecondJump.start.y+1) + " to " 
								+ (SecondJump.end.x+1) + "," + (SecondJump.end.y+1) + "\n");

								Thread.sleep(500);

								this.gameBoard.executeJump(SecondJump);
							}
						}
					}
				}while(doubleJumpPossible == true);

				moveBeenMade = true;
			}

			else{
				System.out.println("Please enter a valid move\n");

				Thread.sleep(1000);
			}

		}

	}


	//Check if both players still have pieces, if not then return false and display the correct message.

	public boolean isGameFinished(){
		if(this.gameBoard.numberOfPieces(this.Black) == 0){
			System.out.println("White Wins");
			return true;
		}else if(this.gameBoard.numberOfPieces(this.White) == 0){
			System.out.println("Black Wins");
			return true;
		}else{
			return false;
		}
	}



	public static void main(String[] args) throws InterruptedException{

		//Introduction. 

		System.out.println("Welcome to the game of Draughts.\n");
		Thread.sleep(1500);
		System.out.println("Moves are made by inputting the row and column of the square you want to move");
		System.out.println("from and the row and column of the square you want to move to.\n");
		Thread.sleep(3000);
		System.out.println("If you miss-enter a square the program will allow you to enter the square again.\n");
		Thread.sleep(2000);
		System.out.println("Best of luck.\n");
		Thread.sleep(1000);

		/*Offer the user a choice of whether they would like to play a human or computer.*/

		Scanner sinput = new Scanner(System.in);
		String typeOfGame = "H";
		System.out.println("\nWould you like to play the computer, or another human:\nPlease enter 'C' or 'H'");

		try{
			typeOfGame = sinput.next();
		}catch(InputMismatchException e){
			System.out.println("Please enter the exact phase here: ");
			typeOfGame = sinput.next();
		}

		Thread.sleep(500);

		/*Create the game, and print out the board*/

		Game G = new Game(typeOfGame); 
		Thread.sleep(500);
		G.gameBoard.printBoard();

		/*Loop, while both players have pieces, compute who's move it is, then execute their move and print the board.*/

		boolean gameDone = false;

		while(gameDone == false){

			if(G.turnCounter % 2 == 0){
				System.out.println("Black to move.");
				Thread.sleep(500);
				G.Turn(G.Black, G.White);
			}else{
				System.out.println("White to move.");
				Thread.sleep(500);
				G.Turn(G.White, G.Black);
			}

			//Add one to the count, and print out the board.
			//Check if any kings have been born, and if the game is finished.
			
			G.turnCounter++;
			G.gameBoard.kingBirth();
			Thread.sleep(500);
			G.gameBoard.printBoard();
			gameDone = G.isGameFinished();
			Thread.sleep(500);

		}
	}
}
