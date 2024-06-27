package com.example.random_number_generator.activities;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import static com.example.random_number_generator.lib.Utils.showInfoDialog;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.random_number_generator.R;
import com.example.random_number_generator.lib.Utils;
import com.example.random_number_generator.models.RandomNumber;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.example.random_number_generator.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;
    private final String mKEY = "KEY";
    private EditText mFromEditText;
    private EditText mToEditText;
    private TextView mResult;
    private int currentRandomNumber;
    private Snackbar mSnackbar;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        setupToolbar();
        setupFAB();

        // setup Fields:
        mRandomNumber = new RandomNumber();
        initializeHistoryList(savedInstanceState, mKEY);
        mFromEditText = findViewById(R.id.from_et_text);
        mToEditText = findViewById(R.id.to_et_text);
        mResult = findViewById(R.id.tv_result);
    }

    private void initializeHistoryList (Bundle savedInstanceState, String key)
    {
        if (savedInstanceState != null) {
            mNumberHistory = savedInstanceState.getIntegerArrayList (key);
        }
        else {
            String history = getDefaultSharedPreferences (this).getString (key, null);
            mNumberHistory = history == null ?
                    new ArrayList<> () : Utils.getNumberListFromJSONString (history);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(mKEY, mNumberHistory);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveHistoryToSharedPrefs();
    }

    private void saveHistoryToSharedPrefs() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        editor.putString(mKEY, Utils.getJSONStringFromNumberList(mNumberHistory));
        editor.apply();
    }

    // can delete this:
    private void saveOrDeleteGameInSharedPrefs() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        editor.putString(mKEY, Utils.getJSONStringFromNumberList(mNumberHistory));
        editor.apply();

        // Save current game or remove any prior game to/from default shared preferences
        /*if (mUseAutoSave) {
            editor.putString(mKEY_GAME, mGame.getJSONFromCurrentGame());
        } else {
            editor.remove(mKEY_GAME);
        }

        editor.apply();*/
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick (View v) {
                handleFABClick(v);
            }
        });
    }

    private void handleFABClick(View v) {
        try
        {
            int fromData = Integer.parseInt(mFromEditText.getText().toString());
            int toData = Integer.parseInt(mToEditText.getText().toString());
            mRandomNumber.setFromTo(fromData, toData);

            currentRandomNumber = mRandomNumber.getCurrentRandomNumber();
            mNumberHistory.add(currentRandomNumber);
            mResult.setText(String.valueOf(currentRandomNumber));
            mResult.setVisibility(View.VISIBLE);
            saveHistoryToSharedPrefs();
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    R.string.error_msg,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_history) {
            showHistory();
            return true;
        } else if (id == R.id.action_clear_history)
        {
            clearHistory();
            return true;
        } else if (id == R.id.action_about) 
        {
            showAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAbout() {
        mSnackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.about_text),
                Snackbar.LENGTH_LONG);
        mSnackbar.show();
    }

    private void showHistory() {
        Utils.showInfoDialog (MainActivity.this,
                getString(R.string.history_title), mNumberHistory.toString());
    }

    private void clearHistory() {
        mNumberHistory.clear();
        saveHistoryToSharedPrefs();
        Toast.makeText(this, getString(R.string.history_cleared_message), Toast.LENGTH_SHORT).show();
    }


}