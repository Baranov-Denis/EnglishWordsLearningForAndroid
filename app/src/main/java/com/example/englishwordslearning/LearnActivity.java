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

        mainInterface.showWordForLearn(findViewById(R.id.learn_activity));

    }
}