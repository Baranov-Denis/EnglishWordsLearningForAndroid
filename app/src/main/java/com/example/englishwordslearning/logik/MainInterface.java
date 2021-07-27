package com.example.englishwordslearning.logik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishwordslearning.database.WordsDataBase;

import java.util.ArrayList;

public class MainInterface {

    private static MainInterface mainInterface;

    private final WordsDataBase wordsDataBase;
    private final SQLiteDatabase mySQLiteDatabase;
    private ProcessOfLearning processOfLearning;

    public static MainInterface getMainInterface(Context context) {
        if (mainInterface == null) {
            mainInterface = new MainInterface(context);
        }
        return mainInterface;
    }


    private MainInterface(Context context) {
        wordsDataBase = new WordsDataBase(context);
        mySQLiteDatabase = WordsDataBase.getDatabase();
        processOfLearning = ProcessOfLearning.getProcessOfLearning();
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mySQLiteDatabase;
    }

    public void addNewWord(String EnglishWord, String russianWord) {
        wordsDataBase.addNewWord(EnglishWord, russianWord);
        processOfLearning.reloadAllOfWordsOfDictionary();
    }

    public void deleteCurrentWord(long targetWord) {
        wordsDataBase.deleteCurrentWord(targetWord);
        processOfLearning.reloadAllOfWordsOfDictionary();
    }


    public ArrayList<WordCard> getAllOfWordsOfDictionary() {
        return processOfLearning.getAllOfWordsOfDictionary();
    }
}
