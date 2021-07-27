package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.englishwordslearning.database.WordsDataBase;
import com.example.englishwordslearning.logik.ProcessOfLearning;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Подключение кнопок
        setOnClickButtons();
        //Загружаем базу данных
        new WordsDataBase(this);
        //Создание Process of learning
        ProcessOfLearning.getProcessOfLearning();
    }

    public void setOnClickButtons() {
        Button createActivityButton = findViewById(R.id.create_button);
        Button learnActivityButton = findViewById(R.id.learn_button);
        createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateActivity(view);
            }
        });
        learnActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLearningActivity(view);
            }
        });
    }

    public void startCreateActivity(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    public void startLearningActivity(View view) {
        Intent intent = new Intent(this, LearnActivity.class);
        startActivity(intent);
    }
}