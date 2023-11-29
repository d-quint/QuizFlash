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
 * It is composed of a white rectangle with a JLabel in the center.
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

    // Calculate the font size based on the length of the text
    int defaultFontSize = 80;
    int fontSize = defaultFontSize;

    String question = card.getQuestion();
    String answer = card.getAnswer();

    int maxLength = Math.max(question.length(), answer.length());

    // Do a map to calculate the font size based on the length of the text
    // The longer the text (10 to 25 characters), the smaller the font size (60 px to 30 px)
    fontSize = (int) map(maxLength, 10, 25, 60, 30);

    // Construct JLabels with the adjusted font size
    JLabel front = new JLabel(styleText(question, fontSize));
    JLabel back = new JLabel(styleText(answer, fontSize));

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
    this.setBackground(new Color(250, 250, 255));

    // Set the font of the JLabel
    front.setFont(Screen.medium_font.deriveFont(23.0f));
    back.setFont(Screen.medium_font.deriveFont(23.0f));

    // Make sure this panel has margins
    this.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

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
   * Maps a value from one range to another.
   * @param value The value to map.
   * @param start1 The start of the first range.
   * @param stop1 The end of the first range.
   * @param start2 The start of the second range.
   * @param stop2 The end of the second range.
   * @return The mapped value.
   */
  private double map(double value, double start1, double stop1, double start2, double stop2) {
    if (value <= start1) {
      return start2;
    } else if (value >= stop1) {
      return stop2;
    }

    return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
  }

  /**
   * Styles a string in HTML tags.
   * @param text The text to style.
   * @param fontSize The font size of the text.
   * @return The styled text.
   */
  private String styleText(String text, int fontSize) {
    return (
      "<html>" +
        "<body>" +
          "<div style='text-align: center; font-family: \"Courier New\", \"Lucida Console\", monospace;" 
            + " font-size: " + fontSize + "px;'>" 
              + text +
          "</div>" +
        "</body>" +
      "</html>"
    );
  }

  /**
   * Flips the card.
   */
  public void flip() {
    CardLayout layout = (CardLayout) this.getLayout();
    layout.next(this);
  }
}
