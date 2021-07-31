package com.example.englishwordslearning.logik;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishwordslearning.R;
import com.example.englishwordslearning.database.WordsDataBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ProcessOfLearning {

    private static ProcessOfLearning processOfLearning;
    private WordsDataBase wordsDataBase;

    /**
     * ArrayList содержащий все слова из библиотеки
     */
    private ArrayList<WordCard> allOfWordsOfDictionary;

    private HashSet<WordCard> learningWordsForButtons;


    private WordCard wordThatNeedsToBeTranslated;


    int countOfButtons = 10;


    public WordCard getWordThatNeedsToBeTranslated() {
        return wordThatNeedsToBeTranslated;
    }


    public static ProcessOfLearning getProcessOfLearning() {
        if (processOfLearning == null) {
            processOfLearning = new ProcessOfLearning();
        }
        return processOfLearning;
    }

    private ProcessOfLearning() {
        wordsDataBase = WordsDataBase.getWordsDataBase();
    }

    public void setAllOfWordsOfDictionary(ArrayList<WordCard> allOfWordsOfDictionary) {
        this.allOfWordsOfDictionary = allOfWordsOfDictionary;
    }

    /**
     * ----------------------------- Create Activity -----------------------------------------------
     */

    public void loadDictionaryFromSQLiteDataBase(){
        allOfWordsOfDictionary = wordsDataBase.loadDictionaryFromSQLiteDataBase();
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
        wordsDataBase.addNewWord(EnglishWord, russianWord);
        loadDictionaryFromSQLiteDataBase();
    }

    /**
     * Метод удаляет карточку из базы данных
     * и перезагружает HashSet содержащий все слова библиотеки
     * @param targetWord id primary key карточки из таблицы
     */
    public void deleteCurrentWord(long targetWord) {
        wordsDataBase.deleteCurrentWord(targetWord);
        loadDictionaryFromSQLiteDataBase();
    }



    /**
     * ----------------------------- Learn Activity ------------------------------------------------
     */


    /**
     * @param word - слово которое нужно перевести
     */
    public void showWord(String word, View view) {
        TextView targetWord = view.findViewById(R.id.target_word);
        targetWord.setText(word);
    }

    /**
     * В настоящее время на кнопки попадают первые 10 слов из словаря.
     * Необходимо передавать в метод массив изучаемых слов для отображения их на кнопках и дальнейшей работы с ними
     */
    public void createButtons(View view, Context context) {

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.button_layout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.setMargins(15, 15, 15, 0); // left, top, right, bottom
        ll.removeAllViews();


        //TODO заменить все слова на избранные
        ArrayList<WordCard> TEMPPPPlearningWordsForButtons = allOfWordsOfDictionary;

        learningWordsForButtons = getHashSetFromArrayListForCreateButtons(TEMPPPPlearningWordsForButtons, countOfButtons);

        getWordForLearn(learningWordsForButtons);

        Iterator<WordCard> iterator = learningWordsForButtons.iterator();


        for (int i = 0; i < countOfButtons; i++) {
            Button myButton = new Button(context);
            WordCard tempWordCardForButton = iterator.next();
            myButton.setText(tempWordCardForButton.getEnglishWord());
            myButton.setOnClickListener(view2 -> {
                //передаём WordCard который принадлежит нажатой кнопке для проверки
                onClickButton(tempWordCardForButton, countOfButtons, view, context);
            });
            myButton.setLayoutParams(layoutParams);
            ll.addView(myButton);
        }
    }


    /**
     * Создание HashSet с карточками по количеству кнопок
     *
     * @param wordCards      ArrayList с изучаемыми в настоящее время карточками
     * @param countOfButtons количество кнопок
     * @return HashSet c карточками - карточек столько сколько кнопок, hashSet создаёт случайный порядок
     * карточек, и не нужна проверка на дубликаты
     */
    private HashSet<WordCard> getHashSetFromArrayListForCreateButtons(ArrayList<WordCard> wordCards, int countOfButtons) {
        HashSet<WordCard> learningWordsForButtons = new HashSet<>();
        while (learningWordsForButtons.size() < countOfButtons) {
            learningWordsForButtons.add(wordCards.get((int) (Math.random() * wordCards.size())));
        }
        return learningWordsForButtons;
    }


    /**
     * @param wordCard получает WordsCard с кнопки для проверки ответа
     *                 Тут будет проверка ответа
     */
    public void onClickButton(WordCard wordCard, int countOfWordButtons, View view, Context context) {

        Toast toast;
        if (wordCard.getRussianWord().equals(wordThatNeedsToBeTranslated.getRussianWord())) {
            toast = Toast.makeText(context, "E-e-e-e-e!", Toast.LENGTH_SHORT);
            createButtons(view, context);
        } else {
            toast = Toast.makeText(context, "Lo-o-o-o-se!", Toast.LENGTH_SHORT);
        }
        toast.show();


        showWord(wordThatNeedsToBeTranslated.getRussianWord(), view);
    }


    public WordCard getWordForLearn(HashSet<WordCard> learningWordsForButtons) {
        int random = (int) (Math.random() * learningWordsForButtons.size());
        int count = 0;
        for (WordCard wordCard : learningWordsForButtons) {
            if (count == random) {
                wordThatNeedsToBeTranslated = wordCard;
                return wordCard;
            }
            count++;
        }
        return null;
    }



   /* public HashSet<WordCard> getAllOfWordsOfDictionary() {
        return allOfWordsOfDictionary;
    }*/


//    public void reloadAllOfWordsOfDictionary(){
//        allOfWordsOfDictionary = mainInterface.getAllOfWordsOfDictionary();
//    }


}
