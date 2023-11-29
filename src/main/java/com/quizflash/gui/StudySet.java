package com.quizflash.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.quizflash.Screen;
import com.quizflash.logic.Card;
import com.quizflash.logic.CardSet;
import com.quizflash.logic.SetHandler;

/**
 * A designated user interface to study the current set.
 */
public class StudySet extends JPanel {
  JPanel center_panel = new JPanel();
  CardGUI card_gui;

  JButton[] buttons = {
    new JButton("Got It Wrong"),
    new JButton("Got It Right"),
    new JButton("Back")
  };

  // A panel containing the panels for the card and its two buttons
  JPanel card_panel;
  JPanel wrong_panel;
  JPanel right_panel;
  JPanel card_container;

  // A panel containing the progress bar and the label for the remaining cards count and card set name
  JPanel progress_panel;
  JPanel progress_label_panel;
  JPanel progress_bar_panel;
  JLabel remaining_cards_count;
  JProgressBar progress_bar;

  // A panel containing the toggle buttons for the card
  JPanel toggle_panel;

  // A toggle button that allows the user to switch between the front and back of the card
  JToggleButton flip_btn = new JToggleButton("Flip");

  // A toggle button that allows the user to randomize the order of the cards
  JToggleButton randomize_btn = new JToggleButton("Toggle Randomized Mode");

  /**
   * Constructs a new Edit Set screen.
   */
  public StudySet() {
    super();
    initToggleBtnPanel();
    initCenterPanel();
    initScreen();
  }

  /**
   * Initializes the toggle button panel.
   */
  private void initToggleBtnPanel() {
    toggle_panel = new JPanel();
    toggle_panel.setLayout(new FlowLayout(FlowLayout.CENTER));

    // Initialize the toggle buttons
    flip_btn.setSelected(false);
    randomize_btn.setSelected(false);

    // Add action listeners to the toggle buttons

    // Flip button
    flip_btn.addActionListener(e -> {
      if (flip_btn.isSelected()) {
        flip_btn.setText("Flip Back");
        card_gui.flip();
      } else {
        flip_btn.setText("Flip");
        card_gui.flip();
      }
    });

    // Randomize button
    randomize_btn.addActionListener(e -> {
      if (randomize_btn.isSelected()) {
        randomize_btn.setText("Back to Sequential Mode");
        SetHandler.toggleRandomization();
      } else {
        randomize_btn.setText("Toggle Randomized Mode");
        SetHandler.toggleRandomization();
      }
    });

    // Add a button to go back to the previous screen
    buttons[2].addActionListener(e -> Screen.getGUIScreen().switchTo("Main Menu"));

    // Add buttons to the toggle panel
    toggle_panel.add(flip_btn);
    toggle_panel.add(randomize_btn);
    toggle_panel.add(buttons[2]);
  }

  /**
   * Displays the card gui of a card.
   * @param card the card to display
   */
  private void displayCard(Card card) {
    String identifier = "Card " + SetHandler.getCurrentCardIndex();

    card_gui = new CardGUI(card);
    card_container.add(card_gui, identifier);

    CardLayout cl = (CardLayout) card_container.getLayout();
    cl.show(card_container, identifier);

    if (flip_btn.isSelected()) {
      card_gui.flip();
    }
  }

