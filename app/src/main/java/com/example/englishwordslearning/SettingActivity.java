package com.example.englishwordslearning;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.englishwordslearning.database.WordsDataBaseHelper;
import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.ProcessOfLearning;

public class SettingActivity extends AppCompatActivity {
    private MainInterface mainInterface;

    LinearLayout setCountOfRepeats;
    LinearLayout setCountOfWords;
    LinearLayout setTypeOfLearningSpinner;
    LinearLayout setTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCountOfRepeats = findViewById(R.id.setCountOfRepeats);
        setCountOfWords = findViewById(R.id.setCountOfWords);
        setTypeOfLearningSpinner = findViewById(R.id.setTypeOfLearningSpinner);
        setTable = findViewById(R.id.setTable);

        setContentView(R.layout.activity_setting);
        mainInterface = MainInterface.getMainInterface(this);
        setSeekBar();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Переназначаем действие кнопки вверх для получения анимации перехода
        toolbar.setNavigationOnClickListener(view -> onBackPressed());


        setModeSpinner();
        setTableSpinner();

    }


    @Override
    protected void onResume() {
        super.onResume();
         setCountOfRepeats = findViewById(R.id.setCountOfRepeats);
         setCountOfWords = findViewById(R.id.setCountOfWords);
         setTypeOfLearningSpinner = findViewById(R.id.setTypeOfLearningSpinner);
         setTable = findViewById(R.id.setTable);

        final Animation rightToLeft = AnimationUtils.loadAnimation(this, R.anim.from_right_to_left);
        final Animation leftToRight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);

        setCountOfRepeats.startAnimation(rightToLeft);
        setCountOfWords.startAnimation(leftToRight);
        setTypeOfLearningSpinner.startAnimation(rightToLeft);
        setTable.startAnimation(leftToRight);
    }

    @Override
    protected void onPause() {
        super.onPause();
         setCountOfRepeats = findViewById(R.id.setCountOfRepeats);
        setCountOfWords = findViewById(R.id.setCountOfWords);
         setTypeOfLearningSpinner = findViewById(R.id.setTypeOfLearningSpinner);
        setTable = findViewById(R.id.setTable);

        final Animation hideToLeft = AnimationUtils.loadAnimation(this, R.anim.hide_to_left);
        final Animation hideToRight = AnimationUtils.loadAnimation(this, R.anim.hide_to_right);

        setCountOfRepeats.startAnimation(hideToLeft);
        setCountOfWords.startAnimation(hideToRight);
        setTypeOfLearningSpinner.startAnimation(hideToLeft);
        setTable.startAnimation(hideToRight);


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


    private void setModeSpinner() {
        String[] data = {"Русский - Английский", "Английский - Русский", "Случайный порядок"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.my_spinner, data);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop);
        Spinner spinner = findViewById(R.id.modeSpinner);
        spinner.setAdapter(adapter);

        spinner.setSelection(mainInterface.isTypeOfLearn());


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemPosition, long l) {
                mainInterface.setTypeOfLearn(selectedItemPosition);
                mainInterface.updateButtons();
                saveSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void setTableSpinner() {
        String[] data = WordsDataBaseHelper.getTableNamesList().toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.my_spinner, data);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop);
        Spinner spinner = findViewById(R.id.tableSpinner);

        spinner.setAdapter(adapter);

        spinner.setSelection(ProcessOfLearning.getCurrentTableNum());


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int selectedItemPosition, long l) {
                ProcessOfLearning.setCurrentTableNum(selectedItemPosition);
                saveSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void saveSettings() {
        SharedPreferences.Editor editor = MainActivity.getMySharedPreference().edit();
        editor.putInt(MainActivity.COUNT_OF_REPEAT, mainInterface.getCountOfRepeatWord());
        editor.putInt(MainActivity.COUNT_OF_NUMBER_CURRENT_WORDS, mainInterface.getTheNumberOfWordsBeingStudied());
        editor.putInt(MainActivity.TYPE_OF_LEARN_WORDS, mainInterface.isTypeOfLearn());
        editor.putInt(MainActivity.CURRENT_TABLE_NAME, ProcessOfLearning.getCurrentTableNum());
        editor.apply();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SettingActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}