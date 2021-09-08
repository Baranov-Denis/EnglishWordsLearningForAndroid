package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

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
    }

    private void setSeekBar() {
        int countOfRepeatWord = mainInterface.getCountOfRepeatWord();
        SeekBar seekBar = findViewById(R.id.seekBarForCountOfLearningWords);
        seekBar.setProgress(countOfRepeatWord);
        TextView textViewOfCount = findViewById(R.id.count_of_repeat_int);
        textViewOfCount.setText(String.valueOf(countOfRepeatWord));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int count, boolean b) {
                mainInterface.setCountOfRepeatWord(count);
                textViewOfCount.setText(String.valueOf(count));
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

    private void saveSettings() {
        SharedPreferences.Editor editor = MainActivity.getMySharedPreference().edit();
        editor.putInt(MainActivity.COUNT_OF_REPEAT, mainInterface.getCountOfRepeatWord());
        editor.apply();
    }
}