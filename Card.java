
public class Card
{
	public enum Suit{Clubs,Diamonds,Hearts,Spades}; 
	private Suit suit;
	private int rank; 
	public  Card(Suit s,int r)
	{
		suit=s;
		rank=r;
	}	
	
	public void printCard()
	{
		System.out.println(suit+","+rank);
	}
	public Suit getSuit(){
		return suit;
	}
	public int getRank(){
		return rank;
	}
}