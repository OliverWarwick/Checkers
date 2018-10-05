import java.util.*;
import java.awt.Point;

public abstract class Player{
	
	public char mark;

	public Player(char mark){
		this.mark = mark; 
	}

	public char getMark(){
		return this.mark;
	}

	public abstract Move getMove(Board B, Player Oppo);
		//Get a players move. Impliment in the human/computer extension to the class.

}