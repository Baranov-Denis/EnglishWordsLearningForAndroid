package com.example.englishwordslearning.logik;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.englishwordslearning.R;
import com.example.englishwordslearning.database.WordsDataBaseHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class ProcessOfLearning {

    private static ProcessOfLearning processOfLearning;
    private WordsDataBaseHelper wordsDataBaseHelper;
    private SQLiteDatabase wordsDatabase;

    private static final String TAG = " ->> learning";

    /**
     * Переменная для
     */
    private int typeOfLearnFinal = 0;


    /**
     * Переменная для выбора имени таблицы currentTableName из
     */
    private static int currentTableNum;

    public static int getCurrentTableNum() {
        return currentTableNum;
    }

    public static void setCurrentTableNum(int currentTableNum) {
        ProcessOfLearning.currentTableNum = currentTableNum;
        currentTableName = WordsDataBaseHelper.getTableNamesList().get(currentTableNum);
    }

    public static String currentTableName;

    public String getCurrentTableName() {
        return currentTableName;
    }

    /**
     * typeOfLearn создан для изменения
     */
    private int typeOfLearn = 0;

    public int getTypeOfLearn() {
        return typeOfLearn;
    }

    private int setTypeOfLearnFinal() {
        if (typeOfLearn == 0) return 0;
        if (typeOfLearn == 1) return 1;
        return (int) (Math.random() * 2);
    }

    public void setTypeOfLearn(int typeOfLearn) {
        this.typeOfLearn = typeOfLearn;
    }

    private Context mainContext;

    /**
     * ArrayList содержащий все слова из библиотеки
     */
    private ArrayList<WordCard> allOfWordsOfDictionary;

    public int getAllOfWordsOfDictionarySize() {
        return allOfWordsOfDictionary.size();
    }

    private int numberOfUnlearnedWords;

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


    private ArrayList<Button> buttons;


    /**
     * Переменная для отслеживания правильного ответа
     * используется при создании кнопок
     * в случае когда дан неправильный ответ answeredTrue
     * будет присвоено значение false после чего
     * при создании кнопок не будут обновлены надписи на них
     * а вместо этого кнопки изменят цвет
     */
    private boolean answeredTrue = true;

    private boolean done = true;


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
        if (countOfRepeatWord > this.countOfRepeatWord) {
            reWriteWordDataBaseAfterChangingRepeat(countOfRepeatWord);
        }
        this.countOfRepeatWord = countOfRepeatWord;
    }


    /**
     * Данный метод служит для того, чтобы при увеличении количества повторений слова,
     * все выученые слова, которые были повторены меньше нового значения будут перезаписаны как невыученые
     *
     * @param newCount новое значение количества повторений слова
     */
    private void reWriteWordDataBaseAfterChangingRepeat(int newCount) {
        for (WordCard wordCard : allOfWordsOfDictionary) {
            if (wordCard.getRightAnswerCount() < newCount) {
                if (wordCard.isLearned() > 0) {
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

        return processOfLearning;
    }


    /**
     * -------------------------------------------------------------------   Конструктор!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private ProcessOfLearning(Context context) {
        this.mainContext = context;
        wordsDataBaseHelper = WordsDataBaseHelper.getWordsDataBaseHelper(context);
        wordsDatabase = wordsDataBaseHelper.getReadableDatabase();
        currentTableName = WordsDataBaseHelper.getTableNamesList().get(currentTableNum);
    //    allOfWordsOfDictionary = loadWordsDictionary();


    }

    public void updateWordsDictionary() {
        allOfWordsOfDictionary = loadWordsDictionary();
    }


    /**
     * Загрузка главного словаря
     *
     * @return возвращает словарь состоящий из всех слов, содержащихся в базе данных
     */
    private ArrayList<WordCard> loadWordsDictionary() {
        ArrayList<WordCard> allWords = new ArrayList<>();
        WordCard tempWordCard;
        numberOfUnlearnedWords = 0;
        Cursor wordCursor = wordsDatabase.query(currentTableName, null, null, null, null, null, "ENGLISH_WORD");
        while (wordCursor.moveToNext()) {
            if (wordCursor.getInt(6) == 0) numberOfUnlearnedWords++;
            tempWordCard = new WordCard(wordCursor.getString(1), wordCursor.getString(2), wordCursor.getInt(3), wordCursor.getInt(4), wordCursor.getInt(5), wordCursor.getInt(6));

            if(!allWords.contains(tempWordCard)) {
                allWords.add(tempWordCard);
            }else {
                Toast toast = Toast.makeText(mainContext , "Word already exist "  + tempWordCard.getEnglishWord(), Toast.LENGTH_SHORT);
                toast.show();
            }

        }
        return allWords;
    }

    public void setAllOfWordsOfDictionary(ArrayList<WordCard> allOfWordsOfDictionary) {
        this.allOfWordsOfDictionary = allOfWordsOfDictionary;
    }


    /**
     * Данный метод заполняет карточками основной массив содержащий все слова
     */
    // public void loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary() {

    //   allOfWordsOfDictionary = wordsDataBaseHelper.loadDictionaryFromSQLiteDataBase();

    //   }

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
        wordsDataBaseHelper.addNewWord(currentTableName, EnglishWord, russianWord, view);
        updateWordsDictionary();
        // allOfWordsOfDictionary = loadWordsDictionary();
    }

    /**
     * Метод удаляет карточку из базы данных
     * и перезагружает HashSet содержащий все слова библиотеки
     *
     * @param targetWord id primary key карточки из таблицы
     */
    public void deleteCurrentWord(long targetWord) {
        wordsDataBaseHelper.deleteCurrentWord(currentTableName, targetWord);
        allOfWordsOfDictionary = loadWordsDictionary();
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


        allOfWordsOfDictionary = loadWordsDictionary();

        //Очищает словарь изучаемых слов. Иначе при изучении будут загружены не сброшенные данные.
        currentLearningWords = null;
        wordThatNeedsToBeTranslated = null;
        answeredTrue = true;
        done = true;
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * ----------------------------- Learn Activity ------------------------------------------------
     * ---------------------------------------------------------------------------------------------
     */


    private ArrayList<WordCard> createLearnList() {
        //updateWordsDictionary();
        ArrayList<WordCard> learnList = new ArrayList<>();
        WordCard randomCard;
        boolean endingWords = false;

        for (WordCard wordCard : allOfWordsOfDictionary) {

            if (wordCard.nowLearning() > 0 & wordCard.isLearned() == 0)
                learnList.add(wordCard);
        }

        while (learnList.size() < countOfCurrentLearnWords) {

            randomCard = getRandomWordCardFromMainDictionary();

            if (numberOfUnlearnedWords > countOfCurrentLearnWords) {
                if (randomCard.nowLearning() == 0 && !learnList.contains(randomCard) && randomCard.isLearned() == 0) {
                    //Присваиваем карточке слова отметку о том, что оно изучается в настоящее время
                    randomCard.setNowLearning(1);
                    //Перезаписываем данные о карточке в базе данных
                    wordsDataBaseHelper.changeExistsWord(randomCard);
                    learnList.add(randomCard);
                }
            } else {

                if (!learnList.contains(randomCard)) {
                    endingWords = true;
                    //Присваиваем карточке слова отметку о том, что оно изучается в настоящее время
                    randomCard.setNowLearning(1);
                    //Перезаписываем данные о карточке в базе данных
                    wordsDataBaseHelper.changeExistsWord(randomCard);
                    learnList.add(randomCard);

                }
            }
        }


        if (endingWords) {
            String text = "";

            if (numberOfUnlearnedWords > 0)
                text = "Слова на исходе осталось неизученно : " + numberOfUnlearnedWords;
            else text = "Вы выучили все слова.";

            Toast toast = Toast.makeText(mainContext, text, Toast.LENGTH_SHORT);
            toast.show();
        }

        return learnList;
    }


    private WordCard getRandomWordCardFromMainDictionary() {
        int random;
        random = (int) (Math.random() * allOfWordsOfDictionary.size());
        return allOfWordsOfDictionary.get(random);
    }


    /**
     * @param - слово которое нужно перевести
     */
    public void showWord(View view) {
        TextView targetWord = view.findViewById(R.id.target_word);
        TextView targetWordCount = view.findViewById(R.id.count_of_target_word);
        if (typeOfLearnFinal == 0) {
            targetWord.setText(wordThatNeedsToBeTranslated.getRussianWord());
        } else if (typeOfLearnFinal == 1) {
            targetWord.setText(wordThatNeedsToBeTranslated.getEnglishWord());
        }

        targetWordCount.setText(wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + (countOfRepeatWord + 1));


    }


    private ArrayList<Button> getButtons(View view) {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(view.findViewById(R.id.button_1));
        buttons.add(view.findViewById(R.id.button_2));
        buttons.add(view.findViewById(R.id.button_3));
        buttons.add(view.findViewById(R.id.button_4));
        buttons.add(view.findViewById(R.id.button_5));
        buttons.add(view.findViewById(R.id.button_6));
        buttons.add(view.findViewById(R.id.button_7));
        buttons.add(view.findViewById(R.id.button_8));
        buttons.add(view.findViewById(R.id.button_9));
        buttons.add(view.findViewById(R.id.button_10));
        return buttons;
    }


    public void createButtons(View view, Context context) {

        buttons = getButtons(view);

        final Animation alfa = AnimationUtils.loadAnimation(mainContext, R.anim.faf);

        //allOfWordsOfDictionary = loadWordsDictionary();
        currentLearningWords = createLearnList();


        if (answeredTrue && done) {

            done = false;
            typeOfLearnFinal = setTypeOfLearnFinal();
            learningWordsForButtons = getRandomListForCreateButtons(currentLearningWords, countOfButtons);

            wordThatNeedsToBeTranslated = getWordForLearn(learningWordsForButtons);

        }

        showWord(view);

        Iterator<WordCard> iterator = learningWordsForButtons.iterator();

        for (int i = 0; i < countOfButtons; i++) {


            Button myButton = buttons.get(i);

            WordCard tempWordCardForButton = iterator.next();


            if (typeOfLearnFinal == 0) {
                myButton.setText(tempWordCardForButton.getEnglishWord());
            } else if (typeOfLearnFinal == 1) {
                myButton.setText(tempWordCardForButton.getRussianWord());
            }


            if (myButton.getText().length() < 10) {
                myButton.setTextSize(30);
            } else if (myButton.getText().length() < 15) {
                myButton.setTextSize(27);
            } else {
                myButton.setTextSize(23);
            }
            myButton.setBackground(context.getResources().getDrawable(R.drawable.button_for_learn_background));


            if (!answeredTrue) {


                myButton.setBackground(context.getResources().getDrawable(R.drawable.wrong_button_background));
            }
            if (tempWordCardForButton.getEnglishWord().equals(wordThatNeedsToBeTranslated.getEnglishWord()) && tempWordCardForButton.getRightAnswerCount() == 0) {
                final Animation wrongAnim = AnimationUtils.loadAnimation(mainContext, R.anim.wrong_answer_animation_button);
                myButton.startAnimation(wrongAnim);
                myButton.setBackground(context.getResources().getDrawable(R.drawable.right_button_for_learn_background));
            }


            myButton.setOnClickListener(view2 -> {

                onClickButton(tempWordCardForButton, view, context, view2);


            });


        }


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

        while (learningWordsForButtons.size() < countOfButtons) {
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
    public void onClickButton(WordCard wordCard, View view, Context context, View button) {
        final Animation animAlpha = AnimationUtils.loadAnimation(mainContext, R.anim.right_answer_animation_button);
        final Animation animA = AnimationUtils.loadAnimation(mainContext, R.anim.eeee);

        if (wordCard.getRussianWord().equals(wordThatNeedsToBeTranslated.getRussianWord())) {

            if (answeredTrue) {
                reactionToTheRightAnswer(wordCard);

                button.startAnimation(animAlpha);
                button.setBackground(context.getResources().getDrawable(R.drawable.right_button_for_learn_background));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createButtons(view, context);
                    }
                }, 300);

            } else {
                button.startAnimation(animA);
                reactionToTheRightAnswer(wordCard);
                createButtons(view, context);
            }

        } else {
            reactionToTheWrongAnswer(wordThatNeedsToBeTranslated, view, context);
            showWord(view);
            //  showWord(wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + (countOfRepeatWord + 1), view);
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
        done = true;
    }


    private void reactionToTheWrongAnswer(WordCard wrongWordCard, View view, Context context) {
        wrongWordCard.setRightAnswerCount(0);
        wrongWordCard.setWrongAnswerCount(wrongWordCard.getWrongAnswerCount() + 1);
        wordsDataBaseHelper.changeExistsWord(wrongWordCard);
        reWriteWordCardInArrayList(allOfWordsOfDictionary, wrongWordCard);
        answeredTrue = false;
        createButtons(view, context);
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
        return learningWordsForButtons.get(random);
    }


    /**
     * Метод нужен для того, чтобы после изменения модели изучения между англ - русск, русск - англ
     * и случайным выбором, ставилась метка о том, что
     * 1 - ответ правильный
     * 2 - ответ закончен
     * Это позволяет обновить кнопки и угадываемое слово иначе после изменения модели
     * первый ответ будет старым.
     */
    public void updateButtons() {
        answeredTrue = true;
        done = true;
    }


}
