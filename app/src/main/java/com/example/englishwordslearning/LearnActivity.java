package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.WordCard;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    private MainInterface mainInterface;


    private final int countOfWordButtons = 10;

    private ArrayList<WordCard> currentLearningWords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        mainInterface = MainInterface.getMainInterface(this);

        showWord("null");
        createButtons(countOfWordButtons);
    }


    /**
     * @param word - слово которое нужно перевести
     */
    private void showWord(String word) {
        TextView targetWord = findViewById(R.id.target_word);
        targetWord.setText(word);
    }

    /**
     * В настоящее время на кнопки попадают первые 10 слов из словаря.
     * Необходимо передавать в метод массив изучаемых слов для отображения их на кнопках и дальнейшей работы с ними
     *
     * @param countOfButtons вводит количество создаваемых кнопок
     */
    private void createButtons(int countOfButtons) {
        ArrayList<WordCard> words = mainInterface.getAllOfWordsOfDictionary();
        for (int i = 0; i < countOfButtons; i++) {
            Button myButton = new Button(this);
            WordCard tempWordCard = words.get(i);
            myButton.setText(tempWordCard.getEnglishWord());
            myButton.setOnClickListener(view -> {
                //передаём WordCard который принадлежит нажатой кнопке для проверки
                onClickButton(tempWordCard);
            });

            LinearLayout ll = (LinearLayout) findViewById(R.id.button_layout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
            layoutParams.setMargins(15, 15, 15, 0); // left, top, right, bottom
            myButton.setLayoutParams(layoutParams);
            ll.addView(myButton);
        }
    }

    /**
     * @param wordCard получает WordsCard с кнопки для проверки ответа
     *             Тут будет проверка ответа
     */
    public void onClickButton(WordCard wordCard) {
        showWord(wordCard.getRussianWord());
    }
}