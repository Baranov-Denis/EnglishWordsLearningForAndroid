package com.example.englishwordslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.englishwordslearning.database.WordsDataBase;
import com.example.englishwordslearning.logik.MainInterface;
import com.example.englishwordslearning.logik.ProcessOfLearning;

public class CreateActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private Cursor wordCursor;
    private SimpleCursorAdapter cursorAdapter;

    private MainInterface mainInterface;
    //Идентификационный номер слова из общего списка слов после нажатия на него в списке
    private long selectedWord;

    public long getSelectedWord() {
        return selectedWord;
    }

    public void setSelectedWord(long selectedWord) {
        this.selectedWord = selectedWord;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //wordsDataBase = new WordsDataBase(this);
        mainInterface = MainInterface.getMainInterface(this);
        database = mainInterface.getSQLiteDatabase();
        createWordsList();
        setOnClickForButtons();

    }


    private void setOnClickForButtons() {
        Button saveButton = (Button) findViewById(R.id.button_for_save_word);
        Button deleteButton = (Button) findViewById(R.id.button_for_delete_word);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWord(view);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteWord(view);
            }
        });
    }

    /**
     * Запускаем сохранение слова
     *
     * @param view
     */
    private void saveWord(View view) {
        //Получаем EditText для Английского слова
        EditText englishWord = (EditText) findViewById(R.id.edit_text_english_word);
        //Получаем String из поля
        String newEnglishWord = englishWord.getText().toString();
        //Тоже самое для русского слова
        EditText newRussianWord = (EditText) findViewById(R.id.edit_text_russian_word);
        String russian = newRussianWord.getText().toString();

        //Вызываем сохранение метода в базу данных
        mainInterface.addNewWord(newEnglishWord, russian);

        // Этот код обновляет текущую activity без анимации
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void deleteWord(View view) {
        mainInterface.deleteCurrentWord(getSelectedWord());
        // Этот код обновляет текущую activity без анимации
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    /**
     * // Метод для создания списка слов
     */
    private void createWordsList() {
        ListView wordsList = (ListView) findViewById(R.id.list_of_words);
        // SQLiteOpenHelper helper = new WordsDataBase(this);
        try {
            //database = helper.getReadableDatabase();
            wordCursor = database.query("DICTIONARY", new String[]{"_id", "ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT"}, null, null, null, null, "ENGLISH_WORD");
            cursorAdapter = new SimpleCursorAdapter(this, R.layout.list, wordCursor,
                    new String[]{"ENGLISH_WORD", "RUSSIAN_WORD", "RIGHT_ANSWER_COUNT"}, new int[]{R.id.text1, R.id.text2, R.id.text3}, 0);
            wordsList.setAdapter(cursorAdapter);

        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Error", Toast.LENGTH_LONG);
            toast.show();
        }

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectedWord(id);
            }
        };

        wordsList.setOnItemClickListener(listener);
    }


}