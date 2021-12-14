package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.WordCard;
import com.example.englishwordslearning.recycler.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NewCreateActivity extends AppCompatActivity {

    private MainInterface mainInterface;
    private ArrayList<WordCard> allWordsFromCurrentDictionary;

    private TextView englishSort;
    private TextView russianSort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_create);
        mainInterface = MainInterface.getMainInterface(this);
        mainInterface.updateWordsDictionary();
        setRecyclerView();
        setOnClickForButtons();
        setSortButtons();
    }


    private void setRecyclerView() {
        RecyclerView wordsRecycler = findViewById(R.id.words_recycler);
        allWordsFromCurrentDictionary = mainInterface.getAllWordsFromCurrentDictionary();


        String[] engWords = new String[allWordsFromCurrentDictionary.size()];
        String[] rusWords = new String[allWordsFromCurrentDictionary.size()];
        String[] stat = new String[allWordsFromCurrentDictionary.size()];


        for (int i = 0; i < engWords.length; i++) {
            engWords[i] = allWordsFromCurrentDictionary.get(i).getEnglishWord();
            rusWords[i] = allWordsFromCurrentDictionary.get(i).getRussianWord();
            if(allWordsFromCurrentDictionary.get(i).getRightAnswerCount()>0) {
                stat[i] = allWordsFromCurrentDictionary.get(i).getRightAnswerCount() + "/" + (mainInterface.getCountOfRepeatWord() + 1);
            }else {
               // stat[i] = "✔";
                stat[i] = "-";
            }
        }


        RecyclerAdapter adapter = new RecyclerAdapter(engWords, rusWords, stat);
        wordsRecycler.setAdapter(adapter);
        wordsRecycler.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setSortButtons() {
        englishSort = findViewById(R.id.engl_sort);
        russianSort = findViewById(R.id.rus_sort);
        englishSort.setOnClickListener(e -> {
            sortEnglishWords();
            englishSort.setText("English ▼");
            russianSort.setText("Russian");
        });


        russianSort.setOnClickListener(e -> {
            sortRussianWords();
            englishSort.setText("English");
            russianSort.setText("Russian ▼");
        });
    }


    private void setOnClickForButtons() {
        Button saveButton = findViewById(R.id.button_for_save_word);
        Button deleteButton = findViewById(R.id.button_for_delete_word);
        Button resetButton = findViewById(R.id.button_for_reset_all_progress);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortRussianWords();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortEnglishWords();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllProgress();
            }
        });
    }

    /**
     * Запускаем сохранение слова
     *
     *
     */
    private void sortRussianWords() {

        Collections.sort(allWordsFromCurrentDictionary, new Comparator<WordCard>() {
            @Override
            public int compare(WordCard lhs, WordCard rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getRussianWord().compareTo(rhs.getRussianWord());
            }
        });
        setRecyclerView();
    }

    private void sortEnglishWords() {
        Collections.sort(allWordsFromCurrentDictionary, new Comparator<WordCard>() {
            @Override
            public int compare(WordCard lhs, WordCard rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getEnglishWord().compareTo(rhs.getEnglishWord());
            }
        });
        setRecyclerView();
    }



    private void resetAllProgress() {
        mainInterface.resetAllProgress();
        setRecyclerView();
    }


}