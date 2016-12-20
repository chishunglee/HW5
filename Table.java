import java.util.ArrayList;


public class Table {
	public static final int MAXPLAYER=4;//最多一張牌桌能坐幾個人
	private Deck allCard;//存放所有的牌
	private Player[]allPlayer;//存放所有的玩家
	private Dealer dealer;//存放一個莊家
	private int[]pos_betArray;//存放每個玩家在一局下的注
	public Table(int nDeck)		//Constructor
	{
	    allCard=new Deck(nDeck);
		allPlayer=new Player[MAXPLAYER];
		dealer=new Dealer();
	}
	
	public void set_player(int pos,Player p)	//將Player放到牌桌上 
	{		
			
		allPlayer[pos]=p;
	}
	
	public Player[] get_player()	//回傳所有在牌桌上的player
	{						
		return allPlayer;
	}
	
	public void set_dealer(Dealer d)// 將Dealer放到 (e) 變數中，為 (e)變數之setter。
	{

		dealer= d;
	}
	
	public Card get_face_up_card_of_dealer() //回傳dealer打開的那張牌，也就是第二張牌
	{
		ArrayList<Card> DealerCard=new ArrayList<Card>();
		DealerCard=dealer.getOneRoundCard();
		return DealerCard.get(1);
	}
	
	private void ask_each_player_about_bets()
	{
		pos_betArray=new int[allPlayer.length];	//玩家4
		int i=0;
		while(i<allPlayer.length)
		{
			if(allPlayer[i]!=null)
			{
				allPlayer[i].say_hello();
				int bet=allPlayer[i].make_bet();
				if(bet>allPlayer[i].get_current_chips())
				{
					pos_betArray[i]=0;			//bet不能大於chips
				}
				else
				{
					pos_betArray[i]=allPlayer[i].make_bet();
				}	
			}
			i++;
		}
		
	}
	
	private void distribute_cards_to_dealer_and_players()//發底牌
	{
		
		int i=0;
		while(i<allPlayer.length)
		{
			if (allPlayer[i] != null && pos_betArray[i] != 0){
				 ArrayList<Card> playersCard = new ArrayList<Card>();
	                playersCard.add(allCard.getOneCard(true));
	                playersCard.add(allCard.getOneCard(true));
	                allPlayer[i].setOneRoundCard(playersCard);
			}
			i++;
		}	
		ArrayList<Card> DealerCard = new ArrayList<Card>();
		DealerCard.add(allCard.getOneCard(false));	//蓋
		DealerCard.add(allCard.getOneCard(true));	//開
		dealer.setOneRoundCard(DealerCard);
		System.out.print("Dealer's face up card is ");
		DealerCard.get(1).printCard();//顯示Dealer開著的牌
	}
	 
	private void ask_each_player_about_hits() //問玩家是否要牌
	{
	        int i = 0;
	        while (i < allPlayer.length) {
	            if (allPlayer[i] != null && pos_betArray[i] != 0) {
	                System.out.print(String.valueOf(allPlayer[i].get_name()) + "'s Cards now:");
	                allPlayer[i].printAllCard();
	                this.hit_process(i, allPlayer[i].getOneRoundCard());//要牌過程另外處理
	                System.out.println(String.valueOf(allPlayer[i].get_name()) + "'s hit is over!");
	            }
	            ++i;
	        }
	    }

	private void hit_process(int pos, ArrayList<Card> cards) {//要牌過程
	        boolean hit;
	        do {
	            if (hit = allPlayer[pos].hit_me(this)) {
	                cards.add(allCard.getOneCard(true));
	                allPlayer[pos].setOneRoundCard(cards);
	                System.out.print("Hit! ");
	                System.out.print(String.valueOf(allPlayer[pos].get_name()) + "'s Cards now:");
	                allPlayer[pos].printAllCard();
	                if (allPlayer[pos].getTotalValue() <= 21) continue;
	                hit = false;
	                continue;
	            }
	            System.out.println("Pass hit!");
	        } while (hit);
	    }
	
	private void ask_dealer_about_hits()//問莊家是否要牌
	{
		boolean hit;
		ArrayList<Card>cards=dealer.getOneRoundCard();
		do{
			if(hit=dealer.hit_me(this)){
				cards.add(allCard.getOneCard(true));
				dealer.setOneRoundCard(cards);
			}
			if(dealer.getTotalValue()<=21)continue;
			hit=false;
		}while(hit);
		System.out.println("Dealer's hit is over");
	}
	
