package com.example.englishwordslearning.logik;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.englishwordslearning.R;
import com.example.englishwordslearning.database.WordsDataBase;

import java.util.ArrayList;
import java.util.Iterator;

public class ProcessOfLearning {

    private static ProcessOfLearning processOfLearning;
    private WordsDataBase wordsDataBase;

    /**
     * ArrayList содержащий все слова из библиотеки
     */
    private ArrayList<WordCard> allOfWordsOfDictionary;

    /**
     * HashSet содержащий изучаемые слова выбранные для кнопок
     */
    private ArrayList<WordCard> learningWordsForButtons;


    /**
     * ArrayList в котором содержатся слова, которые изучаются в настоящее время
     * количество слов в этом списке определяется переменной countOfCurrentLearningWords
     */
    private ArrayList<WordCard> currentLearningWords;

    /**
     * количество слов в списке изучаемых слов
     */
    private int countOfCurrentLearningWords = 10;

    /**
     * количество необходимых правильных ответов для того чтобы слово считалось выученным
     */
    private int countOfRepeatWord = 19;


    /**
     * слово которое показывается вверху страницы которое нужно перевести
     */
    private WordCard wordThatNeedsToBeTranslated;


    /**
     * количество кнопок с вариантами слов
     */
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
        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();
        // cleanAllProgress();
        currentLearningWords = createCurrentLearningWordsArrayList();