  /**
    * Initializes the center panel.
    */
  private void initCenterPanel() {
    // This contains the card panel and the progress panel
    center_panel.setLayout(new BorderLayout());

    if (SetHandler.getCurrentSet() == null) {
      return;
    }

    String now_studying = "Now Studying: \"" + SetHandler.getCurrentSet().getName() + "\"";
    
    center_panel.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), now_studying, TitledBorder.CENTER, TitledBorder.TOP, Screen.medium_font));

    initCardPanel();
    initProgressPanel();

    center_panel.add(card_panel, BorderLayout.CENTER);
    center_panel.add(progress_panel, BorderLayout.SOUTH);
  }

  /**
   * Initializes the progress panel.
   */
  private void initProgressPanel() {
    progress_panel = new JPanel();
    progress_panel.setLayout(new BorderLayout());

    // Initializing the progress label panel
    progress_label_panel = new JPanel();
    progress_label_panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    JLabel remaining_cards_label = new JLabel("Remaining Cards: ");
    remaining_cards_count = new JLabel(
      String.valueOf(
        SetHandler.getCurrentSet().getUnknownCardCount()) 
          + "/" + 
      String.valueOf(
        SetHandler.getCurrentSet().getCardCount()));

    progress_label_panel.add(remaining_cards_label);
    progress_label_panel.add(remaining_cards_count);

    // Initializing the progress bar panel
    progress_bar_panel = new JPanel();
    progress_bar_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

    progress_bar = new JProgressBar(0, SetHandler.getCurrentSet().getCardCount());

    progress_bar_panel.setLayout(new BoxLayout(progress_bar_panel, BoxLayout.Y_AXIS));
    progress_bar_panel.add(Box.createVerticalGlue());
    progress_bar_panel.add(progress_bar);
    progress_bar_panel.add(Box.createVerticalGlue());

    progress_panel.add(progress_label_panel, BorderLayout.WEST);
    progress_panel.add(progress_bar_panel, BorderLayout.CENTER);
  }

  /**
   * Initializes the card panel.
   */
  private void initCardPanel() {
    card_panel = new JPanel();
    card_panel.setLayout(new BorderLayout());

    // Initializing the card container which houses many panels for each card and putting it in a scroll pane
    card_container = new JPanel();
    card_container.setLayout(new CardLayout());
    displayCard(SetHandler.getCurrentCard());

    // Initializing the "got it wrong" panel
    wrong_panel = new JPanel();
    wrong_panel.setLayout(new GridLayout(1, 1));
    wrong_panel.add(buttons[0]);

    // Initializing the "got it right" panel
    right_panel = new JPanel();
    right_panel.setLayout(new GridLayout(1, 1));
    right_panel.add(buttons[1]);

    // Add action listeners to the buttons

    // Got It Wrong button
    buttons[0].addActionListener(e -> {
      Card current_card = SetHandler.getCurrentCard();

      current_card.setKnowsThis(false);
      displayCard(SetHandler.getNextCard());
    });

    // Got It Right button
    buttons[1].addActionListener(e -> {
      CardSet current_set = SetHandler.getCurrentSet();
      Card current_card = SetHandler.getCurrentCard();

      current_card.setKnowsThis(true);
      remaining_cards_count.setText(
        String.valueOf(
          SetHandler.getCurrentSet().getUnknownCardCount()) 
            + "/" + 
        String.valueOf(
          SetHandler.getCurrentSet().getCardCount()));
      progress_bar.setValue(current_set.getKnownCardCount());

      // Check if the set is finished
      if (SetHandler.getCurrentSet().getUnknownCardCount() == 0) {
        JOptionPane.showMessageDialog(null, "You have finished studying this set!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        Screen.getGUIScreen().switchTo("Main Menu");

        // Reset the card set and the progress bar
        SetHandler.setCurrentCardIndex(0);
        current_set.resetKnownCards();
        progress_bar.setValue(0);
        remaining_cards_count.setText(
          String.valueOf(
            SetHandler.getCurrentSet().getUnknownCardCount()) 
              + "/" + 
          String.valueOf(
            SetHandler.getCurrentSet().getCardCount()));

        return;
      }

      displayCard(SetHandler.getNextCard());
    });

    card_panel.add(card_container, BorderLayout.CENTER);
    card_panel.add(wrong_panel, BorderLayout.WEST);
    card_panel.add(right_panel, BorderLayout.EAST);
  }

  /**
   * Initializes the screen.
   */
  private void initScreen() {
    TitledBorder studyset_border = BorderFactory.createTitledBorder("Study Set");
    studyset_border.setTitleJustification(TitledBorder.RIGHT);
    studyset_border.setTitleFont(Screen.small_font);

    this.setLayout(new BorderLayout());
    this.setBorder(studyset_border);
    this.add(center_panel, BorderLayout.CENTER);
    this.add(toggle_panel, BorderLayout.SOUTH);
  }
}
