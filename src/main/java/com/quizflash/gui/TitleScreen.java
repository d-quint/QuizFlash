package com.quizflash.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.quizflash.Screen;
import com.quizflash.logic.SetHandler;

/**
 * A title screen that displays an image at the center of the title card and a bunch of buttons at the bottom that redirect to the other screens (Study Set, Create Set, Edit Current Set).
 */
public class TitleScreen extends JPanel {
  JPanel center_panel = new JPanel();
  JPanel button_panel = new JPanel();

  ImageIcon title_card = new ImageIcon("src/main/resources/title.png");
  JLabel title_card_label = new JLabel(title_card);

  JButton[] buttons = {
    new JButton("Study Set"),
    new JButton("Create Set"),
    new JButton("Load Set"),
    new JButton("Edit Current Set"),
    new JButton("Exit")
  };

  /**
   * Constructs a new title screen.
   */
  public TitleScreen() {
    super();
    initBtnPanel();
    initCenterPanel();
    initScreen();
  }

  /**
   * Initializes the button panel.
   */
  private void initBtnPanel() {
    button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.Y_AXIS));

    for (JButton button : buttons) {
      button.setAlignmentX(CENTER_ALIGNMENT);

      button_panel.add(button);
    }

    button_panel.add(Box.createVerticalGlue());

    button_panel.setSize(100, 100);

    initActionListeners();
  }

  /**
   * Initializes the action listeners for the buttons.
   */
  private void initActionListeners() {
    // Study Set
    buttons[0].addActionListener(e -> {
      if (SetHandler.getCurrentSet() == null) {
        JOptionPane.showMessageDialog(null, "Please load a card set first.", "ERROR", JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (SetHandler.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please add cards to the set first.", "ERROR", JOptionPane.ERROR_MESSAGE);
        Screen.getGUIScreen().switchTo("Edit Current Set");
        return;
      }

      Screen.getGUIScreen().switchTo("Study Set");
    });

    // Create Set
    buttons[1].addActionListener(e -> {
      if (Screen.createSet()) {
        JOptionPane.showMessageDialog(null, "Successfully created card set.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // Load Set
    buttons[2].addActionListener(e -> {
      if (Screen.loadCardSet()) {
        // Ask user if they want to study it or edit it
        String[] options = {"Study", "Edit"};
        int choice = JOptionPane.showOptionDialog(
          null, "Successfully loaded card set. Would you like to study it or edit it?", 
          "SUCCESS", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice == 0) {
          if (SetHandler.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please add cards to the set first.", "ERROR", JOptionPane.ERROR_MESSAGE);
            Screen.getGUIScreen().switchTo("Edit Current Set");
            return;
          }

          Screen.getGUIScreen().switchTo("Study Set");
        } else {
          Screen.getGUIScreen().switchTo("Edit Current Set");
        }
      } else {
        System.out.println("Failed to load card set.");
      }
    });

    // Edit Current Set
    buttons[3].addActionListener(e -> {
      if (SetHandler.getCurrentSet() == null) {
        JOptionPane.showMessageDialog(null, "Please load a card set first.", "ERROR", JOptionPane.ERROR_MESSAGE);
        return;
      }

      Screen.getGUIScreen().switchTo("Edit Current Set");
    });

    buttons[4].addActionListener(e -> {
      System.exit(0);
    });
  }

  /**
    * Initializes the center panel.
    */
  private void initCenterPanel() {
    // This contains the title card image and the button panel
    center_panel.setLayout(new GridBagLayout());
    
    TitledBorder title_border = BorderFactory.createTitledBorder("Main Menu");
    title_border.setTitleJustification(TitledBorder.RIGHT);
    title_border.setTitleFont(Screen.small_font);
    
    center_panel.setBorder(title_border);

    GridBagConstraints gbc = new GridBagConstraints();
    
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.BOTH;

    center_panel.add(title_card_label, gbc);
    
    gbc.gridy = 1;
    gbc.weighty = 0.9;
    center_panel.add(button_panel, gbc);
  }

  /**
   * Initializes the screen.
   */
  private void initScreen() {
    this.setLayout(new BorderLayout());
    this.add(center_panel, BorderLayout.CENTER);
  }
}
