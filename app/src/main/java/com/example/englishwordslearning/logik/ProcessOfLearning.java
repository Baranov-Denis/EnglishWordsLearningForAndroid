package com.example.englishwordslearning.logik;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.englishwordslearning.R;
import com.example.englishwordslearning.database.WordsDataBase;

import java.util.ArrayList;

public class ProcessOfLearning {

    private static ProcessOfLearning processOfLearning;
    private ArrayList<WordCard> allOfWordsOfDictionary;

    public ArrayList<WordCard> getAllOfWordsOfDictionary() {
        return allOfWordsOfDictionary;
    }

    private SQLiteDatabase mySQLiteDatabase;

    public static ProcessOfLearning getProcessOfLearning() {
        if (processOfLearning == null) {
            processOfLearning = new ProcessOfLearning();
        }
        return processOfLearning;
    }

    private ProcessOfLearning() {
        mySQLiteDatabase = WordsDataBase.getDatabase();
       // allOfWordsOfDictionary = createDictionaryFromSQLiteDataBase();
    }


    /**
     * Этот метод загружает всю информацию из базы данных и преобразует её в
     * WordCard классы
     * @return - массив карточек типа WordCard со всеми словами из SQLiteDataBase
     */
    private ArrayList<WordCard> createDictionaryFromSQLiteDataBase() {

        ArrayList<WordCard> tempLibrary = new ArrayList<>();
        Cursor wordCursor = mySQLiteDatabase.query("DICTIONARY", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT","WRONG_ANSWER_STAT", "NOW_LEARNING", "IS_LEARNED" }, null, null, null, null, "ENGLISH_WORD");
            while (wordCursor.moveToNext()) {
                tempLibrary.add(new WordCard(wordCursor.getString(1), wordCursor.getString(2), wordCursor.getInt(3), wordCursor.getInt(4), wordCursor.getInt(5), wordCursor.getInt(6)));
            }
            //Проверка массива
        for(WordCard x : tempLibrary) System.out.println(x);
        return tempLibrary;
    }

    public void reloadAllOfWordsOfDictionary(){
        allOfWordsOfDictionary = createDictionaryFromSQLiteDataBase();
    }


}
