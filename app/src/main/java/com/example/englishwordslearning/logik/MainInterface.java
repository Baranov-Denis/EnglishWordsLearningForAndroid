package com.example.englishwordslearning.logik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.example.englishwordslearning.database.WordsDataBase;

import java.util.ArrayList;
import java.util.HashSet;

public class MainInterface {

    private static MainInterface mainInterface;

    private final WordsDataBase wordsDataBase;
    //  private final SQLiteDatabase mySQLiteDatabase;
    private ProcessOfLearning processOfLearning;

    public static MainInterface getMainInterface() {
        return mainInterface;
    }

    public static MainInterface getMainInterface(Context context) {
        if (mainInterface == null) {
            mainInterface = new MainInterface(context);
        }
        return mainInterface;
    }


    /**
     * Приватный конструктор
     * получаем экземпляр базы данных
     * получаем экземпляр главного процесса
     * загружаем ArrayList<WordCard> с библиотекой всех слов в главный процесс
     * @param context
     */
    private MainInterface(Context context) {
        wordsDataBase = WordsDataBase.getWordsDataBase(context);
        processOfLearning = ProcessOfLearning.getProcessOfLearning();
      //  processOfLearning.setAllOfWordsOfDictionary(wordsDataBase.loadDictionaryFromSQLiteDataBase());
    }

    /**
     * Возвращает базу данных
     * используется для получения курсора
     * и построения List View в Create Activity
     * Рекомендуется переработать построение ListView с помощью Array адаптера
     * @return SQLiteDataBase
     */
    public SQLiteDatabase getSQLiteDatabase() {
        return wordsDataBase.getWritableDatabase();
    }

    /**
     * Метод добавляет карточку в базу данных
     * с нулевыми показателями всех полей
     * и перезагружает HashSet содержащий все слова библиотеки
     *
     * @param EnglishWord английское слово
     * @param russianWord русское слово
     */
    public void addNewWord(String EnglishWord, String russianWord) {
        processOfLearning.addNewWord(EnglishWord, russianWord);
    }

    /**
     * Метод удаляет карточку из базы данных
     * и перезагружает HashSet содержащий все слова библиотеки
     * @param targetWord id primary key карточки из таблицы
     */
    public void deleteCurrentWord(long targetWord) {
        processOfLearning.deleteCurrentWord(targetWord);
    }

    public void resetAllProgress(){
        processOfLearning.cleanAllProgress();
    }




    /**
     * ----------------------------------------- Learn Activity ------------------------------------
     */

    public void showWordForLearn(String word , View view){
        processOfLearning.showWord(word,view);
    }

    public void createButtonsForLearning( View view,Context context){
        processOfLearning.createButtons(view,context);
    }

    public  WordCard getWordThatNeedsToBeTranslated() {
        return processOfLearning.getWordThatNeedsToBeTranslated();
    }

}
