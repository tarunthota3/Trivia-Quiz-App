/*
Assignment: In Class Assignment 07
File name: MainActivity.java
Full name:
Akhil Madhamshetty-801165622
Tarun thota-801164383
 */
package com.example.inclass07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "demo";
    static String TRIVIA_KEY = "TRIVIA";
    ImageView imageView;
    TextView textViewTriviaReady;
    ProgressBar progressBar;
    Button buttonExit;
    Button buttonStartTrivia;
    String baseURL = "http://dev.theappsdr.com/apis/trivia_json/index.php";
    ArrayList<Trivia> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Trivia App");
        imageView = findViewById(R.id.imageViewTrivia);
        textViewTriviaReady = findViewById(R.id.textViewTriviaReady);
        buttonExit = findViewById(R.id.buttonExit);
        buttonStartTrivia = findViewById(R.id.buttonStartTrivia);
        progressBar = findViewById(R.id.progressBar);

        new GetQuestions().execute();

        buttonStartTrivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TriviaActivity.class);
                intent.putExtra(TRIVIA_KEY, data);
                startActivity(intent);
            }
        });
    }


    class GetQuestions extends AsyncTask<Void, Void, ArrayList<Trivia>>{

        @Override
        protected ArrayList<Trivia> doInBackground(Void... voids) {

            HttpURLConnection connection = null;
            URL url1 = null;
            String url = baseURL;
            try {
                url1 = new URL(url);
                connection = (HttpURLConnection) url1.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray questions = root.getJSONArray("questions");


//                    Log.d(TAG, "doInBackground: " + questions);
                    for(int  i = 0; i < questions.length(); i++){
                        JSONObject questionJSON = questions.getJSONObject(i);

                        String id = questionJSON.getString("id");
                        String text = questionJSON.getString("text");
                        String image = "";
                        if( !questionJSON.has("image") || questionJSON.getString("image") == null || questionJSON.getString("image") == ""){
                            image = "";
                        }
                        else{
                            image = questionJSON.getString("image");
                        }

                        JSONObject choicesJSON = questionJSON.getJSONObject("choices");

                        JSONArray choiceJSONArray = choicesJSON.getJSONArray("choice");
                        String choice[] = new String[choiceJSONArray.length()];
                        for(int j = 0; j < choiceJSONArray.length(); j++){
                            choice[j] = choiceJSONArray.getString(j);
                        }
                        String answer = choicesJSON.getString("answer");
                        Trivia trivia = new Trivia();
                        trivia.id = id;
                        trivia.text = text;
                        trivia.image = image;
                        trivia.choice = choice;
                        trivia.answer = answer;
                        data.add(trivia);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return data;
        }
            @Override
        protected void onPostExecute(ArrayList<Trivia> trivias) {
            super.onPostExecute(trivias);

            progressBar.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            textViewTriviaReady.setVisibility(View.VISIBLE);
            buttonStartTrivia.setEnabled(true);
        }
    }
}
