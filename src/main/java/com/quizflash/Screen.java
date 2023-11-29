package com.quizflash;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import com.quizflash.gui.GUIScreen;
import com.quizflash.gui.MenuBar;
import com.quizflash.logic.CardSet;
import com.quizflash.logic.SetHandler;

/**
 * The main screen of the application.
 * It has two child screens: {@link com.quizflash.GUIScreen} and {@link com.quizflash.gui.MenuBar}.
 * The menu bar is at the top of the screen, and the GUI screen is below it.
 */
public class Screen extends JFrame {
  JLabel developed_by;
  JPanel developed_by_panel = new JPanel();
  MenuBar menu_bar = new MenuBar();
  ImageIcon icon = new ImageIcon("src/main/resources/icon.png");

  public static Font font = new Font("Arial", 0, 16);
  public static Font medium_font = new Font("Arial", Font.BOLD, 15);
  public static Font small_font = new Font("Arial", 0, 8);

  public static GUIScreen gui_screen = new GUIScreen();    

  /**
   * Constructs a new screen.
   */
  public Screen() {
    super("QuizFlash - Your Digital Flashcard Manager");

    this.setIconImage(icon.getImage());

    setUIFont(new FontUIResource(font));
    changeComponentFont(gui_screen, font);
    
    this.setLayout(new BorderLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    initDevelopedBy();

    this.add(menu_bar, BorderLayout.NORTH);
    this.add(gui_screen, BorderLayout.CENTER);
    this.add(developed_by_panel, BorderLayout.SOUTH);

    this.setSize(1100, 700);

    menu_bar.getMenuItem("Theme", "Light").doClick();

    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Initializes the "Developed by" bar at the bottom of the screen.
   */
  private void initDevelopedBy() {
    Font small_font = new Font("Arial", Font.PLAIN, 9); //adjusted
    developed_by_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    developed_by = new JLabel("Developed by: The OOPa LOOMPAS (Moral, Paglinawan, Quintano, Sandhu)");
    developed_by.setAlignmentX(LEFT_ALIGNMENT);
    developed_by.setFont(small_font);
    developed_by_panel.add(developed_by);
  }

  // util functions

  public static GUIScreen getGUIScreen() {
    return gui_screen;
  }

  public static boolean loadCardSet() {
    JFileChooser file_chooser = new JFileChooser();
    file_chooser.setDialogTitle("Load Card Set");
    file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    file_chooser.setAcceptAllFileFilterUsed(false);

    // Only look for .txt files
    file_chooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
      public boolean accept(java.io.File f) {
        if (f.isDirectory()) {
          return true;
        } else {
          return f.getName().endsWith(".txt");
        }
      }

      public String getDescription() {
        return "Card Set Files (*.txt)";
      }
    });

    if (file_chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      System.out.println("File selected: " + file_chooser.getSelectedFile().getAbsolutePath());
      
      try {
        SetHandler.loadIntoApp(file_chooser.getSelectedFile());
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
          null, "An error occurred in loading the set!\nPlease ensure that the .txt file's content is in the right format.", "ERROR", 
          JOptionPane.ERROR_MESSAGE);
        return false;
      }

      gui_screen.updatePanels();

      return true;
    } else {
      System.out.println("No file selected");
      return false;
    }
  }

  public static boolean saveAsCardSet() {
    JFileChooser file_chooser = new JFileChooser();
    file_chooser.setDialogTitle("Save Card Set");
    file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    file_chooser.setAcceptAllFileFilterUsed(false);

    // Only look for .txt files
    file_chooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
      public boolean accept(java.io.File f) {
        if (f.isDirectory()) {
          return true;
        } else {
          return f.getName().endsWith(".txt");
        }
      }

      public String getDescription() {
        return "Card Set Files (*.txt)";
      }
    });

    if (file_chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      System.out.println("File selected: " + file_chooser.getSelectedFile().getAbsolutePath());
      
      try {
        SetHandler.saveFromApp(file_chooser.getSelectedFile());
        SetHandler.getCurrentSet().resetKnownCards();
        gui_screen.updateStudySet();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
          null, "An error occurred in saving the set!", "ERROR", 
          JOptionPane.ERROR_MESSAGE);
        return false;
      }
    } else {
      System.out.println("No file selected");
      return false;
    }

    return true;
  }

  public static boolean saveCardSet() {
    if (SetHandler.getCurrentSet() == null || SetHandler.getCurrentSet().getPath() == null) {
      return saveAsCardSet();
    }

    try {
      SetHandler.saveFromApp(new File(SetHandler.getCurrentSet().getPath()));
      SetHandler.getCurrentSet().resetKnownCards();
      gui_screen.updateStudySet();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
        null, "An error occurred in saving the set!", "ERROR", 
        JOptionPane.ERROR_MESSAGE);
      return false;
    }

    return true;
  }

  public static void changeComponentFont(Component component, Font font) {
    component.setFont(font);
    if (component instanceof Container) {
      for (Component child : ((Container) component).getComponents()) {
        changeComponentFont(child, font);
      }
    }
  }

  public static void setUIFont(FontUIResource f) {
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
        Object key = keys.nextElement();
        Object value = UIManager.get(key);
        if (value instanceof FontUIResource) {
            FontUIResource orig = (FontUIResource) value;
            Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
            UIManager.put(key, new FontUIResource(font));
        }
    }
  }

  public static boolean createSet() {
    String set_name = JOptionPane.showInputDialog(
        null, "Please enter the name of the set:", "Create Set", JOptionPane.QUESTION_MESSAGE);

    // Check if they pressed cancel
    if (set_name == null) {
      return false;
    }
    
    while (set_name.isEmpty()) {
      set_name = JOptionPane.showInputDialog(
        null, "Please enter a valid nonempty name for the set:", "Create Set", JOptionPane.ERROR_MESSAGE);
    }

    CardSet new_set = new CardSet(set_name);
    new_set.setDescription("Enter set description...");

    SetHandler.setCurrentSet(new_set);

    Screen.getGUIScreen().updatePanels("Edit Current Set");

    return true;
  }
}