	private void calculate_chips()//計算籌碼
	{
		int dealersCardValue=dealer.getTotalValue();
		System.out.print("Dealer's card value is "+dealersCardValue+",Card:");
		dealer.printAllCard();
		int i=0;
		while(i<allPlayer.length){
			if(allPlayer[i]!=null&&pos_betArray[i]!=0){
				System.out.print(String.valueOf(allPlayer[i].get_name())+"'s Cards:");
				allPlayer[i].printAllCard();
				caculate_process(i);//計算過程另外處理
			}
			i++;
		}
	}
	
	private void caculate_process(int pos){//計算過程
		 System.out.print(String.valueOf(allPlayer[pos].get_name()) + " card value is " + allPlayer[pos].getTotalValue());
	        if (allPlayer[pos].getTotalValue() > 21) {//玩家爆掉(1)
	            if (dealer.getTotalValue() > 21) {//莊家爆掉，沒輸沒贏(1.1)
	                System.out.println(", chips have no change!, the Chips now is: " + allPlayer[pos].get_current_chips());
	            } else {//莊家沒爆，玩家輸錢(1.2)
	                allPlayer[pos].increase_chips(- pos_betArray[pos]);
	                System.out.println(", Loss " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	            }
	        } else if (allPlayer[pos].getTotalValue() == 21) {//玩家=21點(2)
	            if (allPlayer[pos].getOneRoundCard().size() == 2 && allPlayer[pos].hasAce()) {//Black jack!(2.1)
	                if (dealer.getTotalValue() != 21) {//莊家並不是21點，玩家贏錢(2.1.1)
	                    allPlayer[pos].increase_chips(this.pos_betArray[pos] );
	                    System.out.println(",Black jack!!! Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	                } else if (dealer.getOneRoundCard().size() == 2 && dealer.hasAce()) {//莊家也是Black jack!，沒輸沒贏(2.1.2)
	                    System.out.println(",Black Jack!!!! But chips have no change!, the Chips now is: " + allPlayer[pos].get_current_chips());
	                } else {//莊家是21點，但不是Black Jack!玩家贏錢(2.1.3)
	                    allPlayer[pos].increase_chips(pos_betArray[pos] );
	                    System.out.println(",Black jack!!! Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	                }
	                //玩家21點但並沒有Black Jack!(2.2)
	            } else if (dealer.getTotalValue() != 21) {//莊家並不是21點，玩家贏錢(2.2.1)
	                allPlayer[pos].increase_chips(pos_betArray[pos] );
	                System.out.println(",Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	            } else {//玩家是21點莊家是也21點，但都沒有Black Jack!，沒輸沒贏(2.2.2)
	                System.out.println(",chips have no change!The Chips now is: " + allPlayer[pos].get_current_chips());
	            }
	            //玩家不等於21點(3)
	        } else if (dealer.getTotalValue() > 21) {//莊家爆掉，玩家贏錢(3.1)
	            allPlayer[pos].increase_chips(pos_betArray[pos]);
	            System.out.println(", Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	        } else if (dealer.getTotalValue() < allPlayer[pos].getTotalValue()) {//莊家點數<玩家點數，玩家贏錢(3.2)
	            allPlayer[pos].increase_chips(pos_betArray[pos]);
	            System.out.println(", Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	        } else if (dealer.getTotalValue() > allPlayer[pos].getTotalValue()) {//莊家點數>玩家點數，玩家輸錢(3.3)
	            allPlayer[pos].increase_chips(- pos_betArray[pos]);
	            System.out.println(", Loss " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	        } else {//莊家點數=玩家點數，沒輸沒贏(3.4)
	            System.out.println(", chips have no change! The Chips now is: " + allPlayer[pos].get_current_chips());
	        }
	    }
	
	public int[] get_palyers_bet(){
		return pos_betArray;
	}
	
	public void play(){//玩一小局的過程
		ask_each_player_about_bets();
		distribute_cards_to_dealer_and_players();
		ask_each_player_about_hits();
		ask_dealer_about_hits();
		calculate_chips();
	}

	
	
}