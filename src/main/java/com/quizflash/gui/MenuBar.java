package com.quizflash.gui;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.formdev.flatlaf.intellijthemes.*;
import com.quizflash.Screen;

/**
 * The main menu bar of the application.
 */
public class MenuBar extends JMenuBar {
  String[][] menu_items = {
    {"File", "New Set", "Load Set", "Save Set", "Save Set As", "Exit"},
    {"Theme", "Light", "Dark", "Classic"}
  };

  /**
   * Constructs a new menu bar.
   */
  public MenuBar() {
    super();
    initMenuBar();
    initActionListeners();
  }

  /**
   * Initializes the menu bar.
   */
  private void initMenuBar() {
    for (String[] menu_item : menu_items) {
      JMenu menu = new JMenu(menu_item[0]);
      for (int i = 1; i < menu_item.length; i++) {
        menu.add(new JMenuItem(menu_item[i]));
      }
      this.add(menu);
    }
  }

  /**
   * Returns the menu item at the specified index.
   * @param menu_index The index of the menu.
   * @param item_index The index of the item.
   * @return The menu item at the specified index.
   */
  public JMenuItem getMenuItem(int menu_index, int item_index) {
    return this.getMenu(menu_index).getItem(item_index);
  }

  /**
   * Returns the menu item at the specified name.
   * @param menu_name The name of the menu.
   * @param item_name The name of the item.
   * @return The menu item at the specified name.
   */
  public JMenuItem getMenuItem(String menu_name, String item_name) {
    // Loop through the menus
    for (int i = 0; i < menu_items.length; i++) {
      if (menu_items[i][0].equals(menu_name)) {
        // Loop through the menu items
        for (int j = 1; j < menu_items[i].length; j++) {
          if (menu_items[i][j].equals(item_name)) {
            return this.getMenuItem(i, j - 1);
          }
        }
      }
    }

    return null;
  }

  /**
   * Initializes the action listeners for the menu bar.
   */
  public void initActionListeners() {
    // File > New Set
    getMenuItem("File", "New Set").addActionListener(e -> {
      if (Screen.createSet()) {
        JOptionPane.showMessageDialog(null, "Successfully created card set.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // File > Load Set
    getMenuItem("File", "Load Set").addActionListener(e -> {
      // Load set from file
      if (Screen.loadCardSet()) {
        JOptionPane.showMessageDialog(null, "Card set loaded successfully!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
      } 
    });

    // File > Save Set
    getMenuItem("File", "Save Set").addActionListener(e -> {
      // Save set to file
      if (Screen.saveCardSet()) {
        JOptionPane.showMessageDialog(null, "Card set saved successfully!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // File > Save Set As
    getMenuItem("File", "Save Set As").addActionListener(e -> {
      // Save set to file
      if (Screen.saveAsCardSet()) {
        JOptionPane.showMessageDialog(null, "Card set saved successfully!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
      }
    });

    // File > Exit
    getMenuItem("File", "Exit").addActionListener(e -> {
      System.exit(0);
    });

    // Theme > Light
    getMenuItem("Theme", "Light").addActionListener(e -> {
      // Change LAF to light
      try {
        UIManager.setLookAndFeel(new FlatSolarizedLightIJTheme());
        SwingUtilities.updateComponentTreeUI(this.getParent());

        Color gridColor = Color.decode("#818181");
        Color selectColor = Color.decode("#b3b3b3");
        Color textColor = Color.decode("#000000");
        
        if (EditSet.getTable() != null) {
          EditSet.getTable().setShowGrid(true);
          EditSet.getTable().setGridColor(gridColor);
          EditSet.getTable().setSelectionBackground(selectColor);
          EditSet.getTable().setSelectionForeground(textColor);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });

    // Theme > Dark
    getMenuItem("Theme", "Dark").addActionListener(e -> {
      // Change LAF to dark
      try {
        UIManager.setLookAndFeel(new FlatMaterialDesignDarkIJTheme());
        SwingUtilities.updateComponentTreeUI(this.getParent());

        Color gridColor = Color.decode("#818181");
        Color selectColor = Color.decode("#b3b3b3");
        Color textColor = Color.decode("#000000");
        
        if (EditSet.getTable() != null) {
          EditSet.getTable().setShowGrid(true);
          EditSet.getTable().setGridColor(gridColor);
          EditSet.getTable().setSelectionBackground(selectColor);
          EditSet.getTable().setSelectionForeground(textColor);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });

    // Theme > Classic
    getMenuItem("Theme", "Classic").addActionListener(e -> {
      // Change LAF to classic (Metal)
      try {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
          if ("Metal".equals(info.getName())) {
            UIManager.setLookAndFeel(info.getClassName());
            SwingUtilities.updateComponentTreeUI(this.getParent());
            break;
          }
        }
      } catch (Exception ex) {
        // If Metal is not available, you can set the GUI to another look and feel (Light).
        JOptionPane.showMessageDialog(
          null, "Classic (Metal) theme is not available. Using Flat Light theme instead.", "ERROR", JOptionPane.ERROR_MESSAGE);
        getMenuItem("Theme", "Light").doClick();
      }
    });
  }
}
