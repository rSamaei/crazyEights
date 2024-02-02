package testingforvideo;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;


public class player extends JFrame{
	
	private int width;
	private int height;
	private Container contentPane;
	private JTextArea message;
	
	private clientSideConnection csc;
	
	public player(int w, int h){
		width = w;
		height = h;
		contentPane = this.getContentPane();
		message = new JTextArea();
	}
	
	public void setName() {
		this.setSize(800, 400);
		this.setTitle("Set Username");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(new GridLayout(1,2));
		contentPane.add(message);
		message.setText("Please enter you username:");
		message.setWrapStyleWord(true);
		message.setLineWrap(true);
		message.setEditable(false);
		
	}
	
	public void setUpGUI() {
		this.setSize(width, height);
		this.setTitle("Test");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(new GridLayout(1,5));
		contentPane.add(message);
		message.setText("Creating a simple turn-based game in Java.");
		message.setWrapStyleWord(true);
		message.setLineWrap(true);
		message.setEditable(false);
		
		this.setVisible(true);
	}
	
	public void connectToServer() {
		csc = new clientSideConnection();;
	}
	
	private class clientSideConnection{
		
		private Socket socket; 
		private DataInputStream dataIn;
		private DataOutputStream dataOut;
		
		public clientSideConnection() {
			System.out.println("----Client-----");
			try {
				socket = new Socket("localhost", 51735);
				dataIn = new DataInputStream(socket.getInputStream());
				dataOut = new DataOutputStream(socket.getOutputStream());
			} catch (IOException ex) {
				System.out.println("IO Exception from CSC constructor");
			}
		}
		
		public void closeConnection() {
			try {
				socket.close();
				System.out.println("Connection closed.");
			} catch (IOException ex) {
				System.out.println("IOException on closeConnection() CSC");
			}
		}
		
	}
	
	public static void main(String[] args) {
		player p = new player(1000, 700);
		p.setUpGUI();
		//p.connectToServer();
	}
	
}
