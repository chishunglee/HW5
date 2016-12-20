import java.util.ArrayList;


public class Table {
	public static final int MAXPLAYER=4;//���һ�����������ׂ���
	private Deck allCard;//������е���
	private Player[]allPlayer;//������е����
	private Dealer dealer;//���һ���f��
	private int[]pos_betArray;//���ÿ�������һ���µ�ע
	public Table(int nDeck)		//Constructor
	{
	    allCard=new Deck(nDeck);
		allPlayer=new Player[MAXPLAYER];
		dealer=new Dealer();
	}
	
	public void set_player(int pos,Player p)	//��Player�ŵ������� 
	{		
			
		allPlayer[pos]=p;
	}
	
	public Player[] get_player()	//�؂������������ϵ�player
	{						
		return allPlayer;
	}
	
	public void set_dealer(Dealer d)// ��Dealer�ŵ� (e) ׃���У��� (e)׃��֮setter��
	{

		dealer= d;
	}
	
	public Card get_face_up_card_of_dealer() //�؂�dealer���_���Ǐ��ƣ�Ҳ���ǵڶ�����
	{
		ArrayList<Card> DealerCard=new ArrayList<Card>();
		DealerCard=dealer.getOneRoundCard();
		return DealerCard.get(1);
	}
	
	private void ask_each_player_about_bets()
	{
		pos_betArray=new int[allPlayer.length];	//���4
		int i=0;
		while(i<allPlayer.length)
		{
			if(allPlayer[i]!=null)
			{
				allPlayer[i].say_hello();
				int bet=allPlayer[i].make_bet();
				if(bet>allPlayer[i].get_current_chips())
				{
					pos_betArray[i]=0;			//bet���ܴ��chips
				}
				else
				{
					pos_betArray[i]=allPlayer[i].make_bet();
				}	
			}
			i++;
		}
		
	}
	
	private void distribute_cards_to_dealer_and_players()//�l����
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
		DealerCard.add(allCard.getOneCard(false));	//�w
		DealerCard.add(allCard.getOneCard(true));	//�_
		dealer.setOneRoundCard(DealerCard);
		System.out.print("Dealer's face up card is ");
		DealerCard.get(1).printCard();//�@ʾDealer�_������
	}
	 
	private void ask_each_player_about_hits() //������Ƿ�Ҫ��
	{
	        int i = 0;
	        while (i < allPlayer.length) {
	            if (allPlayer[i] != null && pos_betArray[i] != 0) {
	                System.out.print(String.valueOf(allPlayer[i].get_name()) + "'s Cards now:");
	                allPlayer[i].printAllCard();
	                this.hit_process(i, allPlayer[i].getOneRoundCard());//Ҫ���^������̎��
	                System.out.println(String.valueOf(allPlayer[i].get_name()) + "'s hit is over!");
	            }
	            ++i;
	        }
	    }

	private void hit_process(int pos, ArrayList<Card> cards) {//Ҫ���^��
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
	
	private void ask_dealer_about_hits()//���f���Ƿ�Ҫ��
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
	
	private void calculate_chips()//Ӌ��I�a
	{
		int dealersCardValue=dealer.getTotalValue();
		System.out.print("Dealer's card value is "+dealersCardValue+",Card:");
		dealer.printAllCard();
		int i=0;
		while(i<allPlayer.length){
			if(allPlayer[i]!=null&&pos_betArray[i]!=0){
				System.out.print(String.valueOf(allPlayer[i].get_name())+"'s Cards:");
				allPlayer[i].printAllCard();
				caculate_process(i);//Ӌ���^������̎��
			}
			i++;
		}
	}
	
	private void caculate_process(int pos){//Ӌ���^��
		 System.out.print(String.valueOf(allPlayer[pos].get_name()) + " card value is " + allPlayer[pos].getTotalValue());
	        if (allPlayer[pos].getTotalValue() > 21) {//��ұ���(1)
	            if (dealer.getTotalValue() > 21) {//�f�ұ������]ݔ�]�A(1.1)
	                System.out.println(", chips have no change!, the Chips now is: " + allPlayer[pos].get_current_chips());
	            } else {//�f�қ]�������ݔ�X(1.2)
	                allPlayer[pos].increase_chips(- pos_betArray[pos]);
	                System.out.println(", Loss " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	            }
	        } else if (allPlayer[pos].getTotalValue() == 21) {//���=21�c(2)
	            if (allPlayer[pos].getOneRoundCard().size() == 2 && allPlayer[pos].hasAce()) {//Black jack!(2.1)
	                if (dealer.getTotalValue() != 21) {//�f�ҁK����21�c������A�X(2.1.1)
	                    allPlayer[pos].increase_chips(this.pos_betArray[pos] );
	                    System.out.println(",Black jack!!! Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	                } else if (dealer.getOneRoundCard().size() == 2 && dealer.hasAce()) {//�f��Ҳ��Black jack!���]ݔ�]�A(2.1.2)
	                    System.out.println(",Black Jack!!!! But chips have no change!, the Chips now is: " + allPlayer[pos].get_current_chips());
	                } else {//�f����21�c��������Black Jack!����A�X(2.1.3)
	                    allPlayer[pos].increase_chips(pos_betArray[pos] );
	                    System.out.println(",Black jack!!! Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	                }
	                //���21�c���K�]��Black Jack!(2.2)
	            } else if (dealer.getTotalValue() != 21) {//�f�ҁK����21�c������A�X(2.2.1)
	                allPlayer[pos].increase_chips(pos_betArray[pos] );
	                System.out.println(",Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	            } else {//�����21�c�f����Ҳ21�c�������]��Black Jack!���]ݔ�]�A(2.2.2)
	                System.out.println(",chips have no change!The Chips now is: " + allPlayer[pos].get_current_chips());
	            }
	            //��Ҳ����21�c(3)
	        } else if (dealer.getTotalValue() > 21) {//�f�ұ���������A�X(3.1)
	            allPlayer[pos].increase_chips(pos_betArray[pos]);
	            System.out.println(", Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	        } else if (dealer.getTotalValue() < allPlayer[pos].getTotalValue()) {//�f���c��<����c��������A�X(3.2)
	            allPlayer[pos].increase_chips(pos_betArray[pos]);
	            System.out.println(", Get " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	        } else if (dealer.getTotalValue() > allPlayer[pos].getTotalValue()) {//�f���c��>����c�������ݔ�X(3.3)
	            allPlayer[pos].increase_chips(- pos_betArray[pos]);
	            System.out.println(", Loss " + pos_betArray[pos] + " Chips, the Chips now is: " + allPlayer[pos].get_current_chips());
	        } else {//�f���c��=����c�����]ݔ�]�A(3.4)
	            System.out.println(", chips have no change! The Chips now is: " + allPlayer[pos].get_current_chips());
	        }
	    }
	
	public int[] get_palyers_bet(){
		return pos_betArray;
	}
	
	public void play(){//��һС�ֵ��^��
		ask_each_player_about_bets();
		distribute_cards_to_dealer_and_players();
		ask_each_player_about_hits();
		ask_dealer_about_hits();
		calculate_chips();
	}

	
	
}