

public class Player extends Person {
	private String name;
	private int chips;
	private int bet=0; 
	
	public Player(String name, int chips)
	{
		this.name=name;
		this.chips=chips;
	}
	public String get_name()
	{
		return name;
	}
	public int make_bet() 
	{				
		if (chips<100)
		{
			bet=10;
		}
		 if (chips<10)
		{
			bet=1;
		}
		 if (chips <1)
		{
			bet = 0;
		}
		return bet;
	}	
	
	public boolean hit_me(Table table)
	{
		int value = getTotalValue();
		if (value<=16)
		{
			return true;
		}
		return false;		
	}
	
	
	public int get_current_chips() 
	{
		return chips;
	}
	public void increase_chips (int diff)
	{
		chips+=diff;
	}
	public void say_hello() 
	{
		System.out.println("Hello, I am " + name + "."); 
		System.out.println("I have " + chips + " chips.");
	}
}
