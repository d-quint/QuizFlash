package com.quizflash.logic;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class representing a set of cards.
 */
public class CardSet implements Iterable<Card> {
  private String name;
  private String description;
  private String path;
  
  private ArrayList<Card> cards;

  public CardSet(String name) {
    this.name = name;
    cards = new ArrayList<Card>();
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getPath() {
    return path;
  }

  public String setName(String name) {
    return this.name = name;
  }

  public String setDescription(String description) {
    return this.description = description;
  }

  public String setPath(String path) {
    return this.path = path;
  }

  public void addCard(Card card) {
    System.out.println("Adding card: " + card.getQuestion() + " " + card.getAnswer());
    cards.add(card);
  }

  public void editCard(int index, Card card) {
    System.out.println("Editing card: " + cards.get(index).getQuestion() + " " + cards.get(index).getAnswer());
    cards.set(index, card);
    System.out.println("Card edited to: " + card.getQuestion() + " " + card.getAnswer());
  }

  public Card getCard(int index) {
    if (index < 0 || index >= cards.size()) {
      return null;
    }

    return cards.get(index);
  }

  public int getRandomCardIndex() {
    int index = (int) (Math.random() * cards.size());
    return index;
  }

  public boolean isEmpty() {
    return cards.isEmpty();
  }

  public int getCardCount() {
    return cards.size();
  }

  public int getKnownCardCount() {
    int count = 0;
    for (Card card : cards) {
      if (card.isKnown()) {
        count++;
      }
    }
    return count;
  }

  public int getUnknownCardCount() {
    int known_count = getKnownCardCount();
    return cards.size() - known_count;
  }

  public void resetKnownCards() {
    for (Card card : cards) {
      card.setKnowsThis(false);
    }
  }

  public String[][] getData() {
    String[][] data = new String[cards.size()][2];
    for (int i = 0; i < cards.size(); i++) {
        data[i][0] = cards.get(i).getQuestion();
        data[i][1] = cards.get(i).getAnswer();
    }
    return data;
  }

  public void removeCard(int index) {
    cards.remove(index);
  }

  public void clearAllCards() {
    cards.clear();
  }

  @Override
  public Iterator<Card> iterator() {
    return cards.iterator();
  }
}