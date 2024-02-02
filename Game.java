package testingforvideo;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import testingforvideo.Game.InvalidSuitSubmissionException;
import testingforvideo.Game.InvalidValueSubmissionException;

public class Game {

	private int currentPlayer;
	private int previousPlayer;
	private String[] playerIDs;
	
	private Deck deck;
	private ArrayList<ArrayList<Card>> playerHand;
	private ArrayList<Card> stockPile;
	
	private Card.Suit validSuit;
	private Card.Value validValue;
	
	private int cardsToDraw;
	
	boolean gameDirection;
	
	public Game(String[] pids) {
		deck = new Deck();
		deck.reset();
		deck.shuffle();
		
		stockPile = new ArrayList<Card>();
		
		playerIDs = pids;
		currentPlayer = 0;
		gameDirection = false;
		
		playerHand = new ArrayList<ArrayList<Card>>();
		
		for(int i=0; i<pids.length; i++) {
			ArrayList<Card> hand = new ArrayList<Card>(Arrays.asList(deck.drawCard(5)));
			playerHand.add(hand);
		}
	}
	
	public void start(Game game) {
		Card card = deck.drawCard();
		validSuit = card.getSuit();
		validValue = card.getValue();
		
		if(card.getValue() == Card.Value.eight) {
			JLabel message = new JLabel(playerIDs[currentPlayer] + " was skipped!");
			message.setFont(new Font("Times New Roman", Font.BOLD, 18));
			JOptionPane.showMessageDialog(null, message);
			
			if(gameDirection == false) {
				currentPlayer = (currentPlayer+1) % playerIDs.length;
			}
			
			else if(gameDirection == true) {
				currentPlayer = (currentPlayer - 1) % playerIDs.length;
				if(currentPlayer == -1) {
					currentPlayer = playerIDs.length - 1;
				}
			}
		}
		
		if(card.getValue() == Card.Value.king) {
			JLabel message = new JLabel("The game direction changed!");
			message.setFont(new Font("Times New Roman", Font.BOLD, 18));
			JOptionPane.showMessageDialog(null, message);
			gameDirection ^= true;
			currentPlayer = playerIDs.length - 1;
		}
		
		stockPile.add(card);
	}
	
	public Card getTopCard() {
		return new Card(validSuit, validValue);
	}
	
	public ImageIcon getTopCardImage() {
		return new ImageIcon(validValue + "_of_" + validSuit + ".png");
	}
	
	public String getTopCardImageString() {
		return validValue + "_of_" + validSuit + ".png";
	}

	
	public boolean isGameOver() {
		for(String player : this.playerIDs) {
			if(hasEmptyHand(player)) {
				return true;
			}
		}
		return false;
	}
	
	public String gameWinner() {
		for(String player : this.playerIDs) {
			if(hasEmptyHand(player)) {
				return player;
			}
		}
		return null;
	}
	
	public String getCurrentPlayer() {
		return this.playerIDs[this.currentPlayer];
	}
	
	public int getCurrentPlayerInt() {
		return this.currentPlayer;
	}
	
	public int getPreviousPlayerInt() {
		return this.previousPlayer;
	}
	
	public boolean isCurrentPlayer(int id) {
		return id == currentPlayer;
	}
	
	public String getPreviousPlayer(int i) {
		int index = this.currentPlayer - i;
		if(index == -1) {
			index = playerIDs.length - 1;
		}
		return this.playerIDs[index];
	}
	
	public String[] getPlayers() {
		return playerIDs;
	}
	
	public ArrayList<Card> getPlayerHand(String pid){
		int index = Arrays.asList(playerIDs).indexOf(pid);
		return playerHand.get(index);
	}
	
	public int getPlayerHandSize(String pid) {
		return getPlayerHand(pid).size();
	}
	
	public Card getPlayerCard(String pid, int choice) {
		ArrayList<Card> hand = getPlayerHand(pid);
		return hand.get(choice);
	}
	
	public boolean hasEmptyHand(String pid) {
		return getPlayerHand(pid).isEmpty();
	}
	
