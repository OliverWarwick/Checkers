import java.util.*;
import java.awt.Point;

public class NamedPlayer extends Player{

	private String name;

	public String getName(){
		return this.name;
	}

	public NamedPlayer(char mark){
		super(mark);
		
		System.out.println("What is your name?");		//For use in the full game.
		Scanner sinput = new Scanner(System.in);	
		this.name = sinput.next();
		
		//this.name = "Player";				//For use in testing.

		System.out.println("\nCreating new human player: " + this.name);
	}

	public Move getMove(Board B, Player Oppo){
		//Get a humans move from inputs. 
		System.out.println("It's your move " + this.name);

			
		System.out.print("Starting Row: ");
		int startI = getInput();
		System.out.print("Starting Column: ");
		int startJ = getInput();

		System.out.print("\nEnding Row: ");
		int endI = getInput();
		System.out.print("Ending Column: ");
		int endJ = getInput();

		return new Move(new Point(startI, startJ), new Point(endI, endJ), B.board[startI][startJ]);

	}
	
	public static int getInput(){
		//Create scanner and accept an integer input.
		Scanner sinput = new Scanner(System.in);
		try{
			return sinput.nextInt() - 1; //To account for the array/board difference
		}catch (InputMismatchException e){
			return getInput(); 
		}
	}
}