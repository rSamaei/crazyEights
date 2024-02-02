package testingforvideo;

import java.net.*;
import java.util.*;

import testingforvideo.Game.InvalidPlayerTurnException;
import testingforvideo.Game.InvalidSuitSubmissionException;
import testingforvideo.Game.InvalidValueSubmissionException;

import java.io.*;

public class server{

	private ServerSocket sd;
	private ServerSocket so;
	private String[] playerIDs = new String[4];
	private int numPlayers;
	Game game;
	private serverSideConnection[] players = new serverSideConnection[4];
	private boolean running;
	private int currentP;
	private Card cardPlayed;
	private Card acePlayed;
	private Boolean checkWinner;
	
	public server() {
		System.out.println("------Game Server--------");
		numPlayers = 0;
		running = false;
		try {
			sd = new ServerSocket(51735);
			so = new ServerSocket(51736);
		} catch(IOException ex) {
			System.out.println("IOException from GameServer Constructor");
		}
	}
	
	
	public void acceptConnections() {
		
		DataInputStream in;
		
		try {
			System.out.println("Waiting for connections...");
			while(numPlayers < 4) {
				Socket socketd = sd.accept();
				Socket socketo = so.accept();
				in = new DataInputStream(socketd.getInputStream());
				playerIDs[numPlayers] = in.readUTF();
				System.out.println(playerIDs[numPlayers] + " has connected.");
				serverSideConnection ssc = new serverSideConnection(socketd, socketo, playerIDs, numPlayers);
				players[numPlayers] = ssc;
				Thread t = new Thread(ssc);
				t.start();
				numPlayers++;
			}
			System.out.println("We now have 4 players. No longer accepting connections.");
		} catch(IOException ex) {
			System.out.println("IOExcption from acceptConnections()");
			ex.printStackTrace();
		}
	}
	
