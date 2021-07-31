package com.example.englishwordslearning.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.englishwordslearning.CreateActivity;
import com.example.englishwordslearning.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordsDataBase extends SQLiteOpenHelper {

    private static final String DB_NAME = "wordsLearning";
    private static final int DB_VERSION = 5;
    private static SQLiteDatabase database;

    public static SQLiteDatabase getDatabase() {
        return database;
    }

    public WordsDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteOpenHelper liteOpenHelper = this;
        database = liteOpenHelper.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        upgradeMyDataBase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        upgradeMyDataBase(sqLiteDatabase,oldVersion,newVersion);
    }

    private void upgradeMyDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 10) {
            //db.execSQL("DROP TABLE DICTIONARY");
            System.out.println("-------------------------------------------------------------------------------");
            db.execSQL("CREATE TABLE DICTIONARY (_id INTEGER PRIMARY KEY AUTOINCREMENT , ENGLISH_WORD TEXT , RUSSIAN_WORD TEXT, RIGHT_ANSWER_COUNT INTEGER, " +
                    "WRONG_ANSWER_STAT INTEGER, NOW_LEARNING INTEGER, IS_LEARNED INTEGER);");

            insertWord(db, "run", "бег",  0,0,0,0);
            insertWord(db, "wrap", "заворачивать", 0, 0,0,0);
        }
    }


    /**
     * Вставка слов в базу данных
     * /Вставляется английское слово , русское слово , количество сколько раз слово было угадано , и выучено слово или нет 1 или 0
     */
    private void insertWord(SQLiteDatabase db, String englishWord, String russianWord, int rightAnswerCount, int wrongAnswerStat, int nowLearning, int isLearned) {
        if (testExistingWord(db, englishWord, russianWord)) {
            ContentValues wordValue = new ContentValues();
            wordValue.put("ENGLISH_WORD", englishWord);
            wordValue.put("RUSSIAN_WORD", russianWord);
            wordValue.put("RIGHT_ANSWER_COUNT", rightAnswerCount);
            wordValue.put("WRONG_ANSWER_STAT", wrongAnswerStat);
            wordValue.put("NOW_LEARNING", nowLearning);
            wordValue.put("IS_LEARNED", isLearned);
            db.insert("DICTIONARY", null, wordValue);
        }
    }


    /**Проверка существования английского и русского слов
     *
     * @param database
     * @param englishWord
     * @param russianWord
     * @return
     */
    private boolean testExistingWord(SQLiteDatabase database, String englishWord, String russianWord) {

        Cursor englishCursor = database.query("DICTIONARY", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "ENGLISH_WORD = ?", new String[]{englishWord}, null, null, null);
        boolean resultEnglish = englishCursor.getCount() > 0;
        Cursor russianCursor = database.query("DICTIONARY", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "ENGLISH_WORD = ?", new String[]{englishWord}, null, null, null);
        boolean resultRussian = russianCursor.getCount() > 0;
        englishCursor.close();
        russianCursor.close();
        return !resultEnglish && !resultRussian;
    }

    private boolean deleteWord(SQLiteDatabase db, long id) {
        return db.delete("DICTIONARY", "_id = ?", new String[]{"" + id}) > 0;
    }


    /**Проверка на пустую строку и на то, чтобы английское слово было английским , а русское русским
     *
     * @param englishWord
     * @param russianWord
     * @return
     */
    private boolean testGettingWords(String englishWord, String russianWord) {
        Pattern englishPattern = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
        Matcher englishMatcher = englishPattern.matcher(englishWord);

        Pattern russianPattern = Pattern.compile("[а-я]", Pattern.CASE_INSENSITIVE);
        Matcher russianMatcher = russianPattern.matcher(russianWord);

        return englishMatcher.find() && russianMatcher.find();
    }


    /**Метод для добавления слов
     *
     * @param englishWord
     * @param russianWord
     */
    public void addNewWord(String englishWord, String russianWord) {

        if (testGettingWords(englishWord, russianWord)) {
            //Приведение строк к нижнему регистру
            String correctedEnglishWord = englishWord.toLowerCase();
            String correctedRussianWord = russianWord.toLowerCase();
            //Запуск метода вставки в базу данных
            insertWord(database, correctedEnglishWord, correctedRussianWord, 0, 0,0,0);
        }
    }

    /**Метод для удаления выделенного слова по id
     *
     * @param id
     */
    public void deleteCurrentWord(long id) {
        deleteWord(database, id);
    }

}
