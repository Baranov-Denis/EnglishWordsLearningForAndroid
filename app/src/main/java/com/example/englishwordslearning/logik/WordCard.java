package com.example.englishwordslearning.logik;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class WordCard {
    //английское слово
    private String englishWord;
    //русское слово
    private String russianWord;
    //количество сколько раз слово правильно угадано
    //сбрасывается на ноль при каждом неправильном ответе
    private int rightAnswerCount;
    //количество сколько раз слово было не угадано
    //прибавляется на один каждый раз при ошибке
    private int wrongAnswerCount;
    //проверка находится ли слово в списке изучаемых слов
    //если 0 - то значит нет
    //прибавляется 1 при занесении слова в список изучаемых слов
    //устанавливаем 0 после окончания изучения и удаления слова из списка изучаемых слов
    private int nowLearning;
    //проверка выучено слово или нет
    //0 - слово не выучено
    //1 или более слово выучено
    //прибавляется 1 после окончания изучения и удаления слова из списка изучаемых слов
    //выученные слова подмешиваются в список изучаемых слов ?????????????????????????????????
    private int isLearned;

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public void setRussianWord(String russianWord) {
        this.russianWord = russianWord;
    }

    public void setRightAnswerCount(int rightAnswerCount) {
        this.rightAnswerCount = rightAnswerCount;
    }

    public void setWrongAnswerCount(int wrongAnswerCount) {
        this.wrongAnswerCount = wrongAnswerCount;
    }

    public void setNowLearning(int nowLearning) {
        this.nowLearning = nowLearning;
    }

    public void setIsLearned(int isLearned) {
        this.isLearned = isLearned;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getRussianWord() {
        return russianWord;
    }

    public int getRightAnswerCount() {
        return rightAnswerCount;
    }

    public int getWrongAnswerCount() {
        return wrongAnswerCount;
    }

    public int nowLearning() {
        return nowLearning;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordCard wordCard = (WordCard) o;
        return englishWord.equals(wordCard.englishWord) &&
                russianWord.equals(wordCard.russianWord);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(englishWord, russianWord);
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