	public void updateTurn() {
		System.out.println("----- UPDATING GAME -----");
		 for(int i = 0; i < 4; i++) {
			 	players[i].sendTurn(checkWinner);
			 	System.out.println("sent checkWinner");
			 	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	players[i].sendHand();
	        	System.out.println("sent hand " + i );
	        	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	players[i].sendCardString(game.getTopCardImageString());
	        	System.out.println(game.getTopCardImageString());
	        	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	System.out.println("sent topcard " + i);
	        	if(i == currentP) {
	        		players[i].sendTurn(true);
	        		System.out.println("sent turn " + i + true);
	        		try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}else {
	        		players[i].sendTurn(false);
	        		System.out.println("Sent turn " + i + false);
	        		try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
		 System.out.println("----- FINISHED UPDATING ------");
	}
	
	public void receiveTurn() {
		System.out.println("----- RECEIVING TURN ----");
    	cardPlayed = players[game.getCurrentPlayerInt()].receiveCard();
    	if(cardPlayed != null) {
    		System.out.println("received " + cardPlayed.getValue() + "of" + cardPlayed.getSuit());
    		if(cardPlayed.getValue() == Card.Value.ace) {
    	    	acePlayed = players[game.getCurrentPlayerInt()].receiveCard();
    	    	System.out.println("received " + acePlayed.getValue() + "of" + acePlayed.getSuit());
    	    	playAce(cardPlayed, acePlayed);
    	    	game.getTopCardImageString();
    		} else {
    			System.out.println(cardPlayed.getValue() + "of" + cardPlayed.getSuit());
    			playCard(cardPlayed);
    			System.out.println("----- PLAYED " + cardPlayed.getValue() + "of" + cardPlayed.getSuit() + " -----");
    		}
    	} else {
    		game.drawCard();
    		System.out.println("----- DRAWN CARD -----");
    		game.getTopCardImageString();
    	}
    	System.out.println("---- RECEIVED TURN ----");
	}
	
	public void startGame() {
		game = new Game(playerIDs);
        game.start(game);
        System.out.println("started game");
        currentP = game.getCurrentPlayerInt();
        checkWinner = false;

        running = true;
    	
    	while(running) {
            System.out.println("the current player is " + game.getCurrentPlayer());
    		updateTurn();
    		if(checkWinner) {
    			for(int i = 0; i < 4; i++) {
    				players[i].sendCardString("The winner of this match is - " + game.gameWinner());
    			}
    			for(int i = 0; i < 4; i++) {
    				players[i].closeConnection();
    			}
    			running = false;
    		}
    		receiveTurn();
    		System.out.println("------ ROUND FINISHED -------");
    		currentP = game.getCurrentPlayerInt();
    		checkWinner = game.checkWinner();
    	}
       
        
	}
	
	public void playCard(Card card) {
		try {
			game.playCard(card);
		} catch (InvalidSuitSubmissionException | InvalidValueSubmissionException | InvalidPlayerTurnException e) {
			e.printStackTrace();
		}
		
	}
	
	public void playAce(Card card, Card ace) {
		try {
			game.playAce(card, ace);
		} catch (InvalidSuitSubmissionException | InvalidValueSubmissionException e) {
			e.printStackTrace();
		}
		
	}
	
	
private class serverSideConnection implements Runnable {
		
		private Socket serverDataSocket;
		private Socket serverObjectSocket;
		private DataInputStream dataIn;
		private DataOutputStream dataOut;
		private ObjectInputStream objectIn;
		private ObjectOutputStream objectOut;
		private String[] playerNames;
		private int id;
		
		public serverSideConnection(Socket sd, Socket so, String[] pids, int num) {
			serverDataSocket = sd;
			serverObjectSocket = so;
			playerNames = pids;
			id = num;
			
		}
		
		public void run() {
			while(running==true) {
				System.out.println("testHandOne");
				sendHand();
				System.out.println("testHandTwo");
			}
		}
		
		public void sendHand() {
		    try {
		        ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(serverObjectSocket.getOutputStream()));
		        objectOut.writeObject(game.getPlayerHand(playerNames[id]));
		        objectOut.flush(); // flush the output stream to ensure all data is sent
		    } catch (IOException e) {
		        System.out.println("error from sendHand() method");
		        e.printStackTrace();
		    }
		}
		
		public void sendTurn(boolean turn) {
			try {
				dataOut = new DataOutputStream(new BufferedOutputStream(serverDataSocket.getOutputStream()));
				dataOut.writeBoolean(turn);
				dataOut.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		public void sendCardString(String card) {
		    try {
		        ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(serverObjectSocket.getOutputStream()));
		        objectOut.writeObject(card);
		        objectOut.flush();
		    } catch (IOException e) {
		        System.out.println("Error from sendCardString() method");
		        e.printStackTrace();
		    }
		}
		
		public void sendCard(Card card) {
			try {
				objectOut = new ObjectOutputStream(new BufferedOutputStream(serverObjectSocket.getOutputStream()));
				objectOut.writeObject(card);
				dataOut.flush();
				System.out.println("Card sent");
			}catch(IOException e) {
				System.out.println("Error from sendCard() method");
			}
		}
		
		@SuppressWarnings("unchecked")
		public Card receiveCard() {
		    try {
		        ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(serverObjectSocket.getInputStream()));
		        Card card = null;
		        boolean allDataReceived = false;

		        while (!allDataReceived) {
		            try {
		                card = (Card) objectIn.readObject();
		                allDataReceived = true;
		            } catch (ClassNotFoundException ex) {
		                System.out.println("ClassNotFoundException from receiveCard() method");
		            } catch (EOFException ex) {
		                System.out.println("EOFException caught. Reading input stream again...");
		            }
		        }

		        return card;
		    } catch(IOException ex) {
		        System.out.println("IOException from receiveCard() method");
		        ex.printStackTrace();
		    }

		    return null;
		}
		
		public void closeConnection() {
			try {
				serverDataSocket.close();
				serverObjectSocket.close();
				System.out.println("Connection closed.");
			} catch (IOException ex) {
				System.out.println("IOException from closeConnection() SSC");
			}
		}
		
	}
	
	public static void main(String[] args) {
		server s = new server();
		s.acceptConnections();
		s.startGame();
	}
	
}
