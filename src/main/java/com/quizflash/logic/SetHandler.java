package com.quizflash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * A class that handles manipulation of the current CardSet
 */
public class SetHandler {
  private static CardSet currentSet;
  private static int currentCardIndex;
  private static boolean isRandomized;

  /**
   * Sets the current CardSet
   * @param set The CardSet to set
   */
  public static void setCurrentSet(CardSet set) {
    currentSet = set;
    currentCardIndex = 0;
    isRandomized = false;
  }

  /**
   * Sets the current Card index
   * @param i The index to set
   */
  public static void setCurrentCardIndex(int i) {
    currentCardIndex = i;
  }

  /**
   * Toggles the randomization of the current CardSet
   */
  public static void toggleRandomization() {
    isRandomized = !isRandomized;
  }

  /**
   * Checks if the current CardSet is randomized
   * @return True if the current CardSet is randomized, false otherwise
   */
  public static boolean isRandomized() {
    return isRandomized;
  }

  /**
   * Gets the current CardSet
   * @return The current CardSet
   */
  public static CardSet getCurrentSet() {
    return currentSet;
  }

  /**
   * Gets the current Card
   * @return The current Card
   */
  public static Card getCurrentCard() {
    return currentSet.getCard(currentCardIndex);
  }

  /**
   * Gets the current Card index
   * @return The current Card index
   */
  public static int getCurrentCardIndex() {
    return currentCardIndex;
  }

  /**
   * Checks if the current CardSet is empty or uninitialized
   * @return True if the current CardSet is empty or uninitialized, false otherwise
   */
  public static boolean isEmpty() {
    if (currentSet == null) {
      return true;
    }

    return currentSet.isEmpty();
  }

  /**
   * Loads a CardSet from a file into the application
   * @param selectedSetFile The file to load the CardSet from
   * @throws Exception If the file is not found or if there is an error reading the file
   */
  public static void loadIntoApp(File selectedSetFile) throws Exception {
    // Read file and load content into application
    FileReader fileReader = new FileReader(selectedSetFile.getAbsolutePath());
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    String setName = bufferedReader.readLine();
    CardSet cardSet = new CardSet(setName);

    String setDesc = bufferedReader.readLine();
    cardSet.setDescription(setDesc);
    cardSet.setPath(selectedSetFile.getAbsolutePath());

    String line;
    while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
      String[] cardData = line.split(" \\| ");
      Card card = new Card(cardData[0], cardData[1]);
      cardSet.addCard(card);
    }

    bufferedReader.close();

    currentSet = cardSet;
    currentCardIndex = 0;
    isRandomized = false;

    System.out.println("Loaded set: " + currentSet.getName() + " with " + currentSet.getCardCount() + " cards");
  }

  public static void saveFromApp(File selectedFile) throws Exception {
    // Write current set data to the selected file
    FileWriter fileWriter = new FileWriter(selectedFile.getAbsolutePath());
    fileWriter.write(SetHandler.getCurrentSet().getName() + "\n" + SetHandler.getCurrentSet().getDescription() + "\n");
    
    for (Card card : getCurrentSet()) {
      fileWriter.write(card.getQuestion() + " | " + card.getAnswer() + "\n");
    }

    currentSet.setPath(selectedFile.getAbsolutePath());

    fileWriter.close();
  }

  /**
   * Gets the next unknown Card in the current CardSet and sets it as the current Card
   * @return The next Card in the current CardSet
   */
  public static Card getNextCard() {
    if (currentSet.isEmpty()) {
      return null;
    }

    if (isRandomized()) {
      return getNextRandomCard();
    }

    if (currentCardIndex + 1 == currentSet.getCardCount()) {
      currentCardIndex = 0;
    } else {
      currentCardIndex++;
    }

    while (currentSet.getCard(currentCardIndex).isKnown()) {
      currentCardIndex++;
      
      if (currentCardIndex >= currentSet.getCardCount()) {
        currentCardIndex = 0;
      }
    }

    return currentSet.getCard(currentCardIndex);
  }

  private static Card getNextRandomCard() {
    int new_index = currentSet.getRandomCardIndex();
    Card card = currentSet.getCard(new_index);

    while (card.isKnown() || new_index == currentCardIndex) {
      new_index = currentSet.getRandomCardIndex();
      card = currentSet.getCard(new_index);
    }

    currentCardIndex = new_index;

    return card;
  }
}
