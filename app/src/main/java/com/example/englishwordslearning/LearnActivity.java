package com.example.englishwordslearning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.englishwordslearning.logik.MainInterface;

public class LearnActivity extends AppCompatActivity {

    private MainInterface mainInterface;
    private TextView textView;
    private LinearLayout textViewCount;
    private  AppCompatImageButton backButton;
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
        setOnBackButton();
      //  ActionBar actionBar = getSupportActionBar();
    //    assert actionBar != null;
    //    actionBar.setDisplayHomeAsUpEnabled(true);
        //Переназначаем действие кнопки вверх для получения анимации перехода
    //    toolbar.setNavigationOnClickListener(view ->   onBackPressed());
    }



    @Override
    protected void onResume() {
        super.onResume();
        textView = findViewById(R.id.target_word);
        textViewCount = findViewById(R.id.count_of_target_word_layout);
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

        final Animation fromUp = AnimationUtils.loadAnimation(this, R.anim.from_up_to_down);
        final Animation rightToLeft = AnimationUtils.loadAnimation(this, R.anim.from_right_to_left);
        final Animation leftToRight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);

        textView.startAnimation(fromUp);

       textViewCount.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_left_to_right));
       backButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_right_to_left));

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

        final Animation hideToUp = AnimationUtils.loadAnimation(this, R.anim.hide_to_up);
        final Animation hideToRight = AnimationUtils.loadAnimation(this, R.anim.hide_to_right);
        final Animation hideToLeft = AnimationUtils.loadAnimation(this, R.anim.hide_to_left);

        textView.startAnimation(hideToUp);

        textViewCount.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_right));
        backButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_left));

        button0.startAnimation(hideToRight);
        button1.startAnimation(hideToLeft);
        button2.startAnimation(hideToRight);
        button3.startAnimation(hideToLeft);
        button4.startAnimation(hideToRight);
        button5.startAnimation(hideToLeft);
        button6.startAnimation(hideToRight);
        button7.startAnimation(hideToLeft);
        button8.startAnimation(hideToRight);
        button9.startAnimation(hideToLeft);
    }


    private void setOnBackButton(){
        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(e->{
           onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LearnActivity.this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}