package com.quizflash.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.quizflash.Screen;
import com.quizflash.logic.Card;
import com.quizflash.logic.CardSet;
import com.quizflash.logic.SetHandler;

import java.awt.Color;

/**
 * A designated user interface to edit the current set.
 */
public class EditSet extends JPanel {
  JPanel center_panel = new JPanel();
  JPanel button_panel = new JPanel();

  JButton[] buttons = {
    new JButton("Save Set"),
    new JButton("Add Card"),
    new JButton("Remove Card"),
    new JButton("Back")
  };

  JSplitPane info_table_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

  JTable cards_table;
  DefaultTableModel cards_table_model;
  JScrollPane cards_table_pane;
  JScrollPane description_pane;

  JPanel set_info_panel;

  JTextField set_name_field;
  JTextArea set_description_area;

  JLabel count_label;

  /**
   * Constructs a new Edit Set screen.
   */
  public EditSet() {
    super();
    initBtnPanel();
    initCenterPanel();
    initScreen();
  }

  /**
   * Initializes the button panel.
   */
  private void initBtnPanel() {
    button_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    button_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    for (JButton button : buttons) {
      button_panel.add(button);
    }

    initActionListeners();
  }

  /**
   * Initializes the action listeners for the buttons.
   */
  private void initActionListeners() {
    // Save Set
    buttons[0].addActionListener(e -> {
      String new_name = set_name_field.getText();
      String new_description = set_description_area.getText();

      CardSet current_set = SetHandler.getCurrentSet();
      
      if (current_set == null) {
        return;
      }
      
      current_set.setName(new_name);
      current_set.setDescription(new_description);

      if (Screen.saveCardSet()) {
        JOptionPane.showMessageDialog(null, "Card set saved successfully!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(null, "Card set could not be saved!", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
    });

    // Add Card
    buttons[1].addActionListener(e -> {
      CardSet current_set = SetHandler.getCurrentSet();
      
      if (current_set == null) {
        return;
      }
      
      // Create a new blank row in the table
      cards_table_model.addRow(new String[] {"", ""});

      // Focus on the new row
      cards_table.setRowSelectionInterval(cards_table.getRowCount() - 1, cards_table.getRowCount() - 1);

      // Update card count
      count_label.setText(String.valueOf(current_set.getCardCount()));
    });

    // Remove Card
    buttons[2].addActionListener(e -> {
      CardSet current_set = SetHandler.getCurrentSet();
      
      if (current_set == null) {
        return;
      }
      
      // Get the selected row
      int selected_row = cards_table.getSelectedRow();

      if (selected_row == -1) {
        JOptionPane.showMessageDialog(null, "Please select a card to remove!", "ERROR", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Remove the row from the table and the set
      current_set.removeCard(selected_row);
      cards_table_model.removeRow(selected_row);

      // Update card count
      count_label.setText(String.valueOf(current_set.getCardCount()));
    });

    // Back
    buttons[3].addActionListener(e -> Screen.getGUIScreen().switchTo("Main Menu"));
  }

  /**
    * Initializes the center panel.
    */
  private void initCenterPanel() {
    // This contains the cards table and the set info panel
    center_panel.setLayout(new BorderLayout());

    set_info_panel = new JPanel();
    cards_table_pane = new JScrollPane();

    initTable();

    // This contains the cards table
    cards_table_pane.setBorder(
      BorderFactory.createTitledBorder(
        BorderFactory.createEtchedBorder(), "Cards", TitledBorder.CENTER, TitledBorder.TOP, Screen.medium_font));

    initSetInfo();

    // This contains the set info panel
    set_info_panel.setBorder(
      BorderFactory.createTitledBorder(
        BorderFactory.createEtchedBorder(), "Set Information", TitledBorder.CENTER, TitledBorder.TOP, Screen.medium_font));

    // Add the two to a split pane
    info_table_pane.setLeftComponent(set_info_panel);
    info_table_pane.setRightComponent(cards_table_pane);
    info_table_pane.setDividerLocation(0.35);
    
    // Add the split pane to the center panel
    center_panel.add(info_table_pane, BorderLayout.CENTER);
  }

  /**
   * Initializes the set info panel.
   */
  private void initSetInfo() {
    // This displays all information about the card set.
    // It also allows the user to update the set's name and description through text fields and areas.

    CardSet current_set = SetHandler.getCurrentSet();
    
    if (current_set == null) {
      return;
    }

    set_name_field = new JTextField(20);
    set_description_area = new JTextArea(10, 20);

    set_description_area.setWrapStyleWord(true);
    set_description_area.setLineWrap(true);

    set_name_field.setText(current_set.getName());
    set_description_area.setText(current_set.getDescription());

    set_name_field.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent evt) {
        current_set.setName(set_name_field.getText());
      }
    });

    set_description_area.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent evt) {
        current_set.setDescription(set_description_area.getText());
      }
    });

    set_info_panel.setLayout(new BoxLayout(set_info_panel, BoxLayout.Y_AXIS));

    JPanel set_name_panel = new JPanel();
    JPanel set_name_field_panel = new JPanel();
    set_name_panel.setLayout(new FlowLayout());
    
    set_name_field_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    set_name_field_panel.add(set_name_field);

    set_name_panel.add(new JLabel("Card Set Name: "));
    set_name_panel.add(set_name_field_panel);

    description_pane = new JScrollPane(set_description_area);

    JPanel card_count_panel = new JPanel();
    card_count_panel.setLayout(new FlowLayout());

    int card_count = current_set.getCardCount();
    count_label = new JLabel(String.valueOf(card_count));

    card_count_panel.add(new JLabel("Current Card Count: "));
    card_count_panel.add(count_label);

    JPanel set_description_panel = new JPanel();
    set_description_panel.setLayout(new BoxLayout(set_description_panel, BoxLayout.Y_AXIS));

    set_description_panel.add(new JLabel("Card Set Description: "));
    set_description_panel.add(description_pane);

    set_info_panel.add(set_name_panel);
    set_info_panel.add(Box.createVerticalStrut(50));
    set_info_panel.add(set_description_panel);
    set_info_panel.add(Box.createVerticalStrut(10));
    set_info_panel.add(card_count_panel);
  }

  private void initTable() {
    // This is a table with all the cards in the set.
    // It has two columns: one for the front of the card and one for the back of the card.
    // The user can add and remove cards from this table.

    CardSet current_set = SetHandler.getCurrentSet();
    
    if (current_set == null) {
      return;
    }

    String[][] card_data = current_set.getData();

    String[] column_names = {
      "Front",
      "Back"
    };

    cards_table_model = new DefaultTableModel(card_data, column_names);
    cards_table = new JTable(cards_table_model);
    cards_table_pane = new JScrollPane(cards_table);

    // Set the grid lines to be visible and change the grid color
    cards_table.setShowGrid(true);
    cards_table.setShowVerticalLines(true);

    // Set the grid color to #ED4915
    Color gridColor = Color.decode("#818181"); //apply grid for question bank visibility
    cards_table.setGridColor(gridColor);


    // Add listeners to the table (check if a row is selected and edited, then get the new values and make a new card)
    cards_table.getModel().addTableModelListener(e -> {
      // If the card already exists, only edit it using the CardSet's editCard method
      // If the card does not exist, create a new card and add it to the set
      if (e.getType() == TableModelEvent.DELETE) {
        return;
      }
      
      int row = e.getFirstRow();

      String front = (String) cards_table.getValueAt(row, 0);
      String back = (String) cards_table.getValueAt(row, 1);

      Card card = current_set.getCard(row);

      if (card != null) {
        current_set.editCard(row, new Card(front, back));
      } else {
        current_set.addCard(new Card(front, back));
      }
    });

    cards_table.setFillsViewportHeight(true);
  }

  /**
   * Initializes the screen.
   */
  private void initScreen() {
    TitledBorder editset_border = BorderFactory.createTitledBorder("Edit Current Set");
    editset_border.setTitleJustification(TitledBorder.RIGHT);
    editset_border.setTitleFont(Screen.small_font);

    this.setLayout(new BorderLayout());
    this.setBorder(editset_border);
    this.add(center_panel, BorderLayout.CENTER);
    this.add(button_panel, BorderLayout.SOUTH);
  }
}
