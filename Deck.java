package testingforvideo;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import testingforvideo.Card.Suit;

public class Deck {

	private Card[] cards;
	private int cardsInDeck;
	
	public Deck(){
		cards = new Card[52];
		
	}
	
	public void reset() {
		Card.Suit[] suits = Card.Suit.values();
		cardsInDeck=0;
		
		for(int i=0; i<suits.length; i++) {
			Suit suit =  suits[i];
			cards[cardsInDeck++]=new Card(suit, Card.Value.getValue(0));
			
			for(int k=1; k<10; k++) {
				cards[cardsInDeck++]=new Card(suit, Card.Value.getValue(k));
			}
			
			Card.Value[] values = new Card.Value[] {Card.Value.jack, Card.Value.queen, Card.Value.king};
			for(Card.Value value : values) {
				cards[cardsInDeck++]=new Card(suit, value);
			}
		}
		
	}
	
	public Card[] returnDeck() {
		return cards;
	}
	
	public int returnSize() {
		return cards.length;
	}
	
	public void replaceDeckWith(ArrayList<Card> cards) {
		this.cards = cards.toArray(new Card[cards.size()]);
		this.cardsInDeck = this.cards.length;
	}
	
	public void addToDeck(ArrayList<Card> cards) {
	    Card[] newDeck = new Card[this.cards.length + cards.size()];
	    System.arraycopy(this.cards, 0, newDeck, 0, this.cards.length);
	    System.arraycopy(cards.toArray(new Card[0]), 0, newDeck, this.cards.length, cards.size());
	    this.cards = newDeck;
	    this.cardsInDeck = this.cards.length;
	}

	
	public boolean isEmpty() {
		return cardsInDeck==0;
	}
	
	public void shuffle() {
		int n = cards.length;
		Random random = new Random();
		
		for(int i=0; i<cards.length; i++) {
			int randomValue = i+random.nextInt(n-i);
			Card randomCard = cards[randomValue];
			cards[randomValue] = cards[i];
			cards[i] = randomCard;
		}
	}
	
	public Card drawCard() throws IllegalArgumentException {
		if(isEmpty()) {
			throw new IllegalArgumentException("Cannot draw a card since there are no cards in the deck");
		}
		return cards[--cardsInDeck]; //this uses the length of the deck of cards to access and return the top card
	}
	
	public ImageIcon drawCardImage() throws IllegalArgumentException {
		if(isEmpty()) {
			throw new IllegalArgumentException("Cannot draw card since there are no cards in the deck");
		}
		return new ImageIcon(cards[--cardsInDeck].toString() + ".png");
	}
	
	public Card[] drawCard(int n) {
		Card[] ret;
		if(n<0) {
			throw new IllegalArgumentException("Must draw positive cards but tried to draw " + n + " cards.");
		} else if(n>cardsInDeck) {
			ret = new Card[cardsInDeck];
			for(int i = 0; i < cardsInDeck; i++) {
				ret[i] = cards[--cardsInDeck];
			}
		} else {
			ret = new Card[n];
			for(int i=0; i<n; i++) {
				ret[i]=cards[--cardsInDeck];
			}
		}
		
		
		
		return ret;
	}
	
	public void printDeck() {
		for(int i = 0; i<cards.length; i++) {
			System.out.println(cards[i]);
		}
	}
	
	
}
