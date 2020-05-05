package com.example.inclass07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private static final String TAG = "demo";
    TextView textViewPercentage;
    ProgressBar progressBarPercentage;
    static String TRIVIA_KEY = "TRIVIA";
    Button buttonTryAgain;
    Button buttonQuit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        setTitle("Trivia App");
        textViewPercentage = findViewById(R.id.textViewPercentage);
        progressBarPercentage = findViewById(R.id.progressBarPercentage);
        buttonTryAgain = findViewById(R.id.buttonTryAgain);
        buttonQuit = findViewById(R.id.buttonQuit);

        if(getIntent() != null && getIntent().getExtras() != null ){
            int percentage = getIntent().getExtras().getInt(TriviaActivity.STATS_KEY);
            Bundle args = getIntent().getBundleExtra("BUNDLE");
            final ArrayList<Trivia> data = (ArrayList<Trivia>) args.getSerializable("ARRAYLIST");

            Log.d(TAG, "percentage: " + percentage);
            textViewPercentage.setText( percentage + "%");
            progressBarPercentage.setProgress(percentage);

            buttonQuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StatsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            buttonTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StatsActivity.this,TriviaActivity.class);
                    intent.putExtra(TRIVIA_KEY, data);
                    startActivity(intent);
                }
            });
        }
    }
}
