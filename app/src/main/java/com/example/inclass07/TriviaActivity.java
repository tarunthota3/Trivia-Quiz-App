package com.example.inclass07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class TriviaActivity extends AppCompatActivity implements TriviaAdapter.InteractWithTriviaActivity {

    private static final String TAG = "demo";
    TextView questionId;
    TextView timeLimit;
    ImageView imageView;
    TextView question;
    ProgressBar progressBarForImage;

    Button buttonQuit;
    Button buttonNext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    static int id;
    static ArrayList<Trivia> data;
    ArrayList<ScoreData> scoreData = new ArrayList<>();
    static String STATS_KEY = "STATS";
    static String DATA_KEY = "DATA";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        setTitle("Trivia App");
        questionId = findViewById(R.id.questionId);
        timeLimit = findViewById(R.id.time_limit);
        imageView = findViewById(R.id.imageView);
        question = findViewById(R.id.question);
        recyclerView = findViewById(R.id.recyclerView);
        progressBarForImage = findViewById(R.id.progressBarForImage);

        buttonNext = findViewById(R.id.buttonNext);
        buttonQuit = findViewById(R.id.buttonQuit);
        id = 0;

        if(getIntent() != null && getIntent().getExtras() != null ){

            data = (ArrayList<Trivia>) getIntent().getExtras().getSerializable(MainActivity.TRIVIA_KEY);
//            for (Trivia t: data) {
//                Log.d(TAG, "data:::: " + t.toString());
//            }
            final Trivia trivia = data.get(id);
            questionId.setText("Q" +(Integer.parseInt(trivia.id) + 1) + "");
            //For a two minutes count down:
            new CountDownTimer(2*60*1000, 1000) {
                @Override
                public void onTick(long l) {
                    long minute = l/1000/60;
                    long second = (l - minute*60*1000)/1000;
//                    Log.d(TAG, "onTick: "+ minute+ " "+second);
                    timeLimit.setText("Time Left: " + minute + " : " + second);
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished timer");
                    calculateScore();
                }
            }.start();

            imageView.setVisibility(View.INVISIBLE);
            if(trivia.image != ""){
                Picasso.get().load(trivia.image).into(imageView);

                Picasso.get()
                        .load(trivia.image)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBarForImage.setVisibility(View.INVISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
            else{
                progressBarForImage.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            }
            question.setText(trivia.text);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new TriviaAdapter(trivia.choice, this);
            recyclerView.setAdapter(mAdapter);


            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d(TAG, "onClick: " + id + " " + (id < data.size()));
                    id = id + 1;
                    if(id < data.size()){
//                        Log.d(TAG, "IF");
                        Trivia trivia = data.get(id);
                        questionId.setText("Q" +(Integer.parseInt(trivia.id) + 1) + "");

                        progressBarForImage.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                        if(trivia.image != ""){
                            Picasso.get().load(trivia.image).into(imageView);

                            Picasso.get()
                                    .load(trivia.image)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressBarForImage.setVisibility(View.INVISIBLE);
                                            imageView.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                        }
                        else{
                            progressBarForImage.setVisibility(View.INVISIBLE);
                            imageView.setVisibility(View.INVISIBLE);
                        }
                        question.setText(trivia.text);
//                        Log.d(TAG, "choices: " + Arrays.toString(trivia.choice));

                        mAdapter = new TriviaAdapter(trivia.choice, TriviaActivity.this);
                        recyclerView.setAdapter(mAdapter);
                    }
                    else{
//                        Log.d(TAG, "ELSE");
                        calculateScore();
                    }

                }
            });

            buttonQuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void calculateScore() {
        int finalScore = 0;
        for (ScoreData sd: scoreData) {
            if(sd.score == true){
                Log.d(TAG, "true");
                finalScore = finalScore + 1;
            }
        }
        int percentage = (finalScore * 100) /data.size();
        Log.d(TAG, "finalScore: " + data.size());
        Intent intent = new Intent(TriviaActivity.this,StatsActivity.class);
        intent.putExtra(STATS_KEY, percentage);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ARRAYLIST",(Serializable)data);
        intent.putExtra("BUNDLE",bundle);
        startActivity(intent);
        startActivity(intent);
    }

    @Override
    public void getSelectedOption(int position, String s) {
        Log.d(TAG, "getSelectedOption first: " + scoreData.size());
        Log.d(TAG, "Selected Question: " + data.get(id));
        Log.d(TAG, "selected choice: " + s);
        Trivia trivia = data.get(id);
        String selectedAnswer = s;
        int temp = 0;
        for (ScoreData sd : scoreData){
            if(sd.questionId == trivia.id){
                temp = 1;
            }
        }
        if(temp == 1){
            Log.d(TAG, "id present" );
            for(ScoreData sd: scoreData) {
                if(sd.questionId == trivia.id){
                    if(trivia.choice[Integer.parseInt(trivia.answer) - 1] == s){
                        sd.score = true;
                    }
                    else{
                        sd.score = false;
                    }
                }
            }
        }
        else{
            Log.d(TAG, "id not present");
            ScoreData sd = new ScoreData();
            sd.questionId = trivia.id;
            Log.d(TAG, "getSelectedOption: " + trivia.answer);
            if(trivia.choice[Integer.parseInt(trivia.answer) - 1] == s){
                sd.score = true;
            }
            else{
                sd.score = false;
            }
            scoreData.add(sd);
        }
        Log.d(TAG, "scoredata length: " + scoreData.size());
    }
}
