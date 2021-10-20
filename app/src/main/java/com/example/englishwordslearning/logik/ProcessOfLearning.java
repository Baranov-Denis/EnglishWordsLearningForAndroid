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




    private int typeOfLearnFinal = 0;






    /**
     * typeOfLearn создан для изменения
     */
    private int typeOfLearn = 0;

    public int getTypeOfLearn() {
        return typeOfLearn;
    }

    private int setTypeOfLearnFinal(){
        if(typeOfLearn == 0) return 0;
        if(typeOfLearn == 1) return 1;
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
        wordsDataBaseHelper = WordsDataBaseHelper.getWordsDataBaseHelper();
        wordsDatabase = wordsDataBaseHelper.getReadableDatabase();
        allOfWordsOfDictionary = loadWordsDictionary();




    }


    /**
     * Загрузка главного словаря
     *
     * @return возвращает словарь состоящий из всех слов, содержащихся в базе данных
     */
    private ArrayList<WordCard> loadWordsDictionary() {
        ArrayList<WordCard> allWords = new ArrayList<>();
        numberOfUnlearnedWords = 0;
        Cursor wordCursor = wordsDatabase.query(WordsDataBaseHelper.getTableName(), null, null, null, null, null, "ENGLISH_WORD");
        while (wordCursor.moveToNext()) {
            if (wordCursor.getInt(6) == 0) numberOfUnlearnedWords++;
            allWords.add(new WordCard(wordCursor.getString(1), wordCursor.getString(2), wordCursor.getInt(3), wordCursor.getInt(4), wordCursor.getInt(5), wordCursor.getInt(6)));
        }
        Log.i(TAG, "unlearned words : " + numberOfUnlearnedWords + " all words : " + allWords.size());
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
        wordsDataBaseHelper.addNewWord(EnglishWord, russianWord, view);
        allOfWordsOfDictionary = loadWordsDictionary();
    }

    /**
     * Метод удаляет карточку из базы данных
     * и перезагружает HashSet содержащий все слова библиотеки
     *
     * @param targetWord id primary key карточки из таблицы
     */
    public void deleteCurrentWord(long targetWord) {
        wordsDataBaseHelper.deleteCurrentWord(targetWord);
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
        ArrayList<WordCard> learnList = new ArrayList<>();
        WordCard randomCard;
        boolean endingWords = false;


        if (currentLearningWords == null) {
            for (WordCard wordCard : allOfWordsOfDictionary) {
                if (wordCard.nowLearning() > 0 & wordCard.isLearned() == 0) learnList.add(wordCard);
            }
        } else {
            learnList = new ArrayList<>(currentLearningWords);
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
     * Метод для установки изучаемого слова при первом построении страницы
     *
     * @param view
     */
   /* public void showWord(View view) {
        TextView targetWord = view.findViewById(R.id.target_word);
        String learnWord = wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + wordThatNeedsToBeTranslated.isLearned() + " / " + countOfCurrentLearnWords;
        targetWord.setText(learnWord);
    }
*/

    /**
     * @param - слово которое нужно перевести
     */
    public void showWord(View view) {
        TextView targetWord = view.findViewById(R.id.target_word);

        if (typeOfLearnFinal == 0) {
            targetWord.setText(wordThatNeedsToBeTranslated.getRussianWord() + "    " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + (countOfRepeatWord + 1) );
        } else if(typeOfLearnFinal == 1){
            targetWord.setText(wordThatNeedsToBeTranslated.getEnglishWord() + "    " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/" + (countOfRepeatWord + 1));
        }

    }

    /**
     * НЕт
     * В настоящее время на кнопки попадают первые 10 слов из словаря.
     * Необходимо передавать в метод массив изучаемых слов для отображения их на кнопках и дальнейшей работы с ними
     */
  /*  public void createButtons(View view, Context context) {

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.button_layout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.setMargins(15, 0, 15, 30);
        ll.removeAllViews();


        allOfWordsOfDictionary = loadWordsDictionary();

        currentLearningWords = createLearnList();


        if (answeredTrue) {
            learningWordsForButtons = getRandomListForCreateButtons(currentLearningWords, countOfButtons);
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


        showWord(wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/now " + wordThatNeedsToBeTranslated.nowLearning() + " / " + countOfCurrentLearnWords + " / " + numberOfUnlearnedWords, view);
    }*/
/*
    public void createButtons(View view, Context context, ArrayList<String> savedList ) {

        for (int i = 0; i < savedList.size(); i++) {

            if (i == 0) {
                showWord(savedList.get(0) + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount() + "/now " + wordThatNeedsToBeTranslated.nowLearning() + " / " + countOfCurrentLearnWords + " / " + numberOfUnlearnedWords, view);
            } else {
                Button myButton = buttons.get(i);

                myButton.setText(savedList.get(i));
                myButton.setBackgroundColor(context.getResources().getColor(R.color.gray_light_50));
                if (!answeredTrue) {
                    myButton.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if (savedList.get(i).equals(wordThatNeedsToBeTranslated.getEnglishWord()) ) {
                    myButton.setBackgroundColor(context.getResources().getColor(R.color.green));
                }

                myButton.setOnClickListener(view2 -> {
                    //передаём WordCard который принадлежит нажатой кнопке для проверки
                  //  onClickButton(tempWordCardForButton, view, context);
                });
                // myButton.setLayoutParams(layoutParams);
                // ll.addView(myButton);
            }
        }



    }*/
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

      //  final Animation alfa = AnimationUtils.loadAnimation(mainContext, R.anim.scale_down);

        allOfWordsOfDictionary = loadWordsDictionary();

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
            } else if(typeOfLearnFinal == 1){
                myButton.setText(tempWordCardForButton.getRussianWord());
            }

            final Animation animA = AnimationUtils.loadAnimation(mainContext, R.anim.up_and_down);
            final Animation animB = AnimationUtils.loadAnimation(mainContext, R.anim.up_and_down);
          //  if(i%2 ==0)//myButton.startAnimation(animA);

          //  else //myButton.startAnimation(animB);

            myButton.setBackground(context.getResources().getDrawable(R.drawable.button_for_learn_background));

         //   myButton.setBackgroundColor(context.getResources().getColor(R.color.gray_light_50));


            if (!answeredTrue) {
                //myButton.setBackgroundColor(context.getResources().getColor(R.color.red));
                myButton.setBackground(context.getResources().getDrawable(R.drawable.wrong_button_background));
            }
            if (tempWordCardForButton.getEnglishWord().equals(wordThatNeedsToBeTranslated.getEnglishWord()) && tempWordCardForButton.getRightAnswerCount() == 0) {
               // myButton.setBackgroundColor(context.getResources().getColor(R.color.green));
                myButton.setBackground(context.getResources().getDrawable(R.drawable.right_button_for_learn_background));
            }
           // final Animation animAlpha = AnimationUtils.loadAnimation(mainContext, R.anim.scale_down);

            myButton.setOnClickListener(view2 -> {
              //  view2.startAnimation(animAlpha);
                //передаём WordCard который принадлежит нажатой кнопке для проверки
                onClickButton(tempWordCardForButton, view, context);
              /*  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onClickButton(tempWordCardForButton, view, context);
                    }
                }, 300);*/

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
    public void onClickButton(WordCard wordCard, View view, Context context) {

        if (wordCard.getRussianWord().equals(wordThatNeedsToBeTranslated.getRussianWord())) {

            reactionToTheRightAnswer(wordCard);


            createButtons(view, context);

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
     * Создание массива для сохранения состояния при повороте экрана
     *
     * @return
     *//*
    public ArrayList<String> getSaveList() {
        ArrayList<String> saveList = new ArrayList<>();
        saveList.add(wordThatNeedsToBeTranslated.getRussianWord());
        for (Button button : buttons) saveList.add(button.getText().toString());
        return saveList;
    }*/
}