        //  System.out.println(currentLearningWords.size() + " ---------------------------------------------->");
        for (WordCard x : currentLearningWords) {
            System.out.println(x.getEnglishWord() + " - " + x.getRightAnswerCount() + " ");
        }
        System.out.println(currentLearningWords.size() + " ---------------------------------------------->");
    }

    public void setAllOfWordsOfDictionary(ArrayList<WordCard> allOfWordsOfDictionary) {
        this.allOfWordsOfDictionary = allOfWordsOfDictionary;
    }

    /**
     * ----------------------------- Create Activity -----------------------------------------------
     */

    /**
     *
     */
    public void loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary() {
        allOfWordsOfDictionary = wordsDataBase.loadDictionaryFromSQLiteDataBase();
    }

    /**
     * Метод добавляет карточку в базу данных
     * с нулевыми показателями всех полей
     * и перезагружает ArrayList содержащий все слова библиотеки
     *
     * @param EnglishWord английское слово
     * @param russianWord русское слово
     */
    public void addNewWord(String EnglishWord, String russianWord) {
        wordsDataBase.addNewWord(EnglishWord, russianWord);
        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();
    }

    /**
     * Метод удаляет карточку из базы данных
     * и перезагружает HashSet содержащий все слова библиотеки
     *
     * @param targetWord id primary key карточки из таблицы
     */
    public void deleteCurrentWord(long targetWord) {
        wordsDataBase.deleteCurrentWord(targetWord);
        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();
    }


    /**
     * сбрасывает весь прогресс переписывает количество правильных и неправильных ответов
     * слово не изучено и не изучается
     */
    public void cleanAllProgress() {
        for (WordCard wordCard : allOfWordsOfDictionary) {
            wordCard.setRightAnswerCount(0);
            wordCard.setWrongAnswerCount(0);
            wordCard.setNowLearning(0);
            wordCard.setIsLearned(0);
            wordsDataBase.changeExistsWord(wordCard);
        }
        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();
    }















    /**
     * ----------------------------- Learn Activity ------------------------------------------------
     */

    /**
     * Создаёт список изучаемых слов
     *
     * @return ArrayList со списком изучаемых слов помеченных как nowLearning > 0
     */
    private ArrayList<WordCard> createCurrentLearningWordsArrayList() {

        ArrayList<WordCard> tempArrayList = new ArrayList<>();
        WordCard randomCard;
        int random;
        int allOfWordsOfDictionarySize = allOfWordsOfDictionary.size();

        if (currentLearningWords != null) tempArrayList.addAll(currentLearningWords);
        else tempArrayList = createLearningListFirstStep();

        int wordsCount = 0;

        while (tempArrayList.size() < countOfCurrentLearningWords) {

            random = (int) (Math.random() * allOfWordsOfDictionarySize);
            randomCard = allOfWordsOfDictionary.get(random);

            if (!tempArrayList.contains(randomCard) && randomCard.isLearned() == 0) {
                randomCard.setNowLearning(1);
                tempArrayList.add(randomCard);
                wordsDataBase.changeExistsWord(randomCard);
            }
            if (wordsCount < allOfWordsOfDictionarySize) {
                wordsCount++;
            } else {
               tempArrayList.add(allOfWordsOfDictionary.get(0));
                System.out.println("ALARM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        }

        return tempArrayList;
    }


    /**
     * Проверяет существует ли словарь изучаемых слов
     * Если не существует, то пробует загрузить его из списка всех существующих слов
     * для этого проверяется nowLearning если > 0 то добавляется в словарь изучаемых слов
     *
     * @return
     */
    private ArrayList<WordCard> createLearningListFirstStep() {
        ArrayList<WordCard> tempArrayList = new ArrayList<>();
        if (currentLearningWords == null) {
            for (WordCard wordCard : allOfWordsOfDictionary) {
                if (wordCard.nowLearning() > 0)
                    tempArrayList.add(wordCard);
                if (tempArrayList.size() == countOfCurrentLearningWords) break;
            }
        }
        return tempArrayList;
    }


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


        loadDictionaryFromSQLiteDataBaseToAllOfWordsOfDictionary();

        currentLearningWords = createCurrentLearningWordsArrayList();

        System.out.println(currentLearningWords.size() + " ---------------------------------------------->");
        for (WordCard x : currentLearningWords) {
            System.out.println(x.getEnglishWord() + " - " + x.getRightAnswerCount() + " ");
        }
        System.out.println(currentLearningWords.size() + " ---------------------------------------------->");
        /**
         *
         * ***************************
         *
         */
        learningWordsForButtons = getRandomListForCreateButtons(currentLearningWords, countOfButtons);

        getWordForLearn(learningWordsForButtons);

        Iterator<WordCard> iterator = learningWordsForButtons.iterator();


        for (int i = 0; i < countOfButtons; i++) {
            Button myButton = new Button(context);
            WordCard tempWordCardForButton = iterator.next();
            myButton.setText(tempWordCardForButton.getEnglishWord());
            myButton.setOnClickListener(view2 -> {
                //передаём WordCard который принадлежит нажатой кнопке для проверки
                onClickButton(tempWordCardForButton, view, context);
            });
            myButton.setLayoutParams(layoutParams);
            ll.addView(myButton);
        }

        showWord(wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount(), view);
    }


    /**
     * Создание HashSet с карточками по количеству кнопок
     *
     * @param wordCards      ArrayList с изучаемыми в настоящее время карточками
     * @param countOfButtons количество кнопок
     * @return HashSet c карточками - карточек столько сколько кнопок, hashSet создаёт случайный порядок
     * карточек, и не нужна проверка на дубликаты
     */
    private ArrayList<WordCard> getRandomListForCreateButtons(ArrayList<WordCard> wordCards, int countOfButtons) {
        ArrayList<WordCard> learningWordsForButtons = new ArrayList<>();
        WordCard wordCard;
        int wordCounter = 0;
        while (learningWordsForButtons.size() < countOfButtons) {
            wordCard = wordCards.get((int) (Math.random() * wordCards.size()));
            if(wordCounter < countOfButtons){
            if(!learningWordsForButtons.contains(wordCard)) {
                learningWordsForButtons.add(wordCard);
            }
            }else learningWordsForButtons.add(wordCards.get(0));
            wordCounter++;
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
            reactionToTheWrongAnswer(wordThatNeedsToBeTranslated);
            showWord(wordThatNeedsToBeTranslated.getRussianWord() + "  " + wordThatNeedsToBeTranslated.getRightAnswerCount(), view);
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
            wordsDataBase.changeExistsWord(rightWordCard);
            reWriteWordCardInArrayList(allOfWordsOfDictionary, rightWordCard);
        } else {
            rightWordCard.setRightAnswerCount(rightWordCard.getRightAnswerCount() + 1);
            rightWordCard.setNowLearning(0);
            rightWordCard.setIsLearned(1);
            wordsDataBase.changeExistsWord(rightWordCard);
            currentLearningWords.remove(rightWordCard);
            reWriteWordCardInArrayList(allOfWordsOfDictionary, rightWordCard);
        }
        currentLearningWords = createCurrentLearningWordsArrayList();
    }



    private void reactionToTheWrongAnswer(WordCard wrongWordCard) {
        wrongWordCard.setRightAnswerCount(0);
        System.out.println(wrongWordCard.getEnglishWord() + " " + wrongWordCard.getWrongAnswerCount());
        wrongWordCard.setWrongAnswerCount(wrongWordCard.getWrongAnswerCount() + 1);
        wordsDataBase.changeExistsWord(wrongWordCard);
        reWriteWordCardInArrayList(allOfWordsOfDictionary, wrongWordCard);
        currentLearningWords = createCurrentLearningWordsArrayList();
    }


    private void reWriteWordCardInArrayList(ArrayList<WordCard> wordCards, WordCard wordCard) {
        wordCards.remove(wordCard);
        wordCards.add(wordCard);
    }


    public WordCard getWordForLearn(ArrayList<WordCard> learningWordsForButtons) {
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


}
