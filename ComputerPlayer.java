import java.awt.Point;
import java.util.*;
import java.lang.Math;

public class ComputerPlayer extends Player{
	
	public ComputerPlayer(char mark){
		super(mark);
		System.out.println("Creating new computer player.");
	}

	//May also need to pass it the other player i.e "Black" as then it can check how many moves they have avalible etc.

	public Move getMove(Board B, Player Oppo){

		//Algorithm for playing.

		//Step 0.
		if(this.canComputerWin(B,Oppo)){
			System.out.println("Step 0 - Winning");
			return this.canComputerWinExecute(B,Oppo);
			
		}


		//Step 1.
		else if(this.isComputerAboutToLose(B,Oppo)){
			System.out.println("Step 1 - Avoiding a loss.");
			return this.isComputerAboutToLoseExecute(B,Oppo);
			
		}

		//Step 2A.
		else if(this.isDoubleMoveAvalible(B,Oppo) != null){
			System.out.println("Step 2A - Double move avalible.");
			return(this.isDoubleMoveAvalible(B,Oppo));
		}


		//Step 2B.
		else if(this.canComputerTakePieceWithoutTakeBack(B,Oppo) != null){
			System.out.println("Step 2B - Taking a piece without takeback.");
			return this.canComputerTakePieceWithoutTakeBack(B,Oppo);
			
		}

		//Step 3A.
		else if(this.canComputerJumpToTrade(B,Oppo) != null){
			System.out.println("Step 3A - Trading by jumping.");
			return this.canComputerJumpToTrade(B,Oppo);
		}


		//Step 3B.
		else if(this.canComputerMoveToDefendAPiece(B,Oppo) != null){
			System.out.println("Step 3B - Move to defend a piece.");
			return this.canComputerMoveToDefendAPiece(B,Oppo);
		}


		//Step 3C.
		else if(this.isHumanAboutToTakeAPiece(B,Oppo) != null){
			System.out.println("Step 3C - Avoiding being taken.");
			return this.isHumanAboutToTakeAPiece(B,Oppo);
		}

		
		//Step 4A.
		else if(this.canComputerMoveToTradeAPiece(B,Oppo) != null){
			System.out.println("Step 4A - Trading a piece by moving first.");
			return this.canComputerMoveToTradeAPiece(B,Oppo);
		}

		//Step 4B - Aggresive Routine.
		else if(this.isAggresivePlayAllowed(B,Oppo) == true){
			if(this.isComputerFreeToMakeKing(B,Oppo) != null){
				System.out.println("Step 4B Part 1 - Free to make a king");
				return this.isComputerFreeToMakeKing(B,Oppo);
			}else{
				System.out.println("Step 4B Part 2 - Moving towards oppo");
				return this.isComputerFreeToMoveTowardsAPiece(B,Oppo);
			}
		}


		//Step 5. 
		else if(this.canComputerFormATriangle(B,Oppo) != null){
			System.out.println("Step 5 - Forming a triangle.");
			return this.canComputerFormATriangle(B,Oppo);
		}


		//Step 6.
		else if(this.canSafelyMoveToSide(B,Oppo) != null){
			System.out.println("Step 6 - Moving to the side.");
			return this.canSafelyMoveToSide(B,Oppo);
		}


		//Step 7.
		else if(this.randomMoveWithoutBeingTaken(B,Oppo) != null){
			System.out.println("Step 7 - Random move without being taken.");
			return this.randomMoveWithoutBeingTaken(B,Oppo);
		}


		//Step 8.
		else{
			System.out.println("Step 8 - Random move.");
			return this.randomMove(B,Oppo);
		}

	}

	//Step 0.
	//Boolean return on whether step 0 is the step to execute.

	public boolean canComputerWin(Board B, Player Oppo){

		if(B.numberOfPieces(Oppo) == 1 && B.possibleJumps(this).size() != 0){
			return true;
		}else{
			return false;
		}

	}

	//Execution of Step 0.

	public Move canComputerWinExecute(Board B, Player Oppo){

		ArrayList<Move> WinningMoves = B.possibleJumps(this);

		return WinningMoves.get(0);

	}