	public boolean checkWinner() {
		for (ArrayList<Card> hand : playerHand) {
	        if (hand.isEmpty()) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public boolean validCardPlay(Card card) {
		return card.getSuit() == validSuit || card.getValue() == validValue;
	}
	
	public void checkPlayerTurn(String pid) throws InvalidPlayerTurnException{
		if(this.playerIDs[this.currentPlayer] != pid) {
			throw new InvalidPlayerTurnException("It is not " + pid + "'s turn", pid);
		}
	}
	
	public void setCardSuit(Card.Suit suit) {
		validSuit = suit;
	}

	public void playCard(Card card) 
		throws InvalidSuitSubmissionException, InvalidValueSubmissionException, InvalidPlayerTurnException{
		
		System.out.println("------ CARDS TO DRAW ------");
		System.out.println(cardsToDraw);
		if(cardsToDraw != 0) { 
			if(deck.returnSize() < cardsToDraw) {
				deck.addToDeck(stockPile);
				stockPile.clear();
				deck.shuffle();
			}
			
			ArrayList<Card> hand = playerHand.get(currentPlayer);
			hand.addAll(Arrays.asList(deck.drawCard(cardsToDraw)));
			playerHand.set(currentPlayer, hand);
			
			if(gameDirection == false) {
				currentPlayer = (currentPlayer + 1) % playerIDs.length;
			}
			else if(gameDirection == true) {
				currentPlayer = (currentPlayer - 1) % playerIDs.length;
				if(currentPlayer == -1) {
					currentPlayer = playerIDs.length - 1;
					}
			}
				
				cardsToDraw = 0;
			
		} else {
			if(!validCardPlay(card)) {
				if(card.getValue() == Card.Value.jack && validValue == Card.Value.jack || card.getValue() == Card.Value.jack && validValue == Card.Value.two) {
					cardsToDraw += 5;
				} else if(card.getValue() == Card.Value.two && validValue == Card.Value.jack || card.getValue() == Card.Value.two && validValue == Card.Value.two) {
					cardsToDraw += 2;
			} else {
				if(card.getSuit() != validSuit) {
					JLabel message = new JLabel("Invalid player move, expected suit: " + validSuit + " but played " + card.getSuit());
					message.setFont(new Font("Times New Roman", Font.BOLD, 16));
					JOptionPane.showMessageDialog(null, message);
					throw new InvalidSuitSubmissionException(message, card.getSuit(), validSuit);
				}
				
				else if(card.getValue() != validValue) {
					JLabel message = new JLabel("Invalid player move, expected value: " + validValue + " but got value: " + card.getValue());
					message.setFont(new Font("Times New Roman", Font.BOLD, 16));
					JOptionPane.showMessageDialog(null, message);
					throw new InvalidValueSubmissionException(message, card.getValue(), validValue);
					}
				}
			} else {
				if(card.getValue() == Card.Value.king) {
					JLabel message = new JLabel("changed the direction!");
					message.setFont(new Font("Times New Roman", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(null, message);
			
					gameDirection ^= true;
					System.out.println("changed game direction");
					/*if(gameDirection == true) {
						currentPlayer = (currentPlayer - 2) % playerIDs.length;
						if(currentPlayer == -1) {
							currentPlayer = playerIDs.length - 1;
						}
						if(currentPlayer == -2) {
							currentPlayer = playerIDs.length - 2;
						}
					} else if(gameDirection == false) {
						currentPlayer = (currentPlayer + 2) % playerIDs.length;
					}*/
				}
			
				
				if(card.getValue() == Card.Value.jack) {
					cardsToDraw += 5;
				} 
				if(card.getValue() == Card.Value.two) {
					cardsToDraw += 2;
				}
				
				validSuit = card.getSuit();
				validValue = card.getValue();
				stockPile.add(card);
				ArrayList<Card> hand = playerHand.get(currentPlayer);

				for (Card Card : hand) {
					if (Card.getValue().equals(card.getValue()) && Card.getSuit().equals(card.getSuit())) {
						hand.remove(Card);
						break;
					}
				}
				playerHand.set(currentPlayer, hand);
			
				if(gameDirection == false) {
					currentPlayer = (currentPlayer + 1) % playerIDs.length;
				}
				else if(gameDirection == true) {
					currentPlayer = (currentPlayer - 1) % playerIDs.length;
					if(currentPlayer == -1) {
						currentPlayer = playerIDs.length - 1;
					}
				}
				
				if(card.getValue() == Card.Value.eight) {
					JLabel message = new JLabel("skipped " + getCurrentPlayer());
					message.setFont(new Font("Times New Roman", Font.PLAIN, 16));
					JOptionPane.showMessageDialog(null, message);
					if(gameDirection == false) {
						currentPlayer = (currentPlayer + 1) % playerIDs.length;
					}
					else if(gameDirection == true) {
						currentPlayer = (currentPlayer - 1) % playerIDs.length;
						if(currentPlayer == -1) {
							currentPlayer = playerIDs.length - 1;
						}
					}
				}	
			}
		}
	}
	
	public void playAce(Card card, Card ace) throws InvalidSuitSubmissionException, InvalidValueSubmissionException {
		
		if(cardsToDraw != 0) {
			if(deck.returnSize() < cardsToDraw) {
				deck.addToDeck(stockPile);
				stockPile.clear();
				deck.shuffle();
			}
			
			ArrayList<Card> hand = playerHand.get(currentPlayer);
			hand.addAll(Arrays.asList(deck.drawCard(cardsToDraw)));
			playerHand.set(currentPlayer, hand);
			
			if(gameDirection == false) {
				currentPlayer = (currentPlayer + 1) % playerIDs.length;
			}
			else if(gameDirection == true) {
				currentPlayer = (currentPlayer - 1) % playerIDs.length;
				if(currentPlayer == -1) {
					currentPlayer = playerIDs.length - 1;
				}
			}
				
			cardsToDraw = 0;
			
		} else {
			if(!validCardPlay(card)) {
				if(card.getSuit() != validSuit) {
					JLabel message = new JLabel("Invalid player move, expected suit: " + validSuit + " but played " + card.getSuit());
					message.setFont(new Font("Times New Roman", Font.BOLD, 16));
					JOptionPane.showMessageDialog(null, message);
					throw new InvalidSuitSubmissionException(message, card.getSuit(), validSuit);
				}
				
				else if(card.getValue() != validValue) {
					JLabel message = new JLabel("Invalid player move, expected value: " + validValue + " but played: " + card.getValue());
					message.setFont(new Font("Times New Roman", Font.BOLD, 16));
					JOptionPane.showMessageDialog(null, message);
					throw new InvalidValueSubmissionException(message, card.getValue(), validValue);
				}
			} else {
				validSuit = ace.getSuit();
				validValue = ace.getValue();
				stockPile.add(card);
				ArrayList<Card> hand = playerHand.get(currentPlayer);

				for (Card Card : hand) {
					if (Card.getValue().equals(card.getValue()) && Card.getSuit().equals(card.getSuit())) {
						hand.remove(Card);
						break;
					}
				}
				playerHand.set(currentPlayer, hand);
		
				if(gameDirection == false) {
					currentPlayer = (currentPlayer + 1) % playerIDs.length;
				}
				else if(gameDirection == true) {
					currentPlayer = (currentPlayer - 1) % playerIDs.length;
					if(currentPlayer == -1) {
						currentPlayer = playerIDs.length - 1;
					}
				}
		}
		}
	}
	
	public void drawCard() {
		
		if(deck.isEmpty()) {
			deck.replaceDeckWith(stockPile);
			stockPile.clear();
			deck.shuffle();
		}
		
		ArrayList<Card> hand = playerHand.get(currentPlayer);
		hand.add(deck.drawCard());
		playerHand.set(currentPlayer, hand);
		System.out.println("set new hand");
		
		if(gameDirection == false) {
			currentPlayer = (currentPlayer + 1) % playerIDs.length;
		}
		else if(gameDirection == true) {
			currentPlayer = (currentPlayer - 1) % playerIDs.length;
			if(currentPlayer == -1) {
				currentPlayer = playerIDs.length - 1;
			}
		}
		
	}

class InvalidPlayerTurnException extends Exception {
	String playerID;
	
	public InvalidPlayerTurnException(String message, String pid) {
		super(message);
		playerID = pid;
	}
	
	public String getPid() {
		return playerID;
	}
}

class InvalidSuitSubmissionException extends Exception {
	private Card.Suit expected;
	private Card.Suit actual;
	
	public InvalidSuitSubmissionException(JLabel message, Card.Suit actual, Card.Suit expected) {  
		this.actual = actual;
		this.expected = expected;
	}
}

class InvalidValueSubmissionException extends Exception {
	private Card.Value expected;
	private Card.Value actual;
	
	public InvalidValueSubmissionException(JLabel message, Card.Value actual, Card.Value expected) {
		this.actual = actual;
		this.expected = expected;
	}
}
}