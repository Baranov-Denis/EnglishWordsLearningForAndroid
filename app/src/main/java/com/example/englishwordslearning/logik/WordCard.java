package com.example.englishwordslearning.logik;

public class WordCard {
    private final String englishWord;
    private final String russianWord;
    private final int rightAnswerCount;
    private final int wrongAnswerCount;
    private final int nowLearning;
    private final int isLearned;

    public String getEnglishWord() {
        return englishWord;
    }

    public String getRussianWord() {
        return russianWord;
    }

    public int getRightAnswerCount() {
        return rightAnswerCount;
    }

    public int isLearned() {
        return isLearned;
    }

    public WordCard(String englishWord, String russianWord, int rightAnswerCount, int wrongAnswerCount, int nowLearning, int isLearned) {
        this.englishWord = englishWord;
        this.russianWord = russianWord;
        this.rightAnswerCount = rightAnswerCount;
        this.wrongAnswerCount = wrongAnswerCount;
        this.nowLearning = nowLearning;
        this.isLearned = isLearned;
    }

    @Override
    public String toString() {
        return "WordCard{" +
                "englishWord='" + englishWord + '\'' +
                ", russianWord='" + russianWord + '\'' +
                ", rightAnswerCount=" + rightAnswerCount +
                ", wrongAnswerCount=" + wrongAnswerCount +
                ", nowLearning=" + nowLearning +
                ", isLearned=" + isLearned +
                '}';
    }
}
