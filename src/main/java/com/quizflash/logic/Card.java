package com.quizflash.logic;

/**
 * A class representing a flashcard.
 */
public class Card {
  private String question;
  private String answer;
  private boolean knowsThis;

  public Card(String question, String answer) {
    this.question = question;
    this.answer = answer;

    knowsThis = false;
  }

  public String getQuestion() {
    return question;
  }

  public String getAnswer() {
    return answer;
  }

  public boolean isKnown() {
    return knowsThis;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public void setKnowsThis(boolean knowsThis) {
    this.knowsThis = knowsThis;
  }
}
