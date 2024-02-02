package testingforvideo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class clientSideConnection{

	private Socket dataSocket; 
	private Socket objectSocket;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	String name;
	
	public clientSideConnection(String name) {
		this.name = name;
		connect();
	}
	
	public void connect() {
		try {
			dataSocket = new Socket("localhost", 51735);
			objectSocket = new Socket("localhost", 51736);
			System.out.println("connected to server");
			dataIn = new DataInputStream(dataSocket.getInputStream());
			dataOut = new DataOutputStream(dataSocket.getOutputStream());
			System.out.println(name);
			dataOut.writeUTF(name);
			dataOut.flush();
		} catch (IOException ex) {
			System.out.println("IO Exception from CSC constructor");
			ex.printStackTrace();
			try {
				Thread.sleep(500);
				connect();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void isConnected(Socket dataSocket) {
		if (dataSocket.isConnected()) {
		    System.out.println("socket is connected");
		} else {
		    System.out.println("socket is not connected");
		}

	}
	
	public boolean receiveBoolean() {
	    boolean booleanID = false;
	    
	    try {
	    	dataIn = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
	        boolean allDataReceived = false;
	        while (!allDataReceived) {
	            try {
	                booleanID = dataIn.readBoolean();
	                allDataReceived = true;
	            } catch (EOFException ex) {
	                System.out.println("EOFException caught. Reading input stream again...");
	                ex.printStackTrace();
	                try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	            }
	        }
	        return booleanID;
	    } catch(IOException ex) {
	        System.out.println("IOException from receiveTurn() method");
	        ex.printStackTrace();
	    }

	    return false;
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
	        ex.printStackTrace();
	    }

	    return null;
	}
	
	public Card receiveCard() {
	    try {
	    	if(objectSocket.isConnected()) {
	    		System.out.println("objectsocket is connected");
	    	}
	    	System.out.println("objectinputstream initialisation test");
	        objectIn = new ObjectInputStream(new BufferedInputStream(objectSocket.getInputStream()));
	        Card card = null;
	        boolean allDataReceived = false;
	        System.out.println("initialisation test complete");

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

