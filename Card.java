package testingforvideo;

import java.io.Serializable;

public class Card implements Serializable{

	private static final long serialVersionUID = 1L;

	enum Suit{
		hearts, diamonds, spades, clubs;
		
		private static final Suit[] suit = Suit.values();
		public static Suit getSuit(int i) {
			return Suit.suit[i];
		}
	}
	
	enum Value{
		ace, two, three, four, five, six, seven, eight, nine, ten, jack, queen, king;
		
		private static final Value[] values = Value.values();
		public static Value getValue(int i) {
			return Value.values[i];
		}
	}
	
	private final Suit suit;
	private final Value value;
	
	public Card(final Suit suit, final Value value) {
		this.suit=suit;
		this.value=value;
	}
	
	public Suit getSuit() {
		return this.suit;
	}
	
	public Value getValue() {
		return this.value;
	}
	
	public String toString() {
		return  value + " of " + suit;
	}
	
}
