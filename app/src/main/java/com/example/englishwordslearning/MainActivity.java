package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

//import com.example.englishwordslearning.database.DatabaseHelper;
//import com.example.englishwordslearning.database.UserDataBaseHelper;
import com.example.englishwordslearning.database.WordsDataBaseHelper;
import com.example.englishwordslearning.logik.MainInterface;

public class MainActivity extends AppCompatActivity {

    private MainInterface mainInterface;

    private static SharedPreferences mySharedPreference = null;
    public static final String COUNT_OF_REPEAT = "count_of_repeat";
    public static final String COUNT_OF_NUMBER_CURRENT_WORDS = "count_of_number_current_words";
    public static final String TYPE_OF_LEARN_WORDS = "type_of_learn_words";
    public static final String APP_PREFERENCES = "mySettings";


    private WordsDataBaseHelper mDBHelper;
    private SQLiteDatabase mDb;


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

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //  createActivityButton.startAnimation(animUp);
        //Подключение кнопок
        setOnClickButtons();
        //Загружаем базу данных
        //UserDataBaseHelper.getWordsDataBase(this);
        WordsDataBaseHelper.getWordsDataBaseHelper(this);


        //Создает экземпляр MainInterface
        MainInterface.getMainInterface(this);
        mainInterface = MainInterface.getMainInterface();
        loadSettings();



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
       Button learnActivityButton = findViewById(R.id.learn_button);
       Button createActivityButton = findViewById(R.id.create_button);
       Button settingsActivityButton = findViewById(R.id.settings_button);

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
        settingsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettingsActivity(view);
            }
        });


    }



    public void startSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingActivity.class);

       Button settingsActivityButton = findViewById(R.id.settings_button);
        Button learnActivityButton = findViewById(R.id.learn_button);
        Button createActivityButton = findViewById(R.id.create_button);
       Animation alfa = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

      //  createActivityButton.startAnimation(alfa);
      //  learnActivityButton.startAnimation(alfa);
      //  settingsActivityButton.startAnimation(alfa);
        startActivity(intent);
        overridePendingTransition( R.anim.fade_in,R.anim.fade_out);


      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 100);
*/

    }

    public void startCreateActivity(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
        overridePendingTransition( R.anim.fade_in,R.anim.fade_out);

    }

    public void startLearningActivity(View view) {
        Intent intent = new Intent(this, LearnActivity.class);
        startActivity(intent);
        overridePendingTransition( R.anim.fade_in,R.anim.fade_out);
    }
}