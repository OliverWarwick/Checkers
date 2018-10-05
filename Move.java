import java.awt.Point;

public class Move{
		
	public Point start;
	public Point end;
	public Counter c;

	public Move(Point s, Point e, Counter c){
		this.start = s;
		this.end = e;
		this.c = c;
	}

}