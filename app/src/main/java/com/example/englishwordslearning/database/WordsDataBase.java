package com.example.englishwordslearning.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.englishwordslearning.logik.WordCard;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordsDataBase extends SQLiteOpenHelper {

    private static final String DB_NAME = "wordsLearning";
    private static final int DB_VERSION = 5;
    private static SQLiteDatabase database;
    // private ArrayList<WordCard> allOfWordsOfDictionary;
    private static WordsDataBase wordsDataBase;

    //  public ArrayList<WordCard> getAllOfWordsOfDictionary() {
    //      return allOfWordsOfDictionary;
    //  }



    public static WordsDataBase getWordsDataBase() {
        return wordsDataBase;
    }

    public static WordsDataBase getWordsDataBase(Context context) {
        if (wordsDataBase == null) {
            wordsDataBase = new WordsDataBase(context);
        }
        return wordsDataBase;
    }

    private WordsDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteOpenHelper liteOpenHelper = this;
        database = liteOpenHelper.getWritableDatabase();
        // allOfWordsOfDictionary = loadDictionaryFromSQLiteDataBase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        upgradeMyDataBase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        upgradeMyDataBase(sqLiteDatabase, oldVersion, newVersion);
    }

    private void upgradeMyDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 10) {
            //db.execSQL("DROP TABLE DICTIONARY");
            System.out.println("-------------------------------------------------------------------------------");
            db.execSQL("CREATE TABLE DICTIONARY (_id INTEGER PRIMARY KEY AUTOINCREMENT , ENGLISH_WORD TEXT , RUSSIAN_WORD TEXT, RIGHT_ANSWER_COUNT INTEGER, " +
                    "WRONG_ANSWER_STAT INTEGER, NOW_LEARNING INTEGER, IS_LEARNED INTEGER);");

            insertWord(db, "run", "бег");
            insertWord(db, "wrap", "заворачивать");
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
            db.insert("DICTIONARY", null, wordValue);
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
        db.update("DICTIONARY",wordValue,"ENGLISH_WORD = ?",new String[]{englishWord});

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
        Cursor englishCursor = database.query("DICTIONARY", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "ENGLISH_WORD = ?", new String[]{englishWord}, null, null, null);
        boolean resultEnglish = englishCursor.getCount() > 0;
        Cursor russianCursor = database.query("DICTIONARY", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD"}, "ENGLISH_WORD = ?", new String[]{russianWord}, null, null, null);
        boolean resultRussian = russianCursor.getCount() > 0;
        englishCursor.close();
        russianCursor.close();
        return resultEnglish && resultRussian;
    }

    private void deleteWord(SQLiteDatabase db, long id) {
        db.delete("DICTIONARY", "_id = ?", new String[]{"" + id});
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
    public void addNewWord(String englishWord, String russianWord) {

        if (testGettingWords(englishWord, russianWord)) {
            //Приведение строк к нижнему регистру
            String correctedEnglishWord = englishWord.toLowerCase();
            String correctedRussianWord = russianWord.toLowerCase();
            //Запуск метода вставки в базу данных
            insertWord(database, correctedEnglishWord, correctedRussianWord);
        }
        //  allOfWordsOfDictionary = loadDictionaryFromSQLiteDataBase();
    }

    public void changeExistsWord(WordCard wordCard){

        changeWord(database,wordCard.getEnglishWord(), wordCard.getRussianWord(), wordCard.getRightAnswerCount(), wordCard.getWrongAnswerCount(), wordCard.nowLearning(), wordCard.isLearned());
    }

    /**
     * Метод для удаления выделенного слова по id
     *
     * @param id
     */
    public void deleteCurrentWord(long id) {
        deleteWord(database, id);
        //    allOfWordsOfDictionary = loadDictionaryFromSQLiteDataBase();
    }


    /**
     * Этот метод загружает всю информацию из базы данных и преобразует её в
     * WordCard классы
     *
     * @return - HashSet обьектов типа WordCard со всеми словами из SQLiteDataBase
     */
    public ArrayList<WordCard> loadDictionaryFromSQLiteDataBase() {

        ArrayList<WordCard> tempLibrary = new ArrayList<WordCard>();
        Cursor wordCursor = database.query("DICTIONARY", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT", "WRONG_ANSWER_STAT", "NOW_LEARNING", "IS_LEARNED"}, null, null, null, null, "ENGLISH_WORD");
        while (wordCursor.moveToNext()) {
            tempLibrary.add(new WordCard(wordCursor.getString(1), wordCursor.getString(2), wordCursor.getInt(3), wordCursor.getInt(4), wordCursor.getInt(5), wordCursor.getInt(6)));
        }
        System.out.println("load base +++++++++++++++"+tempLibrary);
        return tempLibrary;
    }

}
