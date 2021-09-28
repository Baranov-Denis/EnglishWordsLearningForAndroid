package com.example.englishwordslearning.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.englishwordslearning.logik.WordCard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordsDataBaseHelper extends SQLiteOpenHelper {


    private final static String TAG = "--->>>   ";
    private static final int DB_VERSION = 1;

    private final Context mContext;

    private static final String DB_NAME = "MAIN_DATABASE";

    private final static String TABLE_NAME = "MAIN_TABLE_DATABASE";
    private static SQLiteDatabase mainDataBase;

    private static WordsDataBaseHelper wordsDataBaseHelper;


    public static String getTableName() {
        return TABLE_NAME;
    }

    public static SQLiteDatabase getMainDataBase() {
        return mainDataBase;
    }

    public static WordsDataBaseHelper getWordsDataBase() {
        return wordsDataBaseHelper;
    }

    public static WordsDataBaseHelper getWordsDataBase(Context context) {
        if (wordsDataBaseHelper == null) {
            wordsDataBaseHelper = new WordsDataBaseHelper(context);
        }
        return wordsDataBaseHelper;
    }

    private WordsDataBaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

        this.mContext = context;

        WordsDataBaseHelper wordsDataBaseHelper = this;
        mainDataBase = wordsDataBaseHelper.getReadableDatabase();

        this.getReadableDatabase();
        Log.i(TAG, mainDataBase.toString());

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // upgradeMyDataBase(db, 0, DB_VERSION);

        db.execSQL("CREATE TABLE MAIN_TABLE_DATABASE (_id INTEGER PRIMARY KEY AUTOINCREMENT , ENGLISH_WORD TEXT , RUSSIAN_WORD TEXT, RIGHT_ANSWER_COUNT INTEGER, " +
                "WRONG_ANSWER_STAT INTEGER, NOW_LEARNING INTEGER, IS_LEARNED INTEGER);");

        insertExternalDatabase(db);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        insertExternalDatabase(db);
    }

    private void insertExternalDatabase(SQLiteDatabase db){
        String englishWord;
        String russianWord;
        ExternalDatabaseHelper externalDatabaseHelper = new ExternalDatabaseHelper(mContext);

        SQLiteDatabase externalDatabase = externalDatabaseHelper.getExternalDatabase();

        Cursor wordCursor = externalDatabase.query(ExternalDatabaseHelper.getTableName(), new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT", "WRONG_ANSWER_STAT", "NOW_LEARNING", "IS_LEARNED"}, null, null, null, null, "ENGLISH_WORD");
        while (wordCursor.moveToNext()) {
            englishWord = wordCursor.getString(1).toLowerCase();
            russianWord = wordCursor.getString(2).toLowerCase();
            insertWord(db, englishWord, russianWord);
        }
        wordCursor.close();
    }


    private void upgradeMyDataBase(SQLiteDatabase db, int i, int dbVersion) {
        if (dbVersion == 1) {
            db.execSQL("CREATE TABLE TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT , ENGLISH_WORD TEXT , RUSSIAN_WORD TEXT, RIGHT_ANSWER_COUNT INTEGER, " +
                    "WRONG_ANSWER_STAT INTEGER, NOW_LEARNING INTEGER, IS_LEARNED INTEGER);");
        }

    }


    /**
     * Вставка слов в базу данных
     * /Вставляется английское слово , русское слово , количество сколько раз слово было угадано , и выучено слово или нет 1 или 0
     */
    private void insertWord(SQLiteDatabase db, String englishWord, String russianWord) {
        if (!testExistingWord(db, englishWord, russianWord)) {
            ContentValues wordValue = new ContentValues();
            wordValue.put("ENGLISH_WORD", englishWord);
            wordValue.put("RUSSIAN_WORD", russianWord);
            wordValue.put("RIGHT_ANSWER_COUNT", 0);
            wordValue.put("WRONG_ANSWER_STAT", 0);
            wordValue.put("NOW_LEARNING", 0);
            wordValue.put("IS_LEARNED", 0);
            db.insert(TABLE_NAME, null, wordValue);
        }
    }

    private void insertWord(SQLiteDatabase db, String tableName, String englishWord, String russianWord, int rightAnswer, int wrongAnswer, int nowLearning, int isLearned) {
        if (!testExistingWord(db, englishWord, russianWord)) {
            ContentValues wordValue = new ContentValues();
            wordValue.put("ENGLISH_WORD", englishWord);
            wordValue.put("RUSSIAN_WORD", russianWord);
            wordValue.put("RIGHT_ANSWER_COUNT", rightAnswer);
            wordValue.put("WRONG_ANSWER_STAT", wrongAnswer);
            wordValue.put("NOW_LEARNING", nowLearning);
            wordValue.put("IS_LEARNED", isLearned);
            db.insert(tableName, null, wordValue);
        }
    }

    private void changeWord(SQLiteDatabase db, String englishWord, String russianWord, int rightAnswerCount, int wrongAnswerStat, int nowLearning, int isLearned) {
        ContentValues wordValue = new ContentValues();
        wordValue.put("ENGLISH_WORD", englishWord);
        wordValue.put("RUSSIAN_WORD", russianWord);
        wordValue.put("RIGHT_ANSWER_COUNT", rightAnswerCount);
        wordValue.put("WRONG_ANSWER_STAT", wrongAnswerStat);
        wordValue.put("NOW_LEARNING", nowLearning);
        wordValue.put("IS_LEARNED", isLearned);
        db.update(TABLE_NAME, wordValue, "ENGLISH_WORD = ?", new String[]{englishWord});
    }

    private void insertWord(SQLiteDatabase db, WordCard wordCard) {
        if (!testExistingWord(db, wordCard.getEnglishWord(), wordCard.getRussianWord())) {
            ContentValues wordValue = new ContentValues();
            wordValue.put("ENGLISH_WORD", wordCard.getEnglishWord());
            wordValue.put("RUSSIAN_WORD", wordCard.getRussianWord());
            wordValue.put("RIGHT_ANSWER_COUNT", wordCard.getRightAnswerCount());
            wordValue.put("WRONG_ANSWER_STAT", wordCard.getWrongAnswerCount());
            wordValue.put("NOW_LEARNING", wordCard.nowLearning());
            wordValue.put("IS_LEARNED", wordCard.isLearned());
            db.insert(TABLE_NAME, null, wordValue);
        }
    }


    /**
     * Проверка существования английского и русского слов
     * в базе данных
     *
     * @param database    база данных
     * @param englishWord англ слово
     * @param russianWord русское слово
     * @return возвращает false если нет ни русского ни английского слова
     * если в базе данных есть или русское или англ слово, то возвращает true
     * нельзя добавить одинаковые слова тоесть run - бег и run - бежать добавить не получится
     * Возможно придётся изменить это??????????????????????????
     */
    private boolean testExistingWord(SQLiteDatabase database, String englishWord, String russianWord) {
        boolean resultEnglish = false;
        boolean resultRussian = false;
        Cursor englishCursor = database.query(TABLE_NAME, new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "ENGLISH_WORD = ?", new String[]{englishWord}, null, null, null);
        if (englishCursor.moveToNext()) {
            resultEnglish = englishCursor.getCount() > 0;
        }

        Cursor russianCursor = database.query(TABLE_NAME, new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "RUSSIAN_WORD = ?", new String[]{russianWord}, null, null, null);
        if (russianCursor.moveToNext()) {
            resultRussian = russianCursor.getCount() > 0;
        }
        englishCursor.close();
        russianCursor.close();

        return resultEnglish && resultRussian;
    }

    private void deleteWord(SQLiteDatabase db, long id) {
        db.delete(TABLE_NAME, "_id = ?", new String[]{"" + id});
    }


    /**
     * Проверка на пустую строку и на то, чтобы английское слово было английским , а русское русским
     *
     * @param englishWord англ слово
     * @param russianWord русс слово
     * @return если слова соответствуют всем требованиям, то возвращает true
     */
    private boolean testGettingWords(String englishWord, String russianWord) {
        Pattern englishPattern = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE);
        Matcher englishMatcher = englishPattern.matcher(englishWord);

        Pattern russianPattern = Pattern.compile("[а-я]", Pattern.CASE_INSENSITIVE);
        Matcher russianMatcher = russianPattern.matcher(russianWord);

        return englishMatcher.find() && russianMatcher.find();
    }


    /**
     *
     * ----------------------------------------------- PUBLIC METHODS -----------------------------------------------------------------
     *
     */

    /**
     * Метод для добавления слов
     *
     * @param englishWord
     * @param russianWord
     */
    public void addNewWord(String englishWord, String russianWord, View view) {

        if (testGettingWords(englishWord, russianWord)) {
            //Приведение строк к нижнему регистру
            String correctedEnglishWord = englishWord.toLowerCase();
            String correctedRussianWord = russianWord.toLowerCase();
            //Запуск метода вставки в базу данных
            insertWord(mainDataBase, correctedEnglishWord, correctedRussianWord);
            //  userDataBaseHelper.insertWord(userDataBase, correctedEnglishWord, correctedRussianWord);


            Toast toast = Toast.makeText(view.getContext(), "Word added", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(view.getContext(), "Word did not added!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void changeExistsWord(WordCard wordCard) {
        changeWord(mainDataBase, wordCard.getEnglishWord(), wordCard.getRussianWord(), wordCard.getRightAnswerCount(), wordCard.getWrongAnswerCount(), wordCard.nowLearning(), wordCard.isLearned());
    }

    /**
     * Метод для удаления выделенного слова по id
     *
     * @param id
     */
    public void deleteCurrentWord(long id) {
        String deletingWord = "";
        Cursor cursor = mainDataBase.query(TABLE_NAME, new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "_id = ?", new String[]{Long.toString(id)}, null, null, null);
        if (cursor.moveToNext()) {
            deletingWord = cursor.getString(1);
        }
        cursor.close();
        deleteWord(mainDataBase, id);
        //   userDataBaseHelper.deleteCurrentWord(deletingWord);
    }


    /**
     * Этот метод загружает всю информацию из базы данных и преобразует её в
     * WordCard классы
     *
     * @return - HashSet обьектов типа WordCard со всеми словами из SQLiteDataBase
     */
    public ArrayList<WordCard> loadDictionaryFromSQLiteDataBase() {

        ArrayList<WordCard> tempLibrary = new ArrayList<WordCard>();
        Cursor wordCursor = mainDataBase.query(TABLE_NAME, new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT", "WRONG_ANSWER_STAT", "NOW_LEARNING", "IS_LEARNED"}, null, null, null, null, "ENGLISH_WORD");
        while (wordCursor.moveToNext()) {
            tempLibrary.add(new WordCard(wordCursor.getString(1), wordCursor.getString(2), wordCursor.getInt(3), wordCursor.getInt(4), wordCursor.getInt(5), wordCursor.getInt(6)));
        }
        wordCursor.close();

        return tempLibrary;


    }

}
