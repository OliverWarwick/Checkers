public class Counter{
	
	private char colour;
	private String type;

	public Counter(Player p){
		this.colour = p.getMark();
		this.type = "Normal";
	}

	public Counter(Player p, String type){
		this.colour = p.getMark();
		this.type = type;
	}


	public char getColour(){
		return colour;
	}

	public void setColour(char colour){
		this.colour = colour;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

}