	//Step 1.
	//Boolean return on whether the computer is about to lose. 

	public boolean isComputerAboutToLose(Board B, Player Oppo){

		if(B.numberOfPieces(this) == 1 && B.possibleJumps(Oppo).size() != 0){
			return true;
		}else{
			return false;
		}
	}

	//Execution of Step 1. Check all moves avalible, and if there are any for which is the next move
	//the human cannot win then execute this.

	public Move isComputerAboutToLoseExecute(Board B, Player Oppo){

		ArrayList<Move> MovesAvalible = B.possibleMoves(this);

		for(Move testMove: MovesAvalible){

			//Create a copy of the board to test on. Have to do this manually. Then test each move, and if gets out of
			//trouble then return this move.

			Board Dummy = this.createDummyBoard(B, Oppo);
			Dummy.executeMove(testMove);

			if(Dummy.possibleJumps(Oppo).size() == 0){
				return testMove;
			}

		}

		//If not such move is avalible, then move randomly.

		return this.randomMove(B,Oppo);

	}

	//Step 2A.
	public Move isDoubleMoveAvalible(Board B, Player Oppo){

		ArrayList<Move> JumpsAvalible = B.possibleJumps(this);

		if(JumpsAvalible.size() != 0){

			for(Move testJump: JumpsAvalible){

				//Want to test each jump, and if there are any that where the number of
				//Jumps avalible to the oppo is 0 on the next turn then execute it. 

				Board Dummy = this.createDummyBoard(B, Oppo);
				Dummy.executeJump(testJump);

				ArrayList<Move> JumpsNowAvalible = Dummy.possibleJumps(this);

				for(Move secondJump: JumpsNowAvalible){
					if(secondJump.start.equals(testJump.end)){
						return testJump;
					}
				}
			}
		}
		return null;
	}


	//Step 2B.
	//Returns null if this if not possible, and if not returns the move.

	public Move canComputerTakePieceWithoutTakeBack(Board B, Player Oppo){

		ArrayList<Move> JumpsAvalible = B.possibleJumps(this);

		if(JumpsAvalible.size() != 0){

			for(Move testJump: JumpsAvalible){

				//Want to test each jump, and if there are any that where the number of
				//Jumps avalible to the oppo is 0 on the next turn then execute it. 

				Board Dummy = this.createDummyBoard(B, Oppo);
				Dummy.executeJump(testJump);

				if(Dummy.possibleJumps(Oppo).size() == 0){
					return testJump;
				}
			}
		}

		return null;
	}


	//Step 3A. Consider all the jumps, if any allow the oppo to just take that piece and have no jumps then execute.
	public Move canComputerJumpToTrade(Board B, Player Oppo){

		ArrayList<Move> JumpsAvalible = B.possibleJumps(this);

		if(JumpsAvalible.size() != 0){

			for(Move M: JumpsAvalible){

				Board Dummy = this.createDummyBoard(B, Oppo);
				Dummy.executeJump(M);

				//Try each jump, and if any only have one Jump back then providing it's on the piece moved.

				ArrayList<Move> humanJumps = Dummy.possibleJumps(Oppo);

				if(humanJumps.size() == 1){

					Move humanJ = humanJumps.get(0);

					if(humanJ.start.x == M.end.x-1 && (humanJ.start.y == M.end.y-1 || humanJ.start.y == M.end.y+1)){
						return M;
					}
				}
			}
		}

		return null;
	}



	//Step 3B.
	public Move canComputerMoveToDefendAPiece(Board B, Player Oppo){

		ArrayList<Move> humanCurrentJumps = B.possibleJumps(Oppo);
		ArrayList<Move> compCurrentMoves = B.possibleMoves(this);

		if(humanCurrentJumps.size() != 0){

			for(Move M: compCurrentMoves){

				Board Dummy = this.createDummyBoard(B, Oppo);
				Dummy.executeJump(M);

				//If Oppo now has no jumps, then must have defended the piece.

				if(Dummy.possibleJumps(Oppo).size() == 0){
					return M;
				}
			}
		}

		return null;
	}



