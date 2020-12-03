package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    TextView txtHighScore;
    TextView txtTotalQuizQues,txtCorrectQues,txtWrongQues;

    Button btStartQuiz;
    Button btMainMenu;

    private int highScore;
    public static final String SHARED_PREFERRENCE = "shread_prefrence";
    public static final String SHARED_PREFERRENCE_HIGH_SCORE = "shread_prefrence_high_score";

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        btMainMenu = findViewById(R.id.result_bt_mainmenu);
        btStartQuiz = findViewById(R.id.result_bt_playAgain);
        txtHighScore = findViewById(R.id.result_text_High_Score);
        txtTotalQuizQues = findViewById(R.id.result_total_Ques);
        txtCorrectQues = findViewById(R.id.result_Correct_Ques);
        txtWrongQues = findViewById(R.id.result_Wrong_Ques);


        btMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ResultActivity.this,PlayActivity.class);
                startActivity(intent);
            }
        });

        btStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        loadHighScore();

        Intent intent = getIntent();

        int score = intent.getIntExtra("Score",0);
        int totalQuestion = intent.getIntExtra("Questions",0);
        int correctQues = intent.getIntExtra("Right",0);
        int wrongQues = intent.getIntExtra("Wrong",0);


        txtTotalQuizQues.setText("Questions: " + String.valueOf(totalQuestion));
        txtCorrectQues.setText("Right: " + String.valueOf(correctQues));
        txtWrongQues.setText("Wrong: " + String.valueOf(wrongQues));

        if (score > highScore){

            updatHighScore(score);
        }


    }

    private void updatHighScore(int newHighScore) {

        highScore = newHighScore;
        txtHighScore.setText("Score: " + String.valueOf(highScore));

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERRENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHARED_PREFERRENCE_HIGH_SCORE,highScore);
        editor.apply();


    }

    private void loadHighScore() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERRENCE,MODE_PRIVATE);
        highScore = sharedPreferences.getInt(SHARED_PREFERRENCE_HIGH_SCORE,0);
        txtHighScore.setText("Score: " + String.valueOf(highScore));

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){

            Intent intent = new Intent(ResultActivity.this,PlayActivity.class);
            startActivity(intent);

        }else {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        }
        backPressedTime = System.currentTimeMillis();
    }
}
