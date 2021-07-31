package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.WordCard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class LearnActivity extends AppCompatActivity {

    private MainInterface mainInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        mainInterface = MainInterface.getMainInterface(this);

        mainInterface.createButtonsForLearning(findViewById(R.id.learn_activity),this);

        mainInterface.showWordForLearn(mainInterface.getWordThatNeedsToBeTranslated().getRussianWord() , findViewById(R.id.learn_activity));

    }

/*
    /**
     * @param word - слово которое нужно перевести
     */
  /* private void showWord(String word, View view) {
        TextView targetWord = findViewById(R.id.target_word);
        targetWord.setText(word);
    }**/

    /**
     * В настоящее время на кнопки попадают первые 10 слов из словаря.
     * Необходимо передавать в метод массив изучаемых слов для отображения их на кнопках и дальнейшей работы с ними
     *
     * @param countOfButtons вводит количество создаваемых кнопок
     */
 /*   private void createButtons(int countOfButtons, View view) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.setMargins(15, 15, 15, 0); // left, top, right, bottom
        ll.removeAllViews();


        //TODO заменить все слова на избранные
        ArrayList<WordCard> TEMPPPPlearningWordsForButtons = mainInterface.getAllOfWordsOfDictionary();

        learningWordsForButtons = getHashSetFromArrayListForCreateButtons(TEMPPPPlearningWordsForButtons, countOfButtons);
        Iterator<WordCard> iterator = learningWordsForButtons.iterator();


        for (int i = 0; i < countOfButtons; i++) {
            Button myButton = new Button(this);
            WordCard tempWordCardForButton = iterator.next();
            myButton.setText(tempWordCardForButton.getEnglishWord());
            myButton.setOnClickListener(view -> {
                //передаём WordCard который принадлежит нажатой кнопке для проверки
                onClickButton(tempWordCardForButton);
            });
            myButton.setLayoutParams(layoutParams);
            ll.addView(myButton);
        }
    }*/

    /**
     * Создание HashSet с карточками по количеству кнопок
     *
     * @param wordCards      ArrayList с изучаемыми в настоящее время карточками
     * @param countOfButtons количество кнопок
     * @return HashSet c карточками - карточек столько сколько кнопок, hashSet создаёт случайный порядок
     * карточек, и не нужна проверка на дубликаты
     */
   /* private HashSet<WordCard> getHashSetFromArrayListForCreateButtons(ArrayList<WordCard> wordCards, int countOfButtons) {
        HashSet<WordCard> learningWordsForButtons = new HashSet<>();
        while (learningWordsForButtons.size() < countOfButtons) {
            learningWordsForButtons.add(wordCards.get((int) (Math.random() * wordCards.size())));
        }
        return learningWordsForButtons;
    }*/

    /**
     * @param wordCard получает WordsCard с кнопки для проверки ответа
     *                 Тут будет проверка ответа
     */
  /*  public void onClickButton(WordCard wordCard) {

        Toast toast;
        if(wordCard.getRussianWord().equals(wordThatNeedsToBeTranslated.getRussianWord())){
            toast = Toast.makeText(this, "E-e-e-e-e!", Toast.LENGTH_LONG);
        }else{
            toast = Toast.makeText(this, "Lo-o-o-o-se!", Toast.LENGTH_LONG);
        }
        toast.show();
        createButtons(countOfWordButtons);
    wordThatNeedsToBeTranslated = getWordForLearn(learningWordsForButtons);
       // showWord(mainLearnWord.getRussianWord());
    }*/



/*

   private WordCard getWordForLearn(HashSet<WordCard> learningWordsForButtons){
        int random =(int) (Math.random() * learningWordsForButtons.size());
        int count = 0;
        for (WordCard wordCard : learningWordsForButtons) {
            if(count == random){
                wordThatNeedsToBeTranslated = wordCard;
                return wordCard;
            }
            count++;
        }
        return null;
   }*/
}