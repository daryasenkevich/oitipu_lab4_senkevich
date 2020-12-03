package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizapp.QuizContract.*;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyQuizz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);

    }


    private void fillQuestionsTable() {

        Questions q1 = new Questions("Автомобильный регистрационный знак Швейцарии - это ?", "S", "CH", "A", "SCH", 2);
        addQuestions(q1);
        Questions q2 = new Questions("С помощью какой шкалы измеряют силу землетрясения ?", "шкала Бофорта", "Шкала Кельвина", "Шкала Рихтера", "Шкала Реомюра", 3);
        addQuestions(q2);
        Questions q3 = new Questions("Какая река имеет наибольшее количество воды на земле ?", "Волга", "Миссисипи", "Дунай", "Амазонка", 4);
        addQuestions(q3);
        Questions q4 = new Questions("На логотипе какого автомобиля изображен бык ?", "Porsche", "Ferarri", "Lamborghini", "Aston Martin", 3);
        addQuestions(q4);
        Questions q5 = new Questions("В каком году образовалась группа 'The Beatles' ?", "1959", "1969", "1979", "1989", 1);
        addQuestions(q5);
        Questions q6 = new Questions("В каком фильме звучала песня 'Три белых коня' ?", "Приключения Электроника", "Чародеи", "Служебный роман", "Ирония судьбы",2);
        addQuestions(q6);
        Questions q7 = new Questions("Каким был рост Наполеона ?", "1,58 м", "1,75 м", "1,68 м", "1,82 м", 3);
        addQuestions(q7);
        Questions q8 = new Questions("В каком городе протекает река Темза ?", "Лондон", "Милан", "Париж", "Будапешт", 1);
        addQuestions(q8);
        Questions q9 = new Questions("Режиссер фильма Интерстеллар?", "Джордж Лукас", "Кристофер Нолан", "Стивен Спилберг", "Дэвид Финчер", 2);
        addQuestions(q9);
        Questions q10 = new Questions("Кто был президентом США в 2000 году ?", "Барак Обама", "Джон Кеннеди", "Билл Клинтон", "Джордж Буш", 3);
        addQuestions(q10);
    }

    private void addQuestions(Questions question) {

        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuestionTable.TABLE_NAME, null, cv);

    }

    public ArrayList<Questions> getAllQuestions() {

        ArrayList<Questions> questionList = new ArrayList<>();
        db = getReadableDatabase();


        String Projection[] = {

                QuestionTable._ID,
                QuestionTable.COLUMN_QUESTION,
                QuestionTable.COLUMN_OPTION1,
                QuestionTable.COLUMN_OPTION2,
                QuestionTable.COLUMN_OPTION3,
                QuestionTable.COLUMN_OPTION4,
                QuestionTable.COLUMN_ANSWER_NR
        };


        Cursor c = db.query(QuestionTable.TABLE_NAME,
                Projection,
                null,
                null,
                null,
                null,
                null);


        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));

                questionList.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionList;

    }

}


