package com.quizflash.gui;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.quizflash.Screen;
import com.quizflash.logic.*;

/**
 * A panel that displays a flashcard.
 * It has two sides: the front and the back.
 * It is composed of a round rectangle with a JLabel in the center.
 */
public class CardGUI extends JPanel { 
  private Card card;

  /**
   * Constructs a new card GUI.
   * @param card The card to display.
   */
  public CardGUI(Card card) {
    this.card = card;

    initCard();
  }

  /**
   * Gets the card.
   * @return The card.
   */
  public Card getCard() {
    return card;
  }

  /**
   * Draws the card.
   */
  private void initCard() {
    this.setLayout(new CardLayout());

    if (card == null) {
      return;
    }

    //created font size auto adjustment for card
    // Calculate the font size based on the length of the text
    int defaultFontSize = 80;
    int fontSize = defaultFontSize;

    String question = card.getQuestion();
    String answer = card.getAnswer();

    int maxLength = Math.max(question.length(), answer.length());

// Adjust the font size based on the length of the longer text
    if (maxLength > 10 && maxLength <= 70) {
      fontSize = (int) (defaultFontSize * 10.0 / maxLength);
    }

    else if (maxLength >70){
      fontSize = 20;
    }

// Construct JLabels with the adjusted font size
    JLabel front = new JLabel("<html> <h1 style=\"color: #3A3B3C; text-align: center; font-size: " + fontSize + "px; font-family: 'Montserrat', sans-serif\">" + question + "</h1> </html>");
    JLabel back = new JLabel("<html> <h1 style=\"color: #3A3B3C; text-align: center; font-size: " + fontSize + "px; font-family: 'Montserrat', sans-serif\">" + answer + "</h1> </html>");


    // Surround the JLabel with a rectangle border
    this.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createRaisedBevelBorder(),
        BorderFactory.createLoweredBevelBorder()
      )
    );

    // Set the size of the card
    this.setSize(200, 200);

    // Set the background color of the card
    this.setBackground(new Color(251, 251, 255)); // color adjustments

    // Set the font of the JLabel
    front.setFont(Screen.medium_font.deriveFont(23.0f));
    back.setFont(Screen.medium_font.deriveFont(23.0f));

    // Make sure this panel has margins
    this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Draw the JLabel in the center of the rectangle
    front.setHorizontalAlignment(JLabel.CENTER);
    back.setHorizontalAlignment(JLabel.CENTER);

    front.setVerticalAlignment(JLabel.CENTER);
    back.setVerticalAlignment(JLabel.CENTER);
    
    // Text should wrap around when it reaches the end of the rectangle
    // JLabel is the same size as the rectangle
    front.setSize(this.getSize());
    back.setSize(this.getSize());

    this.add(front, "front");
    this.add(back, "back");
  }

  /**
   * Flips the card.
   */
  public void flip() {
    CardLayout layout = (CardLayout) this.getLayout();
    layout.next(this);
  }
}
