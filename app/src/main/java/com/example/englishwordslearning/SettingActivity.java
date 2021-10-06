package com.example.englishwordslearning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.englishwordslearning.logik.MainInterface;

public class SettingActivity extends AppCompatActivity {
    private MainInterface mainInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mainInterface = MainInterface.getMainInterface(this);
        setSeekBar();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        setSwitch();
    }

    private void setSeekBar() {
        int countOfRepeatWord = mainInterface.getCountOfRepeatWord();
        int countOfCurrentLearnWords = mainInterface.getTheNumberOfWordsBeingStudied();

        SeekBar seekBarOfRepeat = findViewById(R.id.seek_bar_for_count_of_repeat_words);
        seekBarOfRepeat.setProgress(countOfRepeatWord);
        TextView textViewOfRepeat = findViewById(R.id.count_of_repeat_int);
        textViewOfRepeat.setText(String.valueOf(++countOfRepeatWord));

        SeekBar seekBarOfNumber = findViewById(R.id.seek_bar_count_of_current_learn_words);
        seekBarOfNumber.setProgress(countOfCurrentLearnWords);
        TextView textViewOfNumber = findViewById(R.id.count_of_current_learn_words_int);
        textViewOfNumber.setText(String.valueOf(countOfCurrentLearnWords));


        seekBarOfRepeat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int count, boolean b) {
                mainInterface.setCountOfRepeatWord(count);
                textViewOfRepeat.setText(String.valueOf(++count));
                saveSettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarOfNumber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int count, boolean b) {
                if (mainInterface.getNumberOfAllWords() < count) {
                    count = mainInterface.getNumberOfAllWords();
                }
                mainInterface.setNumberOfCurrentLearnWords(count);
                textViewOfNumber.setText(String.valueOf(count));
                saveSettings();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSwitch() {

        SwitchCompat switch1 = findViewById(R.id.switch1);
        setSwitchText(switch1);
        switch1.setChecked(mainInterface.isTypeOfLearn());
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mainInterface.setTypeOfLearn(isChecked);
            setSwitchText(switch1);
            saveSettings();
        });

    }

    private void setSwitchText(SwitchCompat switch1){
        if(mainInterface.isTypeOfLearn()) {
            switch1.setText(R.string.choose_type_of_learn_1);
        }else {
            switch1.setText(R.string.choose_type_of_learn_2);
        }
    }


    private void saveSettings() {
        SharedPreferences.Editor editor = MainActivity.getMySharedPreference().edit();
        editor.putInt(MainActivity.COUNT_OF_REPEAT, mainInterface.getCountOfRepeatWord());
        editor.putInt(MainActivity.COUNT_OF_NUMBER_CURRENT_WORDS, mainInterface.getTheNumberOfWordsBeingStudied());
        editor.putBoolean(MainActivity.TYPE_OF_LEARN_WORDS,mainInterface.isTypeOfLearn());
        editor.apply();
    }
}