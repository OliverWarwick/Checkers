import java.util.*;
import java.awt.Point;
import java.lang.Math;

public class Board{
	
	public Counter[][] board;
	private int moves;
	private Player p1;
	private Player p2; 

	public Board(Player p1, Player p2){

		//Create new board
		board = new Counter[8][8];
		moves = 0;
		this.p1 = p1;
		this.p2 = p2;

		//Populate fresh board
		PopulateNewBoard(p1, p2);

	}

	public Board(Player p1, Player p2, String s){

		//Made so we have a constructor which doesn't also populate the board.

		board = new Counter[8][8];
		moves = 0;
		this.p1 = p1;
		this.p2 = p2;

	}

	public void PopulateNewBoard(Player p1, Player p2){

		for (int q = 1; q <= 7; q += 2){ board[0][q] = new Counter(p1); }
		for (int q = 0; q <= 7; q += 2){ board[1][q] = new Counter(p1); }
		for (int q = 1; q <= 7; q += 2){ board[2][q] = new Counter(p1); }

		for (int q = 0; q <= 7; q += 2){ board[5][q] = new Counter(p2); }
		for (int q = 1; q <= 7; q += 2){ board[6][q] = new Counter(p2); }
		for (int q = 0; q <= 7; q += 2){ board[7][q] = new Counter(p2); }
		
		}

	public void printBoard(){

		//Print out the heading above each column.
		System.out.println("    1   2   3   4   5   6   7   8  ");
		System.out.println("   ------------------------------- ");

		//For each row..
		for (int i = 0; i < board.length; i++) {

			// print row number
			System.out.print((i+1) + " "); //Modification

			// print each value, if 'null' put a space, otherwise the players mark
			for (int j = 0; j < board[0].length; j++) {
				System.out.print("|");
				if(board[i][j] == null) {
					System.out.print("   ");
				}
				else if(board[i][j].getType().equals("King")){
					char kingPiece = board[i][j].getColour();
					System.out.print("\033[1m " + kingPiece + " \033[0m");
				}
				else{
				System.out.print(" " + board[i][j].getColour() + " ");
				}
			}

			// print row number again
			System.out.println("| " + (i+1));
			System.out.println("   ------------------------------- ");
		}

		// print table footer
		System.out.println("    1   2   3   4   5   6   7   8  \n");
	}

	public boolean isMovePossible(Player p, Counter c, Point start, Point end){

		//First check they are indeed moving the correct colour.

		if(this.board[start.x][start.y] != null){
			boolean isRightColour = isPlayerMovingOwnCounter(p,c);
			if(isRightColour == false){
				return false;
			}
		}

		//Provided that the moves are within the board, and that the start point
		//contains C, and the end point does not. If not then return false, if so check.

		if((start.x >= 0) && (start.x < 8) && (start.y >= 0) && (start.y < 8) && (end.x >= 0) 
		&& (end.x < 8) && (end.y >= 0) && (end.y < 8) && (this.board[start.x][start.y] != null)
		&& (this.board[start.x][start.y].equals(c)) && (this.board[end.x][end.y] == null)){
				
			//If it has type King, then the counter can move anywhere within the radius of 1.

			if(c.getType() == "King"){
				if(Math.abs(start.x - end.x) == 1 && Math.abs(start.y - end.y) == 1){
					return true;
				}
			}

			else{

				//If it is of player 1, who moves down the board, return true if only one move sidewards
				//and one move down.

				if(c.getColour() == 'B'){
					if((end.x - start.x == 1) && (Math.abs(start.y - end.y) == 1)){ return true; } 
				}

				//If it is of player 2, who moves up the board.

				else if(c.getColour() == 'W'){
					if((end.x - start.x == -1) && (Math.abs(start.y - end.y) == 1)){ return true; } 
				}
			}
		}
		return false;
	}


	public boolean isMovePossible(Player p, Move M){
		return isMovePossible(p, this.board[M.start.x][M.start.y], M.start, M.end);
	}


	public void executeMove(Counter c, Point start, Point end){
		//Check if move is valid.
		//This will be using the board co-ord not input from user.

		board[start.x][start.y] = null;
		board[end.x][end.y] = c;

	}

	public void executeMove(Move M){
		executeMove(this.board[M.start.x][M.start.y], M.start, M.end);
	}

	

