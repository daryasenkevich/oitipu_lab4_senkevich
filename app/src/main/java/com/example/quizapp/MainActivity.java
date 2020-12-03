package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;

    private ArrayList<Questions> questionList;
    private int questionCounter;
    private int questionTotalCount;
    private Questions currentQuestions;
    private boolean answered;

    private final Handler handler = new Handler();

    private int correctAns = 0, wrongAns = 0;

    private TimerDialog timerDialog;
    private CorrectDialog correctDialog;
    private WrongDialog wrongDialog;

    int score = 0;

    private int totalSizeofQuiz = 0;

    private static final long COUNTDOWN_IN_MILLIS = 20000;
    private CountDownTimer countDownTimer;
    private long timeleftinMillis;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setupUI();
        fetchDB();

        timerDialog = new TimerDialog(this);
        correctDialog = new CorrectDialog(this);
        wrongDialog = new WrongDialog(this);
    }

    private void setupUI() {
        textViewQuestion = findViewById(R.id.txtQuestionContainer);

        textViewScore = findViewById(R.id.txtScore);
        textViewQuestionCount = findViewById(R.id.txtTotalQuestion);
        textViewCountDown = findViewById(R.id.txtViewTimer);

        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button);
    }

    public void fetchDB() {
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();
        startQuiz();
    }

    public void startQuiz() {

        questionTotalCount = questionList.size();
        Collections.shuffle(questionList);

        showQuestions();   // calling showQuestion() method

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.radio_button1:
                        rb1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        rb2.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.BLACK);

                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn_press));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        break;

                    case R.id.radio_button2:
                        rb2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        rb1.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.BLACK);

                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn_press));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        break;

                    case R.id.radio_button3:
                        rb3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        rb2.setTextColor(Color.BLACK);
                        rb1.setTextColor(Color.BLACK);
                        rb4.setTextColor(Color.BLACK);

                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn_press));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        break;

                    case R.id.radio_button4:
                        rb4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        rb2.setTextColor(Color.BLACK);
                        rb3.setTextColor(Color.BLACK);
                        rb1.setTextColor(Color.BLACK);

                        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn_press));
                        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
                        break;
                }
            }
        });

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {

                        quizOperation();
                    } else {

                        Toast.makeText(MainActivity.this, "Please choose the answer", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void showQuestions() {

        rbGroup.clearCheck();

        rb1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
        rb2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
        rb3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));
        rb4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_radio_btn));

        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);

        if (questionCounter < questionTotalCount) {
            currentQuestions = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestions.getQuestion());
            rb1.setText(currentQuestions.getOption1());
            rb2.setText(currentQuestions.getOption2());
            rb3.setText(currentQuestions.getOption3());
            rb4.setText(currentQuestions.getOption4());

            questionCounter++;
            answered = false;
            buttonConfirmNext.setText("Accept");

            textViewQuestionCount.setText("Questions: " + questionCounter + "/" + questionTotalCount);

            timeleftinMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {

            // If Number of Questions Finishes then we need to finish the Quiz and Shows the User Quiz Performance

            Toast.makeText(this, "Finished", Toast.LENGTH_SHORT).show();

            rb1.setClickable(false);
            rb2.setClickable(false);
            rb3.setClickable(false);
            rb4.setClickable(false);
            buttonConfirmNext.setClickable(false);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    finalResult();

                }
            }, 2000);
        }
    }

    private void quizOperation() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbselected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbselected) + 1;

        checkSolution(answerNr, rbselected);

    }


    private void checkSolution(int answerNr, RadioButton rbselected) {

        switch (currentQuestions.getAnswerNr()) {
            case 1:
                if (currentQuestions.getAnswerNr() == answerNr) {
                    correctAns++;
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);

                } else {
                    wrongAns++;

                    String correctAnswer = (String) rb1.getText();
                    wrongDialog.wrongDialog(correctAnswer, this);

                }
                break;
            case 2:
                if (currentQuestions.getAnswerNr() == answerNr) {
                    correctAns++;
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);
                } else {
                    wrongAns++;

                    String correctAnswer = (String) rb2.getText();
                    wrongDialog.wrongDialog(correctAnswer, this);
                }
                break;
            case 3:
                if (currentQuestions.getAnswerNr() == answerNr) {
                    correctAns++;
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);
                } else {
                    wrongAns++;

                    String correctAnswer = (String) rb3.getText();
                    wrongDialog.wrongDialog(correctAnswer, this);
                }
                break;
            case 4:
                if (currentQuestions.getAnswerNr() == answerNr) {
                    correctAns++;
                    score += 10;
                    textViewScore.setText("Score: " + String.valueOf(score));
                    correctDialog.correctDialog(score, this);
                } else {
                    wrongAns++;
                    String correctAnswer = (String) rb4.getText();
                    wrongDialog.wrongDialog(correctAnswer, this);
                }
                break;
        }
        if (questionCounter == questionTotalCount) {
            buttonConfirmNext.setText("Confirm and finish");
        }
    }

    //  the timer code

    private void startCountDown() {

        countDownTimer = new CountDownTimer(timeleftinMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleftinMillis = millisUntilFinished;

                updateCountDownText();
            }

            @Override
            public void onFinish() {

                timeleftinMillis = 0;
                updateCountDownText();

            }
        }.start();
    }


    private void updateCountDownText() {

        int minutes = (int) (timeleftinMillis / 1000) / 60;
        int seconds = (int) (timeleftinMillis / 1000) % 60;

        //  String timeFormatted = String.format(Locale.getDefault(),"02d:%02d",minutes,seconds);

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);

        if (timeleftinMillis < 10000) {
            textViewCountDown.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else {
            textViewCountDown.setTextColor(ContextCompat.getColor(this, R.color.white));
        }


        if (timeleftinMillis == 0) {

            Toast.makeText(this, "Time is over!", Toast.LENGTH_SHORT).show();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    timerDialog.timerDialog();

                }
            }, 2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Log.i("BUGBUG", "onDestroy() in QuizActivity");
    }

    private void finalResult() {

        Intent resultData = new Intent(MainActivity.this, ResultActivity.class);

        resultData.putExtra("Player score", score);
        resultData.putExtra("Questions", questionTotalCount);
        resultData.putExtra("Right", correctAns);
        resultData.putExtra("Wrong", wrongAns);
        startActivity(resultData);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {

            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        }
        backPressedTime = System.currentTimeMillis();
    }
}