package com.quizflash.gui;

import javax.swing.JPanel;

import java.awt.CardLayout;

/**
 * The main screen of the application.
 * This is in CardLayout with three option panels (Study Set, Create Set, Load Set) 
 */
public class GUIScreen extends JPanel {
  String[] option_panels = {"Main Menu", "Edit Current Set", "Study Set"};
  
  StudySet study_set;
  EditSet edit_set;
  
  /**
   * Constructs a new GUI screen.
   */
  public GUIScreen() {
    super();
    initScreen();
  }

  /**
   * Initializes the screen.
   */
  private void initScreen() {
    this.setLayout(new CardLayout());
    this.add(new TitleScreen(), option_panels[0]);
    this.add(new EditSet(), option_panels[1]);
    this.add(new StudySet(), option_panels[2]);
  }

  /**
   * Switches to the specified option panel.
   * @param option The option panel to switch to.
   */
  public void switchTo(String option) {
    CardLayout cl = (CardLayout) this.getLayout();
    cl.show(this, option);
  }

  /**
   * Gets the option panel at the specified name.
   * @param option The name of the option panel to get.
   * @return The option panel at the specified name.
   */
  public JPanel getOptionPanel(String option) {
    // Loop through all the option panels
    for (int i = 0; i < option_panels.length; i++) {
      // If the option panel is the one we want
      if (option_panels[i].equals(option)) {
        // Return the option panel
        return (JPanel) this.getComponent(i);
      }
    }  

    // If we didn't find the option panel, return null
    return null;
  }

  /**
   * A method to update the panels after loading a deck.
   */
  public void updatePanels() {
    // Create new instances
    edit_set = new EditSet();
    study_set = new StudySet();

    // Remove the old instances
    this.remove(1);
    this.remove(1);

    // Add the new instances
    this.add(edit_set, option_panels[1]);
    this.add(study_set, option_panels[2]);
  }

  /**
   * Updates the panels then switches to the specified option panel.
   */
  public void updatePanels(String option_panel) {
    updatePanels();
    switchTo(option_panel);
  }

  /**
   * Updates only the study set panel.
   */
  public void updateStudySet() {
    study_set = new StudySet();
    this.remove(2);
    this.add(study_set, option_panels[2]);
  }
}
