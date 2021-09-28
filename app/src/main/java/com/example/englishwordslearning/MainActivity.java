package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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
            mainInterface.setNumberOfCurrentLearnWords(mySharedPreference.getInt(COUNT_OF_NUMBER_CURRENT_WORDS,0));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




/*
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
*/



        //Подключение кнопок
        setOnClickButtons();
        //Загружаем базу данных
        //UserDataBaseHelper.getWordsDataBase(this);
         WordsDataBaseHelper.getWordsDataBase(this);



        //Создает экземпляр MainInterface
        MainInterface.getMainInterface(this);
        mainInterface = MainInterface.getMainInterface();
        loadSettings();

   }

    public void setOnClickButtons() {
        Button createActivityButton = findViewById(R.id.create_button);
        Button learnActivityButton = findViewById(R.id.learn_button);
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

    private void startSettingsActivity(View view) {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
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