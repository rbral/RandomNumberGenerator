package com.example.random_number_generator.activities;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;
    private final String mKEY = "KEY";


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

        outState.putString(mKEY, mNumberHistory.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveOrDeleteGameInSharedPrefs();
    }

    private void saveOrDeleteGameInSharedPrefs() {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();

        editor.putString(mKEY, mNumberHistory.toString());

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

    private static void handleFABClick(View v) {
        Snackbar.make (v, "Hello, World!",
                        Snackbar.LENGTH_LONG)
                .setAnchorView (R.id.fab)
                .setAction ("Action", null)
                .show ();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}