package testingforvideo;

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
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;

import java.awt.EventQueue;
import java.util.ArrayList;

public class client {
	
    // instance variables
    private String name; // name of the client
    private clientSideConnection csc; // connection object to the server
    private ArrayList<Card> hand; // list of cards in the client's hand
    private String topCardString; // string representation of the top card on the discard pile
    private boolean yourTurn; // boolean indicating whether it's the client's turn
    private boolean checkWinner; // boolean indicating whether the game has a winner
    private boolean getCard; // boolean indicating whether the client is trying to draw a card
    private GameStage gs; // GUI for the game
    private String winner;

    // constructor
    public client(String name) {
        this.name = name;
        checkWinner = false;
        
        Runnable connectToServer = new Runnable() {
            public void run() {
                connectToServer(); // create a new clientSideConnection object with the given name
            }
        };
        
        Runnable startGameState = new Runnable() {
        	public void run() {
        		startGameState();
        	}
        };
        
        Runnable updateGameState = new Runnable() {
            public void run() {
            	while(!checkWinner) {
            		System.out.println("UPDATING GAME STATE RUNNABLE");
            		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		//updateGameState(); // update the game state
            		playerTurn();
            	}
                
            }
        };
        
        Runnable controlGUI = new Runnable() {
            public void run() {
                controlGUI(); // set up the GUI for the game
                //gs.toggleAllButtons();
            }
        };
        
        Runnable playerTurn = new Runnable() {
            public void run() {
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                playerTurn(); // start running the game
            }
        };
        
        Runnable updateGUI = new Runnable() {
            public void run() {
                updateGUI();
            }
        };
        
        Queue<Runnable> methodQueue = new LinkedList<Runnable>();
        methodQueue.offer(connectToServer);
        methodQueue.offer(startGameState);
        methodQueue.offer(controlGUI);
        methodQueue.offer(playerTurn);
        //methodQueue.offer(updateGUI);
        //methodQueue.offer(updateGameState);
        
        runMethodQueue(methodQueue);
    }

    private void runMethodQueue(Queue<Runnable> methodQueue) {
        if (!methodQueue.isEmpty()) {
            Runnable nextMethod = methodQueue.poll();
            nextMethod.run();
            runMethodQueue(methodQueue);
        }
    }

    // method to connect to the server
    public void connectToServer() {
        csc = new clientSideConnection(name); // create a new clientSideConnection object with the given name
    }
    
    public void playerTurn() {
    	gs.toggleAllButtons();
        System.out.println("----- STARTING TURN -----");
        if (yourTurn) { // if it's the client's turn
            gs.toggleAllButtons(); // enable all buttons
            System.out.println("buttons are enabled: " + gs.buttonIsActive());
            getCard(gs); // allow the client to draw a card
            System.out.println("SENT CARD");
            updateGameStateAlt(() -> playerTurn()); // update the game state to get the new hand and top card after drawing a card, and call playerTurn again once it's finished            gs.toggleAllButtons(); // disable all buttons again
            System.out.println("turnFinished");
        } else { // if it's not the client's turn
            System.out.println("updating game state in playerTurn");
            updateGameStateAlt(() -> playerTurn()); // update the game state to get the new hand and top card after drawing a card, and call playerTurn again once it's finished
            System.out.println("turnFinished");
        }
        System.out.println("------ TURN FINISHED -------");
        //callback.run();
    }

    
    public void controlGUI() {
    	new Thread(() -> {
        	System.out.println("----- CREATING GUI -----");
            gs = new GameStage(name, hand, topCardString, csc);
            gs.setTurnlabel(yourTurn);
            //System.out.println("attempting to show gamestage");
            gs.setVisible(true);
            System.out.println("---- GUI OPENED ----");
        }).start();
    }
    
    // method to allow the client to draw a card
    public void getCard(GameStage gs) {
        new Thread(() -> {
            getCard = true;
            while (getCard) {
                if (gs.buttonClicked()) { // if the "Draw Card" button has been clicked
                    System.out.println("BUTTON HAS BEEN CLICKED");
                    getCard = false; // stop the loop
                } else {
                    try {
                        Thread.sleep(100); // wait for 100ms before checking again
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    public void startGameState() {
    	System.out.println("----- INITIALISING GAME -----");
    	checkWinner = csc.receiveBoolean();
        System.out.println("Received checkWinner");
        hand = csc.receiveHand(); // receive a list of cards from the server
        System.out.println("hand received" + hand.toString());
        topCardString = csc.receiveCardString(); // receive a string representation of the top card from the server
        System.out.println(topCardString);
        yourTurn = csc.receiveBoolean(); // receive boolean indicating whether it's the client's turn
        System.out.println("your turn: " + yourTurn);
        System.out.println("----- FINISHED INITIALISING -----");
    }
    
    public void updateGameStateAlt(Runnable callback) {
    	new Thread(() -> {
        	System.out.println("------ UPDATING GAME STATE -----");
        	checkWinner = csc.receiveBoolean();
        	System.out.println("received checkWinner");
        	try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	hand = csc.receiveHand(); // receive a list of cards from the server
            System.out.println("hand received" + hand.toString());
            try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            topCardString = csc.receiveCardString(); // receive a string representation of the top card from the server
            System.out.println("received top card");
            try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            yourTurn = csc.receiveBoolean(); // receive boolean indicating whether it's the client's turn
            System.out.println("your turn: " + yourTurn);
            System.out.println("----- UPDATED GAME STATE -----");
            updateGUI();
            if(checkWinner) {
            	winner = csc.receiveCardString();
            	JLabel message = new JLabel("The game has ended. The winner was - " + winner);
    			message.setFont(new Font("Times New Roman", Font.BOLD, 18));
    			JOptionPane.showMessageDialog(null, message);
    			csc.closeConnection();
    			gs.dispose();
    			try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			System.exit(0);
            }
            callback.run();
        }).start();
    	
    }
    
    public void updateGUI() {
        System.out.println("----- UPDATING GUI -----");
        SwingUtilities.invokeLater(() -> {
            gs.setButtonIcons(hand);
            gs.setTopCard(topCardString);
            gs.setTurnlabel(yourTurn);
            System.out.println("Set turn label");
        });
        System.out.println("----- UPDATED GUI -----");
    }




}
