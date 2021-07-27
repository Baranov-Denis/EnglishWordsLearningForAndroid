package com.example.englishwordslearning.logik;

public class WordCard {
    private final String englishWord;
    private final String russianWord;
    private final int answerCount;
    private final int isLearned;

    public String getEnglishWord() {
        return englishWord;
    }

    public String getRussianWord() {
        return russianWord;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public int isLearned() {
        return isLearned;
    }

    public WordCard(String englishWord, String russianWord, int answerCount, int isLearned) {
        this.englishWord = englishWord;
        this.russianWord = russianWord;
        this.answerCount = answerCount;
        this.isLearned = isLearned;
    }

    @Override
    public String toString() {
        return "WordCard{" +
                "englishWord='" + englishWord + '\'' +
                ", russianWord='" + russianWord + '\'' +
                ", answerCount=" + answerCount +
                ", isLearned=" + isLearned +
                '}';
    }
}
