package testingforvideo;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class GameStage extends javax.swing.JFrame {

    /**
     * Creates new form GameStage
     */
    private setName addPlayers = new setName();
    ArrayList <String> temp = new ArrayList<>();
    String name;
    ArrayList<Card> Hand;
    //Game game;
    ArrayList<JButton> cardButtons = new ArrayList<JButton>();
    ArrayList<String> cardIds;
    //PopUp window;
    String topCardString;
    Card cardSelected;
    Boolean buttonClicked;
    String cardId;
    clientSideConnection csc;
    PickSuitFrame pickSuit;
    private Boolean getSuit;
    private Card aceSelected;
    
    // Variables declaration                 
    private javax.swing.JButton downCard;
    private javax.swing.JButton drawButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel turnLabel;
    private javax.swing.JLabel pidNameLabel;
    private javax.swing.JButton topCardButton;
    // End of variables declaration  
    
    //public GameStage() {}
    
    public GameStage(String name, ArrayList<Card> hand, String topCard, clientSideConnection csc) {
        initComponents();
        this.name = name;
        this.Hand = hand;
        this.topCardString = topCard;
        this.csc = csc;
        buttonClicked = false;
        populateArrayList();
        setPidName();
        setTopCard(topCard);

        setButtonIcons(Hand);
        addButtonActionListeners();
    }
    
    public GameStage() {
    	initComponents();
	}

	public void setTopCard(String topCard) {
        topCardButton.setIcon(new ImageIcon("/Users/reza/eclipse-workspace/coding/src/blackjackNEA/images/" + topCard));
    }
    
    public void addButtonActionListeners() {
        int i = 0;
        for (JButton cardButton : cardButtons) {
            final int index = i++; // Store the index of the button in a final variable
            cardButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    handleCardButtonAction(index);
                }
            });
        }
        drawButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleDrawButtonAction();
            }
        });
    }
    
    public void setButtonIcons(ArrayList<Card> hand){
    	// Define a comparator to sort by suit and value
    	this.Hand = hand;
    	Comparator<Card> comparator = Comparator.comparing(Card::getSuit).thenComparing(Card::getValue);

    	// Sort the cards using merge sort and the defined comparator
    	mergeSort(Hand, comparator);

    	// Convert the sorted cards to an array of strings
    	String[] cardNames = new String[Hand.size()];
    	for (int i = 0; i < Hand.size(); i++) {
    	    cardNames[i] = Hand.get(i).toString().replace(" ", "_");
    	}

    	// Print the sorted and formatted card names
    	System.out.println(String.join(",", cardNames));

        //String[] cardNames = listString.split(",");
        for (int i = 0; i < cardNames.length; i++) {
            cardNames[i] = cardNames[i].replace(" ", "_");
        }
        cardIds = new ArrayList<>(Arrays.asList(cardNames));
        for(int i = 0; i < cardIds.size(); i++){
            cardButtons.get(i).setIcon(new ImageIcon("/Users/reza/eclipse-workspace/coding/src/blackjackNEA/images/" + cardIds.get(i) + "_small.png"));
        }
        for(int i = cardIds.size(); i < cardButtons.size(); i++){
            cardButtons.get(i).setIcon(null);     //sets all the unused icons to null
        }
    }
    
    public static <T> void mergeSort(ArrayList<T> list, Comparator<T> comparator) {
        if (list.size() > 1) {
            int mid = list.size() / 2;

            ArrayList<T> left = new ArrayList<>(list.subList(0, mid));
            ArrayList<T> right = new ArrayList<>(list.subList(mid, list.size()));

            mergeSort(left, comparator);
            mergeSort(right, comparator);

            int i = 0, j = 0, k = 0;

            while (i < left.size() && j < right.size()) {
                if (comparator.compare(left.get(i), right.get(j)) < 0) {
                    list.set(k, left.get(i));
                    i++;
                } else {
                    list.set(k, right.get(j));
                    j++;
                }
                k++;
            }

            while (i < left.size()) {
                list.set(k, left.get(i));
                i++;
                k++;
            }

            while (j < right.size()) {
                list.set(k, right.get(j));
                j++;
                k++;
            }
        }
    }


    
    
    public void populateArrayList(){
        cardButtons.add(jButton1);
        cardButtons.add(jButton2);
        cardButtons.add(jButton3);
        cardButtons.add(jButton4);
        cardButtons.add(jButton5);
        cardButtons.add(jButton6);
        cardButtons.add(jButton7);
        cardButtons.add(jButton8);
        cardButtons.add(jButton9);
        cardButtons.add(jButton10);
        cardButtons.add(jButton11);
        cardButtons.add(jButton12);
        cardButtons.add(jButton13);
        cardButtons.add(jButton14);
        cardButtons.add(jButton15);
    }
    
    public void setPidName(){
        pidNameLabel.setText(name);
    }
    
    public void setTurnlabel(Boolean turn) {
    	turnLabel.setText("your turn: " + turn);
    }
    
    public void toggleAllButtons() {
        jButton1.setSelected(!jButton1.isSelected());
        jButton2.setSelected(!jButton2.isSelected());
        jButton3.setSelected(!jButton3.isSelected());
        jButton4.setSelected(!jButton4.isSelected());
        jButton5.setSelected(!jButton5.isSelected());
        jButton6.setSelected(!jButton6.isSelected());
        jButton7.setSelected(!jButton7.isSelected());
        jButton8.setSelected(!jButton8.isSelected());
        jButton9.setSelected(!jButton9.isSelected());
        jButton10.setSelected(!jButton10.isSelected());
        jButton11.setSelected(!jButton11.isSelected());
        jButton12.setSelected(!jButton12.isSelected());
        jButton13.setSelected(!jButton13.isSelected());
        jButton14.setSelected(!jButton14.isSelected());
        jButton15.setSelected(!jButton15.isSelected());
    }
    
    public boolean buttonIsActive() {
    	return jButton1.isSelected();
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel1.setBackground(new Color(254, 251, 234));
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        pidNameLabel = new javax.swing.JLabel();
        drawButton = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        downCard = new javax.swing.JButton();
        topCardButton = new javax.swing.JButton();
        turnLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Crazy Eights");
        setPreferredSize(new java.awt.Dimension(1920, 1080));

        pidNameLabel.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        pidNameLabel.setText("jLabel1");

        drawButton.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        drawButton.setText("draw card");
        drawButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawButtonActionPerformed(evt);
            }
        });

        downCard.setIcon(new javax.swing.ImageIcon("/Users/reza/NetBeansProjects/blackjackNEA/src/main/java/com/mycompany/blackjacknea/images/back_of_card.png")); // NOI18N

        turnLabel.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        turnLabel.setText("your turn:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(downCard, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(topCardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(turnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11)
                                .addGap(6, 6, 6)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pidNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(drawButton, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 116, Short.MAX_VALUE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(downCard, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .addComponent(topCardButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(turnLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pidNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(drawButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                              
    
    public void buttonClicked(ActionEvent event) {
        JButton button = (JButton) event.getSource();

        if (button == drawButton) {
            //game.draw();
        } else {
            for (int i = 0; i < cardButtons.size(); i++) {
                if (button == cardButtons.get(i)) {
                    String cardId = cardIds.get(i);
                    break;
                }
            }
        }
    }
    
    private void drawButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        
    }  
    
    public boolean buttonClicked() {
        return buttonClicked;
    }
    
    private void handleCardButtonAction(int index) {
        if (cardIds.get(index) != null) {
        	buttonClicked = true;
            cardId = cardIds.get(index);
            cardSelected = Hand.get(index);
            Hand.remove(index);
            if(cardSelected.getValue() == Card.Value.ace) {
                ace();
            } else {
            	csc.sendCard(cardSelected);
            }
        }
    }
    
    public void ace() {
    	Thread t = new Thread(new Runnable() {
            public void run() {
                System.out.println("----- ACE CODE -----");
                pickSuit = new PickSuitFrame();
                System.out.println("initialised psf");
                while(!pickSuit.clickedButton()) {
                	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                }
                aceSelected = pickSuit.chooseSuit();
                System.out.println("picked suit " + aceSelected.getSuit());
                csc.sendCard(cardSelected);
                csc.sendCard(aceSelected);
                System.out.println("sent " + aceSelected.getValue() + " of " + aceSelected.getSuit());
            }
        });
        t.start();
    }
    
    private void handleDrawButtonAction() {
    	buttonClicked = true;
    	csc.sendCard(null);
    }
    
    public void addButtonClickListener(JButton button, Card card, int index) {
        button.addActionListener((ActionEvent e) -> {
            // Update GUI
            buttonClicked = true;
            cardSelected = card;
            cardId = cardIds.get(index);
        });
    }

    
    public String returnSelection() {
    	return cardId;
    }
    
    public Card returnCard() {
    	return cardSelected;
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(0);
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(1);
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(2);
    }
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(3);
    }
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(4);
    }
    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(5);
    }
    
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(6);
    }
    
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(7);
    }
    
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(8);
    }
    
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(9);
    }
    
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(10);
    }
    
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(11);
    }
    
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(12);
    }
    
    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(13);
    }
    
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(14);
    }
    
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        handleCardButtonAction(15);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameStage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameStage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameStage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameStage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameStage().setVisible(true);
            }
        });
    }

                 
}