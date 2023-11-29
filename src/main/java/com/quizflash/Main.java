package com.quizflash;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme;

/**
 * Application entry point.
 */
public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
    try {
      UIManager.setLookAndFeel(new FlatMaterialDesignDarkIJTheme());
    } catch (Exception e) {
      System.err.println("Failed to initialize LaF, using default.");
    }

      new Screen();
    });
  }
}