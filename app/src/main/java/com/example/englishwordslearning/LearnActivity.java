package com.example.englishwordslearning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.englishwordslearning.logik.MainInterface;

public class LearnActivity extends AppCompatActivity {

    private MainInterface mainInterface;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        mainInterface = MainInterface.getMainInterface(this);

        mainInterface.createButtonsForLearning(findViewById(R.id.learn_activity),this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Переназначаем действие кнопки вверх для получения анимации перехода
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if you want to go one activity back then put onBackPressed() method
                onBackPressed();
            }
        });






    }

    @Override
    protected void onResume() {
        super.onResume();
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        button0 = findViewById(R.id.button_10);

        final Animation rightToLeft = AnimationUtils.loadAnimation(this, R.anim.from_right_to_left);
        final Animation leftToRight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);

        button1.startAnimation(rightToLeft);
        button2.startAnimation(leftToRight);
        button3.startAnimation(rightToLeft);
        button4.startAnimation(leftToRight);
        button5.startAnimation(rightToLeft);
        button6.startAnimation(leftToRight);
        button7.startAnimation(rightToLeft);
        button8.startAnimation(leftToRight);
        button9.startAnimation(rightToLeft);
        button0.startAnimation(leftToRight);
    }

    @Override
    protected void onPause() {
        super.onPause();


        final Animation hideToRight = AnimationUtils.loadAnimation(this, R.anim.hide_to_right);
        final Animation hideToLeft = AnimationUtils.loadAnimation(this, R.anim.hide_to_left);

        button1.startAnimation(hideToRight);
        button2.startAnimation(hideToLeft);
        button3.startAnimation(hideToRight);
        button4.startAnimation(hideToLeft);
        button5.startAnimation(hideToRight);
        button6.startAnimation(hideToLeft);
        button7.startAnimation(hideToRight);
        button8.startAnimation(hideToLeft);
        button9.startAnimation(hideToRight);
        button0.startAnimation(hideToLeft);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LearnActivity.this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}