	//Step 3C.
	//Returns move if possible to do, if not then will 

	public Move isHumanAboutToTakeAPiece(Board B, Player Oppo){

		//If the oppo is about to take a piece.
		//Then test for each of my moves, if I can make the number of jumps pos on next move 0.
		//If not then return false.

		ArrayList<Move> MovesAvalible = B.possibleMoves(this);

		if(B.possibleJumps(Oppo).size() != 0){

			for(Move testMove: MovesAvalible){

				Board Dummy = this.createDummyBoard(B, Oppo);
				Dummy.executeMove(testMove);

				if(Dummy.possibleJumps(Oppo).size() == 0){

					//Means that the move has got the piece out of danger.

					return testMove;
				}
			}
		}

		return null;
	}


	//Step 4A.
	public Move canComputerMoveToTradeAPiece(Board B, Player Oppo){

		ArrayList<Move> MovesAvalibleCurrently = B.possibleMoves(this);

		for(Move firstMove: MovesAvalibleCurrently){

			Board Dummy = this.createDummyBoard(B, Oppo);
			Dummy.executeMove(firstMove);

			ArrayList<Move> JumpsAvalibleHuman = Dummy.possibleJumps(Oppo);

			if(JumpsAvalibleHuman.size() == 1){

				Move humanJump = JumpsAvalibleHuman.get(0);

				//Check that it is jumping over the piece we moved.

				if(humanJump.start.x == firstMove.end.x-1 && (humanJump.start.y == firstMove.end.y-1 || humanJump.start.y == firstMove.end.y-1)){

					Dummy.executeJump(humanJump);

					//Check there are no double jumps avalible. And Computer can jump back over the piece.

					ArrayList<Move> JumpsNowAvalible = Dummy.possibleJumps(this);

					if(Dummy.possibleJumps(Oppo).size() == 0 && JumpsNowAvalible.size() > 0){

						for(Move compJump: JumpsNowAvalible){

							if(compJump.end == firstMove.end){
								return firstMove;
							}
						}
					}
				}
			}
		}
		return null;
	}

	//Step 4B - Boolean return if the condictions for aggresive play are correct then return true.
	public boolean isAggresivePlayAllowed(Board B, Player Oppo){

		if(B.numberOfPieces(Oppo) <= 6 && (B.numberOfPieces(this) - B.numberOfPieces(Oppo) >= 2)){	
			return true;
		}else{
			return false;
		}

	}

	//Step 4B - Part 1. 
	public Move isComputerFreeToMakeKing(Board B, Player Oppo){

		//Need to make sure the oppo has no jumps currently.
		ArrayList<Move> movesAvalible = B.possibleMoves(this);

		if(B.possibleJumps(Oppo).size() == 0){

			for(Move M: movesAvalible){

				if(M.end.x == 0 && B.board[M.start.x][M.start.y].getType().equals("Normal")){

					Board Dummy = this.createDummyBoard(B, Oppo);
					Dummy.executeMove(M);

					ArrayList<Move> JumpsAvalibleHuman = Dummy.possibleJumps(Oppo);

					if(JumpsAvalibleHuman.size() == 0){
						return M;
					}
				}
			}
		}
		return null;
	}


	//Step 4B - Part 2.
	public Move isComputerFreeToMoveTowardsAPiece(Board B, Player Oppo){

		ArrayList<Move> movesAvalible = B.possibleMoves(this);
		ArrayList<Move> posMoves = new ArrayList<Move>();

		for(Move M: movesAvalible){

			Board Dummy = this.createDummyBoard(B, Oppo);
			Dummy.executeMove(M);

			if(Dummy.possibleJumps(Oppo).size() == 0){

				ArrayList<Point> posOppoPieces = B.positionOfPieces(Oppo);

				for(Point P: posOppoPieces){
					if(Math.abs(P.x - M.end.x) <= 2 && Math.abs(P.y - M.end.y) <= 2){
						posMoves.add(M);
					}
				}
			}
		}

		if(posMoves.size() != 0){

			//If any of them are king moves then use this as more versitile piece.

			for(Move M: posMoves){
				if(B.board[M.start.x][M.start.y].getType().equals("King")){
					return M;
				}
			}

			int randomNumber = (int) (Math.random() * posMoves.size());
			return posMoves.get(randomNumber);

		}
		else{
			if(this.randomMoveWithoutBeingTaken(B,Oppo) != null){
				return this.randomMoveWithoutBeingTaken(B,Oppo);
			}else{
				return this.randomMove(B,Oppo);
			}
		}
	}