	public boolean isJumpPossible(Player p, Counter c, Point start, Point end){

		//First check they are indeed moving the correct colour.

		if(this.board[start.x][start.y] != null){
			boolean isRightColour = isPlayerMovingOwnCounter(p,c);
			if(isRightColour == false){
				return false;
			}
		}

		//Need to first check, that the square is in the grid, that there is counter of
		//the player on the square, and of the other players on the adjacent square. 

		if(!(Math.abs(start.x - end.x) == 2 && Math.abs(start.y - end.y) == 2)){
			return false;
		}

		//Need to make sure the co-ords are okay to be examined in the table, and that the start is the players counter
		//and that the mid square is the opponents, and the end square is free.

		if((start.x >= 0) && (start.x < 8) && (start.y >= 0) && (start.y < 8) && (end.x >= 0) 
		&& (end.x < 8) && (end.y >= 0) && (end.y < 8) && (this.board[start.x][start.y] != null) 
		&& (this.board[start.x][start.y].equals(c)) && (this.board[(start.x + end.x)/2][(start.y + end.y)/2] != null) 
		&& (this.board[(start.x + end.x)/2][(start.y + end.y)/2].getColour() != c.getColour()) && (this.board[end.x][end.y] == null)){

			//If counter is a king, then allowed to move in any direction.

			if(c.getType() == "King"){
				if(Math.abs(start.x - end.x) == 2 && Math.abs(start.y - end.y) == 2){
					return true;
				}
			}

			else{

				//If it is of player 1, who moves down the board, return true if only two move sidewards
				//and two move down.

				if(c.getColour() == 'B'){

					if(start.x - end.x == -2 && Math.abs(start.y - end.y) == 2){ return true; }

				}

				//If it is of player 2, who moves up the board.

				else if(c.getColour() == 'W'){

					if(start.x - end.x == 2 && Math.abs(start.y - end.y) == 2){ return true; }

				}

			}
		}
		return false;

	}

	public boolean isJumpPossible(Player p, Move M){
		return isJumpPossible(p, this.board[M.start.x][M.start.y], M.start, M.end);
	}


	public void executeJump(Counter c, Point start, Point end){

		board[start.x][start.y] = null;
		board[(int) (start.x + end.x)/2][(int) (start.y + end.y)/2] = null;
		board[end.x][end.y] = c; 

	}

	public void executeJump(Move M){
		executeJump(this.board[M.start.x][M.start.y], M.start, M.end);
	}

	

	public ArrayList<Point> positionOfPieces(Player p){

		//Point[] locationArray = new Point[12];
		ArrayList<Point> locations = new ArrayList<Point>();

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(this.board[i][j] != null){
					if(this.board[i][j].getColour() == p.getMark()){
						locations.add(new Point(i,j));
					}
				}
			}
		}
		return locations;
	}


	public int numberOfPieces(Player p){
		return (positionOfPieces(p).size());
	}


	public void kingBirth(){

		//Scans top and bottom of the board, to allow for a piece to be upgraded.
		for(int i = 0; i < 8; i++){

			//This would mean a white piece moved to top of the board.
			if((this.board[0][i] != null) && (this.board[0][i].getColour() == 'W') && !(this.board[0][i].getType().equals("King"))){
				this.board[0][i].setType("King");
				System.out.println("King born. Appearing in bold text.\n");

			}

			if((this.board[7][i] != null) && (this.board[7][i].getColour() == 'B') && !(this.board[7][i].getType().equals("King"))){
				this.board[7][i].setType("King");
				System.out.println("King born. Appearing in bold text.\n");

			}
		}
	}

	//Add a method for working out what moves are possible for each player.

	public ArrayList<Move> possibleMoves(Player p){

		ArrayList<Move> posMovesList = new ArrayList<Move>();

		//First find the arrayList containing position of the players pieces. 

		ArrayList<Point> posPieces = positionOfPieces(p);

		//Then for each of the pieces, check if the moves to each adjacent diagonal square
		//are allowed using the moves possible method, returning an array of them.

		Point[] diagonals = {new Point(1,1), new Point(1,-1), new Point(-1,1), new Point(-1,-1)};

		for(Point start: posPieces){

			//For each piece, create the end position, and use to check if possible.

			for(Point addition: diagonals){

				Point end = new Point((start.x + addition.x),(start.y+ addition.y));

				if(isMovePossible(p, this.board[start.x][start.y], start, end)){
					posMovesList.add(new Move(start, end, this.board[start.x][start.y]));
				}
			}
		}
		return posMovesList;
	}

	public ArrayList<Move> possibleJumps(Player p){

		//Similar to possibleMoves, apart from the jumps are two.

		ArrayList<Move> posJumpList = new ArrayList<Move>(); 
		ArrayList<Point> posPieces = positionOfPieces(p);

		//Then for each pieces cycle through the 4 options and check if any are valid moves.

		Point[] diagonals = {new Point(2,2), new Point(2,-2), new Point(-2,2), new Point(-2,-2)};

		for(Point start: posPieces){

			for(Point addition: diagonals){

				Point end = new Point((start.x + addition.x),(start.y+ addition.y));

				if(isJumpPossible(p, this.board[start.x][start.y], start, end)){
					posJumpList.add(new Move(start, end, this.board[start.x][start.y]));
				}
			}
		}
		
		return posJumpList;

	}

	//This should be added into the isPossibleMove() method, to check the player is moving their own counter.

	public boolean isPlayerMovingOwnCounter(Player p, Counter c){
		if(p.getMark() == c.getColour()){
			return true; 
		}else{
			return false;
		}
	}

}