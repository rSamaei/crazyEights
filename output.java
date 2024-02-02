package testingforvideo;
import java.util.Arrays;
public class output {

	public static void main(String[] args) {
		
		Deck deck = new Deck();
		deck.reset();
		System.out.println(Arrays.toString(deck.returnDeck()));
		deck.shuffle();
		System.out.println(Arrays.toString(deck.returnDeck()));
		
	}
	
}