	//Step 5.
	public Move canComputerFormATriangle(Board B, Player Oppo){

		ArrayList<Move> MovesAvalible = B.possibleMoves(this);

		for(Move M: MovesAvalible){

			//Check first if it forms the left side of a triangle. 

			Board Dummy = this.createDummyBoard(B, Oppo);
			Dummy.executeMove(M);

			//Left side check first

			if(M.end.x >= 1 && M.end.y >= 2){

				if(Dummy.board[M.end.x-1][M.end.y-1] != null && Dummy.board[M.end.x-1][M.end.y-1].getColour() == this.mark
					&& Dummy.board[M.end.x][M.end.y-2] != null && Dummy.board[M.end.x][M.end.y-2].getColour() == this.mark){

					//Means we have a left triangle. Now need to check that no Jumps are possible.

					if(Dummy.possibleJumps(Oppo).size() == 0){
						return M;
					}
				}
			}

			//Right side check

			if(M.end.x >= 1 && M.end.y <= 5){

				if(Dummy.board[M.end.x-1][M.end.y+1] != null && Dummy.board[M.end.x-1][M.end.y+1].getColour() == this.mark
					&& Dummy.board[M.end.x][M.end.y+2] != null && Dummy.board[M.end.x][M.end.y+2].getColour() == this.mark){

					//Means we have a right triangle. Check if no Jumps possible.

					if(Dummy.possibleJumps(Oppo).size() == 0){
						return M;
					}
				}
			}	
		}

		return null;
	}


	//Step 6.
	public Move canSafelyMoveToSide(Board B, Player Oppo){

		ArrayList<Move> MovesAvalible = B.possibleMoves(this);

		for(Move M: MovesAvalible){

			//Left side.

			Board Dummy = this.createDummyBoard(B, Oppo);
			Dummy.executeMove(M);

			if(M.end.y == 0 || M.end.y == 7){
				if(Dummy.possibleJumps(Oppo).size() == 0){
					return M;
				}
			}
		}

		return null;

	}


	//Step 7.
	public Move randomMoveWithoutBeingTaken(Board B, Player Oppo){

		ArrayList<Move> MovesAvalible = B.possibleMoves(this);
		ArrayList<Move> MovesPos = new ArrayList<Move>();

		for(Move M: MovesAvalible){

			//Check each forward move, if there is no possible jumps for Oppo.

			Board Dummy = this.createDummyBoard(B, Oppo);
			Dummy.executeMove(M);

			if(Dummy.possibleJumps(Oppo).size() == 0){
				MovesPos.add(M);
				
			}
		}

		if(MovesPos.size() != 0){
			int randomNumber = (int) (Math.random() * MovesPos.size());
			return MovesPos.get(randomNumber);
		}

		return null;

	}



	//Step 8.
	public Move randomMove(Board B, Player Oppo){

		System.out.println("Step 8.");

		ArrayList<Move> randomMoveArray = B.possibleMoves(this);

		//Pick a random number, and return. 

		int randomNumber = (int) (Math.random() * randomMoveArray.size());

		return randomMoveArray.get(randomNumber);

	}

	public Board createDummyBoard(Board B, Player Oppo){

		//Use the constuctor which doesn't populate the board automatically.

		Board DummyBoard = new Board(this, Oppo, "Non-Populated");

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(B.board[i][j] != null){
					DummyBoard.board[i][j] = B.board[i][j];
				}
			}
		}


		return DummyBoard;
		
	}

}