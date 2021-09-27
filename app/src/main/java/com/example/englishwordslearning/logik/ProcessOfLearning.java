package com.example.englishwordslearning.logik;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishwordslearning.R;
import com.example.englishwordslearning.database.ExternalDatabaseHelper;
import com.example.englishwordslearning.database.UserDataBaseHelper;
import com.example.englishwordslearning.database.WordsDataBaseHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class ProcessOfLearning {

    private static ProcessOfLearning processOfLearning;
    private WordsDataBaseHelper wordsDataBaseHelper;
    private static final String TAG = " ->> learning";
    private UserDataBaseHelper userDataBaseHelper;

    private Context mainContext;

    /**
     * ArrayList содержащий все слова из библиотеки
     */
    private ArrayList<WordCard> allOfWordsOfDictionary;

    /**
     * ArrayList в котором содержатся слова, которые изучаются в настоящее время
     * количество слов в этом списке определяется переменной countOfCurrentLearningWords
     * из этого списка выбираются слова для кнопок
     */
    private ArrayList<WordCard> currentLearningWords;

    /**
     * HashSet содержащий изучаемые слова выбранные для кнопок
     */
    private ArrayList<WordCard> learningWordsForButtons;


    /**
     * Переменная для отслеживания правильного ответа
     * используется при создании кнопок
     * в случае когда дан неправильный ответ answeredTrue
     * будет присвоено значение false после чего
     * при создании кнопок не будут обновлены надписи на них
     * а вместо этого кнопки изменят цвет
     */
    private boolean answeredTrue = true;


    /**
     * Количество слов в списке изучаемых слов
     */
    private int countOfCurrentLearnWords = 10;

    public int getCountOfCurrentLearnWords() {
        return countOfCurrentLearnWords;
    }

    public void setCountOfCurrentLearnWords(int countOfCurrentLearnWords) {
        this.countOfCurrentLearnWords = countOfCurrentLearnWords;
    }

    /**
     * количество необходимых правильных ответов для того чтобы слово считалось выученным
     */
    private int countOfRepeatWord = 19;


    /**
     * Слово которое показывается вверху страницы которое нужно перевести
     */
    private WordCard wordThatNeedsToBeTranslated;


    /**
     * количество кнопок с вариантами слов
     */
    int countOfButtons = 10;


    public void setCountOfRepeatWord(int countOfRepeatWord) {
        if(countOfRepeatWord > this.countOfRepeatWord){
            reWriteWordDataBaseAfterChangingRepeat(countOfRepeatWord);
        }
        this.countOfRepeatWord = countOfRepeatWord;
    }


    /**
     * Данный метод служит для того, чтобы при увеличении количества повторений слова,
     * все выученые слова, которые были повторены меньше нового значения будут перезаписаны как невыученые
     * @param newCount новое значение количества повторений слова
     */
    private void reWriteWordDataBaseAfterChangingRepeat(int newCount) {
        for(WordCard wordCard : allOfWordsOfDictionary){
            if(wordCard.getRightAnswerCount() < newCount){
                if(wordCard.isLearned() > 0){
                    wordCard.setIsLearned(0);
                    wordsDataBaseHelper.changeExistsWord(wordCard);
                }
            }
        }
    }

    public int getCountOfRepeatWord() {
        return countOfRepeatWord;
    }

    public WordCard getWordThatNeedsToBeTranslated() {
        return wordThatNeedsToBeTranslated;
    }


    public static ProcessOfLearning getProcessOfLearning(Context context) {

        if (processOfLearning == null) {
            processOfLearning = new ProcessOfLearning(context);
        }

/**
 *
 *
 *
 *
 *
 *
 *
 */
        ExternalDatabaseHelper externalDatabaseHelper = new ExternalDatabaseHelper(context);
        SQLiteDatabase externalDatabase = externalDatabaseHelper.getExternalDatabase();
        Cursor wordCursor = externalDatabase.query("words", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT", "WRONG_ANSWER_STAT", "NOW_LEARNING", "IS_LEARNED"}, null, null, null, null, "ENGLISH_WORD");
        while (wordCursor.moveToNext()) {
            Log.i(TAG, "  0 0 0 0 0 0   " +wordCursor.getString(1));
        }
        wordCursor.close();
/**
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

        return processOfLearning;
    }

    private ProcessOfLearning(Context context) {
        this.mainContext = context;
        wordsDataBaseHelper = WordsDataBaseHelper.getWordsDataBase();
        userDataBaseHelper = UserDataBaseHelper.getUserDataBaseHelper();
        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();

    }

    public void setAllOfWordsOfDictionary(ArrayList<WordCard> allOfWordsOfDictionary) {
        this.allOfWordsOfDictionary = allOfWordsOfDictionary;
    }


    /**
     * Данный метод заполняет карточками основной массив содержащий все слова
     */
    public void loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary() {

        allOfWordsOfDictionary = wordsDataBaseHelper.loadDictionaryFromSQLiteDataBase();

    }

    /**
     * ----------------------------- Create Activity -----------------------------------------------
     */


    /**
     * Метод добавляет карточку в базу данных
     * с нулевыми показателями всех полей
     * и перезагружает ArrayList содержащий все слова библиотеки
     * <p>
     * Используется для начальной загрузки слов
     *
     * @param EnglishWord английское слово
     * @param russianWord русское слово
     */
    public void addNewWord(String EnglishWord, String russianWord, View view) {
        wordsDataBaseHelper.addNewWord(EnglishWord, russianWord, view);
        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();
    }

    /**
     * Метод удаляет карточку из базы данных
     * и перезагружает HashSet содержащий все слова библиотеки
     *
     * @param targetWord id primary key карточки из таблицы
     */
    public void deleteCurrentWord(long targetWord) {
        wordsDataBaseHelper.deleteCurrentWord(targetWord);
        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();
    }


    /**
     * Метод сбрасывает весь прогресс переписывает количество правильных и неправильных ответов
     * устанавливает все поля равными нулю
     * <p>
     * Присваивает словарю изучаемых слов currentLearningWords значение null
     */
    public void cleanAllProgress() {
        for (WordCard wordCard : allOfWordsOfDictionary) {
            wordCard.setRightAnswerCount(0);
            wordCard.setWrongAnswerCount(0);
            wordCard.setNowLearning(0);
            wordCard.setIsLearned(0);
            wordsDataBaseHelper.changeExistsWord(wordCard);
        }
        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();
        //Очищает словарь изучаемых слов. Иначе при изучении будут загружены не сброшенные данные.
        currentLearningWords = null;
    }


    /**
     * ----------------------------- Learn Activity ------------------------------------------------
     */


    private ArrayList<WordCard> repeatCreatingArray(ArrayList<WordCard> tempArrayList) {
        //Проверка существования словаря изучаемых слов
        //Если словарь изучаемых слов уже существует то он будет скопирован
        //во временный словарь
        // Иначе будет запущено создание нового словаря
        if (currentLearningWords != null) {
            tempArrayList = new ArrayList<>(currentLearningWords);
        } else {
            tempArrayList = createLearningListFromMainDictionary();
            answeredTrue = true;
        }
        return tempArrayList;
    }


    /**
     * Создаёт список изучаемых слов
     *
     * @return ArrayList со списком изучаемых слов помеченных как nowLearning > 0
     */
    private ArrayList<WordCard> createCurrentLearningWordsArrayList(Context context) {

        for(WordCard k : allOfWordsOfDictionary)Log.i("----------->> " , k.getEnglishWord() +" " + allOfWordsOfDictionary.size());

        ArrayList<WordCard> tempArrayList = new ArrayList<>();
        //Массив для проверки того, сколько слов уже перебрали
        //Нужен для того, чтобы начать добавлять повторные слова, когда словарь заканчивается
        ArrayList<WordCard> checkedWordCards = new ArrayList<>();
        WordCard randomCard;


        tempArrayList = repeatCreatingArray(tempArrayList);


        //Выполняется пока не набрано заданное количество слов во временном списке
        while (tempArrayList.size() < countOfCurrentLearnWords) {

            randomCard = getRandomWordCardFromMainDictionary();

            if (!checkedWordCards.contains(randomCard)) {
                checkedWordCards.add(randomCard);
            }


            //Проверяем содержится ли случайная карточка во временном списке
            //Проверяем есть ли у карточки слова отметка о том, что оно выучено
            //Если карточка не была добавлена ранее и не была выучена, то она будет добавлена во временный список, а после в список изучаемых слов
            if (!tempArrayList.contains(randomCard) && randomCard.isLearned() == 0) {
                //Присваиваем карточке слова отметку о том, что оно изучается в настоящее время
                randomCard.setNowLearning(1);
                //Добавляем карточку слова во временный список
                tempArrayList.add(randomCard);
                //Перезаписываем данные о карточке в базе данных
                wordsDataBaseHelper.changeExistsWord(randomCard);
            }

            //Если временный список ещё не заполнен, а список для проверки полон, то
            //во временный список будет добавлено случайное слово
            if (checkedWordCards.size() >= allOfWordsOfDictionary.size()) {
                Toast toast = Toast.makeText(context, "СЛОВА ЗАКАНЧИВАЮТСЯ!!!", Toast.LENGTH_SHORT);
                toast.show();
                int check = 0;
                while (check == 0) {
                    randomCard = getRandomWordCardFromMainDictionary();
                    if (!tempArrayList.contains(randomCard)) {
                        tempArrayList.add(randomCard);
                        check++;
                    }
                }
            }

        }
        return tempArrayList;
    }

    private WordCard getRandomWordCardFromMainDictionary(){
        int random;
        random = (int) (Math.random() * allOfWordsOfDictionary.size());
        return allOfWordsOfDictionary.get(random);
    }


    /**
     * Проверяет существует ли словарь изучаемых слов
     * Если не существует, то пробует загрузить его из списка всех существующих слов
     * для этого проверяется nowLearning если > 0 то добавляется в словарь изучаемых слов
     *
     * @return Array List с карточками
     */
    private ArrayList<WordCard> createLearningListFromMainDictionary() {
        ArrayList<WordCard> tempArrayList = new ArrayList<>();
        //if (currentLearningWords == null) {
        for (WordCard wordCard : allOfWordsOfDictionary) {
            if (wordCard.nowLearning() > 0) tempArrayList.add(wordCard);
            if (tempArrayList.size() == countOfCurrentLearnWords) break;
        }
        // }
        return tempArrayList;
    }


    /**
     * Метод для установки изучаемого слова при первом построении страницы
     *
     * @param view
     */
    public void showWord(View view) {
        TextView targetWord = view.findViewById(R.id.target_word);
        String learnWord = wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + (countOfRepeatWord + 1) + " / " + countOfCurrentLearnWords;
        targetWord.setText(learnWord);
    }


    /**
     * @param word - слово которое нужно перевести
     */
    public void showWord(String word, View view) {
        TextView targetWord = view.findViewById(R.id.target_word);
        targetWord.setText(word);
    }

    /**
     * НЕт
     * В настоящее время на кнопки попадают первые 10 слов из словаря.
     * Необходимо передавать в метод массив изучаемых слов для отображения их на кнопках и дальнейшей работы с ними
     */
    public void createButtons(View view, Context context) {

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.button_layout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.setMargins(15, 0, 15, 30);
        ll.removeAllViews();


        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();


        currentLearningWords = createCurrentLearningWordsArrayList(context);

        Log.i(TAG, "after creating ==================================== " + currentLearningWords.size());


        if (answeredTrue) {

            learningWordsForButtons = getRandomListForCreateButtons(currentLearningWords, countOfButtons);

            Log.i(TAG, "More point");
            wordThatNeedsToBeTranslated = getWordForLearn(learningWordsForButtons);
        }


        Iterator<WordCard> iterator = learningWordsForButtons.iterator();


        for (int i = 0; i < countOfButtons; i++) {
            Button myButton = new Button(context);
            WordCard tempWordCardForButton = iterator.next();
            myButton.setText(tempWordCardForButton.getEnglishWord());
            myButton.setTextSize(35);
            myButton.setPadding(5, 5, 5, 5);
            if (!answeredTrue) {
                myButton.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
            if (tempWordCardForButton.getEnglishWord().equals(wordThatNeedsToBeTranslated.getEnglishWord()) && tempWordCardForButton.getRightAnswerCount() == 0) {
                myButton.setBackgroundColor(context.getResources().getColor(R.color.green));
            }

            myButton.setOnClickListener(view2 -> {
                //передаём WordCard который принадлежит нажатой кнопке для проверки
                onClickButton(tempWordCardForButton, view, context);
            });
            myButton.setLayoutParams(layoutParams);
            ll.addView(myButton);
        }


        showWord(wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount() + " / " + (countOfRepeatWord + 1)+ " / " + countOfCurrentLearnWords + " / " + allOfWordsOfDictionary.size(), view);
    }


    /**
     * Создание HashSet с карточками по количеству кнопок
     *
     * @param wordCards      ArrayList с изучаемыми в настоящее время карточками
     * @param countOfButtons количество кнопок
     * @return
     */
    private ArrayList<WordCard> getRandomListForCreateButtons(ArrayList<WordCard> wordCards, int countOfButtons) {

        ArrayList<WordCard> learningWordsForButtons = new ArrayList<>();
        WordCard wordCard;
        int random = 0;

        /**
         *
         * Нужно вынести в отдельный метод
         */

        Log.i("------------ >> " , "start new " + learningWordsForButtons.size() );
        while (learningWordsForButtons.size() < countOfButtons) {
            Log.i("------------ >> " , "start new " + learningWordsForButtons.size() );
            random = (int) (Math.random() * wordCards.size());
            wordCard = wordCards.get(random);
            if (!learningWordsForButtons.contains(wordCard)) {
                learningWordsForButtons.add(wordCard);

            }

        }

        return learningWordsForButtons;
    }


    /**
     * @param wordCard получает WordsCard с кнопки для проверки ответа
     *                 Тут будет проверка ответа
     */
    public void onClickButton(WordCard wordCard, View view, Context context) {
        if (wordCard.getRussianWord().equals(wordThatNeedsToBeTranslated.getRussianWord())) {
            reactionToTheRightAnswer(wordCard);
            createButtons(view, context);
        } else {
            reactionToTheWrongAnswer(wordThatNeedsToBeTranslated, view, context);
            showWord(wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + (countOfRepeatWord + 1), view);
        }
    }

    /**
     * Десйствие при правильном ответе
     *
     * @param rightWordCard
     */
    private void reactionToTheRightAnswer(WordCard rightWordCard) {

        if (rightWordCard.getRightAnswerCount() < countOfRepeatWord) {
            rightWordCard.setRightAnswerCount(rightWordCard.getRightAnswerCount() + 1);
            wordsDataBaseHelper.changeExistsWord(rightWordCard);
            reWriteWordCardInArrayList(allOfWordsOfDictionary, rightWordCard);
        } else {
            rightWordCard.setRightAnswerCount(rightWordCard.getRightAnswerCount() + 1);
            rightWordCard.setNowLearning(0);
            rightWordCard.setIsLearned(1);
            wordsDataBaseHelper.changeExistsWord(rightWordCard);
            currentLearningWords.remove(rightWordCard);
            reWriteWordCardInArrayList(allOfWordsOfDictionary, rightWordCard);
        }
        answeredTrue = true;
        // currentLearningWords = createCurrentLearningWordsArrayList();
    }


    private void reactionToTheWrongAnswer(WordCard wrongWordCard, View view, Context context) {
        wrongWordCard.setRightAnswerCount(0);
        // System.out.println(wrongWordCard.getEnglishWord() + " " + wrongWordCard.getWrongAnswerCount());
        wrongWordCard.setWrongAnswerCount(wrongWordCard.getWrongAnswerCount() + 1);
        wordsDataBaseHelper.changeExistsWord(wrongWordCard);
        reWriteWordCardInArrayList(allOfWordsOfDictionary, wrongWordCard);
        answeredTrue = false;
        createButtons(view, context);
        // currentLearningWords = createCurrentLearningWordsArrayList();
    }


    private void reWriteWordCardInArrayList(ArrayList<WordCard> wordCards, WordCard wordCard) {
        wordCards.remove(wordCard);
        wordCards.add(wordCard);
    }


    /**
     * @param learningWordsForButtons
     * @return
     */
    public WordCard getWordForLearn(ArrayList<WordCard> learningWordsForButtons) {
        int random = (int) (Math.random() * learningWordsForButtons.size());
        // int count = 0;
        return learningWordsForButtons.get(random);
      /*  for (WordCard wordCard : learningWordsForButtons) {
            if (count == random) {
                wordThatNeedsToBeTranslated = wordCard;
                return wordCard;
            }
            count++;
        }
        return null;*/
    }


}
