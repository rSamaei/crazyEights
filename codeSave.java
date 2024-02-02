package testingforvideo;

public class codeSave {

	/*package blackjackNEA;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.JButton;

public class client{

	private String name;
	private clientSideConnection csc;
	private ArrayList<Card> Hand;
	private String topCardString;
	private Boolean yourTurn;
	private Boolean running;
	private Boolean checkWinner;
	private Boolean getCard;
	private GameStage gs;
	private String cardSelected;
	private Card cardPlayed;
	
	public client(String name){
		this.name = name;
		connectToServer();
		setUpGUI();
		/*while(running) {
			//setUpGUIalt(name);
			runGame();
		}
		runGame();
	}

	public void connectToServer(){
		csc = new clientSideConnection(name);
	}
	
	public void setUpGUIalt(String name) {
	    Hand = csc.receiveHand();
	    System.out.println("hand received");
	    topCardString = csc.receiveCardString();
	    System.out.println(topCardString);
	    gs = new GameStage(name, Hand, topCardString, csc);
	    gs.setVisible(false); // hide the GUI initially
	    gs.toggleAllButtons();
	    running = true;
	    checkWinner = false;
	    yourTurn = csc.receiveTurn();
	    gs.setTurnlabel(yourTurn);
	    System.out.println("your turn: " + yourTurn);
	    gs.setVisible(true);
	    System.out.println("gui set up");
	}
	
	public void runGame() {
		if(yourTurn) {
			gs.toggleAllButtons();
			getCard(gs);
			//yourTurn = csc.receiveTurn();
			//updateTurn();
			gs.toggleAllButtons();
			//setUpGUIalt(name);
		} else {
			System.out.println("waiting for turn");
			Hand = csc.receiveHand();
			System.out.println("hand received");
			topCardString = csc.receiveCardString();
		    System.out.println(topCardString);
		    gs = new GameStage(name, Hand, topCardString, csc);
		}
		//updateTurn();	
	}
	
	public void setUpGUI() {
		new Thread(() -> {
			Hand = csc.receiveHand();
		    System.out.println("hand received");
		    topCardString = csc.receiveCardString();
		    System.out.println(topCardString);
		    gs = new GameStage(name, Hand, topCardString, csc);
		    gs.setVisible(false); // hide the GUI initially
		    gs.toggleAllButtons();
		    running = true;
		    checkWinner = false;
		    yourTurn = csc.receiveTurn();
		    gs.setTurnlabel(yourTurn);
		    System.out.println("your turn: " + yourTurn);
		    gs.setVisible(true);
		    System.out.println("gui set up");
		}).start();
	}

	
	public void getCard(GameStage gs) {
		new Thread(() -> {
			getCard = true;
		    while(getCard) {
		    	//System.out.println("getCardTest");
		        if(gs.buttonClicked()) {
		            System.out.println("button has been clicked");
		            getCard = false;
		        } else {
		        	try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
		    }
		}).start();
	}
	
	public void updateTurn() {
		new Thread(() -> {
			getCard = false;
		    while(getCard == false) {
		    	System.out.println("updateTurn test");
		    	cardPlayed = csc.receiveCard();
				System.out.println(cardPlayed + " was played");
				getCard = true;
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
		    }
		}).start();
	}
	
	/*private class clientSideConnection{

		private Socket dataSocket; 
		private Socket objectSocket;
		private DataInputStream dataIn;
		private DataOutputStream dataOut;
		private ObjectInputStream objectIn;
		private ObjectOutputStream objectOut;
		
		public clientSideConnection(String name) {
			System.out.println("----Client-----");
			try {
				dataSocket = new Socket("localhost", 51735);
				objectSocket = new Socket("localhost", 51736);
				dataIn = new DataInputStream(dataSocket.getInputStream());
				dataOut = new DataOutputStream(dataSocket.getOutputStream());
				System.out.println(name);
				dataOut.writeUTF(name);
				//dataIn.close();
				dataOut.flush();
			} catch (IOException ex) {
				System.out.println("IO Exception from CSC constructor");
				ex.printStackTrace();
			}
		}
		
		public void isConnected(Socket dataSocket) {
			if (dataSocket.isConnected()) {
			    System.out.println("socket is connected");
			} else {
			    System.out.println("socket is not connected");
			}

		}
		
		/*public boolean receiveTurn() {
		    boolean turnID = false;
		    
		    try {
		        ObjectInputStream objectIn = new ObjectInputStream(objectSocket.getInputStream());
		        boolean allDataReceived = false;
		        while (!allDataReceived) {
		            try {
		                turnID = (boolean) objectIn.readObject();
		                allDataReceived = true;
		            } catch (ClassNotFoundException ex) {
		                System.out.println("ClassNotFoundException from receiveTurn() method");
		            } catch (EOFException ex) {
		                System.out.println("EOFException caught. Reading input stream again...");
		            }
		        }

		        return turnID;
		    } catch(IOException ex) {
		        System.out.println("IOException from receiveTurn() method");
		        ex.printStackTrace();
		    }

		    return false;
		}
		
		public boolean receiveTurn() {
		    boolean turnID = false;
		    
		    try {
		    	dataIn = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
		        boolean allDataReceived = false;
		        while (!allDataReceived) {
		            try {
		                turnID = dataIn.readBoolean();
		                allDataReceived = true;
		            } catch (EOFException ex) {
		                System.out.println("EOFException caught. Reading input stream again...");
		            }
		        }
		        //dataIn.close();
		        return turnID;
		    } catch(IOException ex) {
		        System.out.println("IOException from receiveTurn() method");
		        ex.printStackTrace();
		    }

		    return false;
		}

		public boolean checkWinner() {
			boolean winnerID = false;
			try{
				winnerID = dataIn.readBoolean();
			}catch(IOException e) {
				System.out.println("IOException from checkWinner();");
				e.printStackTrace();
			}
			return winnerID;
		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<Card> receiveHand() {
		    try {
		        ObjectInputStream objectIn = new ObjectInputStream(objectSocket.getInputStream());
		        ArrayList<Card> hand = new ArrayList<Card>();
		        boolean allDataReceived = false;

		        while (!allDataReceived) {
		            try {
		                hand = (ArrayList<Card>) objectIn.readObject();
		                allDataReceived = true;
		            } catch (ClassNotFoundException ex) {
		                System.out.println("ClassNotFoundException from receiveHand() method");
		            } catch (EOFException ex) {
		                System.out.println("EOFException caught. Reading input stream again...");
		            }
		        }
		        return hand;
		    } catch(IOException ex) {
		        System.out.println("IOException from receiveHand() method");
		    }

		    return null;
		}
		
		public Card receiveCard() {
		    try {
		        ObjectInputStream objectIn = new ObjectInputStream(objectSocket.getInputStream());
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
		
		public String receiveCardString() {
		    String card = null;
		    
		    boolean allDataReceived = false;

		    try {
		        ObjectInputStream objectIn = new ObjectInputStream(objectSocket.getInputStream());

		        while (!allDataReceived) {
		            try {
		                card = (String) objectIn.readObject();
		                allDataReceived = true;
		            } catch (ClassNotFoundException ex) {
		                System.out.println("ClassNotFoundException from receiveCardString() method");
		            } catch (EOFException ex) {
		                System.out.println("EOFException caught. Reading input stream again...");
		            }
		        }

		        return card;
		    } catch(IOException ex) {
		        System.out.println("IOException from receiveCardString() method");
		    }

		    return null;
		}

		public void sendCard(Card card) {
			try {
				objectOut = new ObjectOutputStream(new BufferedOutputStream(objectSocket.getOutputStream()));
				objectOut.writeObject(card);
				objectOut.flush();
			}catch(IOException e) {
				System.out.println("Error from sendCard() method");
			}
		}
		
		/*public String receiveCardString() {
		    String card = null;
		    
		    boolean allDataReceived = false;

	        while (!allDataReceived) {
	            try {
	                card = dataIn.readUTF();
	                allDataReceived = true;
	            }catch(IOException ex) {
	            	ex.printStackTrace();
	            	System.out.println("IOException from receiveCardString()");
	            }
		    }
		    return card;
		}

		
		public void closeConnection() {
			try {
				dataSocket.close();
				objectSocket.close();
				System.out.println("Connection closed.");
			} catch (IOException ex) {
				System.out.println("IOException on closeConnection() CSC");
			}
		}
		
	}

	
	private void checkWinner() {
		csc.closeConnection();
	}
	
	
	
	public static void main(String[] args) {
		
	}
	
}*/

	
}
