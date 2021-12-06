package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

//import com.example.englishwordslearning.database.DatabaseHelper;
//import com.example.englishwordslearning.database.UserDataBaseHelper;
import com.example.englishwordslearning.database.WordsDataBaseHelper;
import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.ProcessOfLearning;

public class MainActivity extends AppCompatActivity {

    private MainInterface mainInterface;

    private static SharedPreferences mySharedPreference = null;
    public static final String COUNT_OF_REPEAT = "count_of_repeat";
    public static final String COUNT_OF_NUMBER_CURRENT_WORDS = "count_of_number_current_words";
    public static final String TYPE_OF_LEARN_WORDS = "type_of_learn_words";
    public static final String CURRENT_TABLE_NAME = "current_table_name";
    public static final String APP_PREFERENCES = "mySettings";


    /**
     * Числово необходимое для подсчёта нажатий кнопки назад
     * при первом нажатии появляется Toast с предупреждением
     * при втором нажатии выход на рабочий стол
     */
    private int exitCounter = 0;


    public static SharedPreferences getMySharedPreference() {
        return mySharedPreference;
    }

    public void loadSettings() {


        if (mySharedPreference == null) {
            mySharedPreference = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }

        if (mySharedPreference.contains(COUNT_OF_REPEAT)) {
            mainInterface.setCountOfRepeatWord(mySharedPreference.getInt(COUNT_OF_REPEAT, 0));
            mainInterface.setNumberOfCurrentLearnWords(mySharedPreference.getInt(COUNT_OF_NUMBER_CURRENT_WORDS, 0));
        }

        if (mySharedPreference.contains(TYPE_OF_LEARN_WORDS)) {
            mainInterface.setTypeOfLearn(mySharedPreference.getInt(TYPE_OF_LEARN_WORDS, 0));
        }

        if (mySharedPreference.contains(CURRENT_TABLE_NAME)){
            ProcessOfLearning.setCurrentTableNum(mySharedPreference.getInt(CURRENT_TABLE_NAME,0));
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        //Загружаем базу данных
        WordsDataBaseHelper.getWordsDataBaseHelper(this);




        //Создает экземпляр MainInterface
        MainInterface.getMainInterface(this);
        mainInterface = MainInterface.getMainInterface();
        ProcessOfLearning pl = ProcessOfLearning.getProcessOfLearning(this);
        loadSettings();


        //Подключение кнопок
        setOnClickButtons();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Button learnActivityButton = findViewById(R.id.learn_button);
        Button createActivityButton = findViewById(R.id.create_button);
        Button settingsActivityButton = findViewById(R.id.settings_button);

        final Animation hideToLeft = AnimationUtils.loadAnimation(this, R.anim.hide_to_left);
        final Animation hideToRight = AnimationUtils.loadAnimation(this, R.anim.hide_to_right);


        learnActivityButton.startAnimation(hideToLeft);
        createActivityButton.startAnimation(hideToRight);
        settingsActivityButton.startAnimation(hideToLeft);

    }


    @Override
    protected void onResume() {
        super.onResume();
        exitCounter = 0;
        Button learnActivityButton = findViewById(R.id.learn_button);
        Button createActivityButton = findViewById(R.id.create_button);
        Button settingsActivityButton = findViewById(R.id.settings_button);



        final Animation wrongAnim = AnimationUtils.loadAnimation(this, R.anim.wrong_answer_animation_button);
        learnActivityButton.startAnimation(wrongAnim);
        learnActivityButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.left_main_button_background,null));

        final Animation rightToLeft = AnimationUtils.loadAnimation(this, R.anim.from_right_to_left);
        final Animation leftToRight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);


        learnActivityButton.startAnimation(rightToLeft);
        createActivityButton.startAnimation(leftToRight);
        settingsActivityButton.startAnimation(rightToLeft);
    }

    public void setOnClickButtons() {
        Button learnActivityButton = findViewById(R.id.learn_button);
        Button createActivityButton = findViewById(R.id.create_button);
        Button settingsActivityButton = findViewById(R.id.settings_button);

        createActivityButton.setOnClickListener(this::startCreateActivity);
        learnActivityButton.setOnClickListener(this::startLearningActivity);
        settingsActivityButton.setOnClickListener(this::startSettingsActivity);
    }


    public void startSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void startCreateActivity(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void startLearningActivity(View view) {
        Intent intent = new Intent(this, LearnActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    public void onBackPressed() {
        if (exitCounter == 1) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            onDestroy();
            exitCounter = 0;
        } else {
            Toast exitToast = Toast.makeText(getApplicationContext(), R.string.press_one_more_time, Toast.LENGTH_SHORT);
            exitToast.show();
            exitCounter++;
        }

    }


}