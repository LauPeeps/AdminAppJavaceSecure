package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ExerciseActivity extends AppCompatActivity {

    TextView exerciseTitle, exerciseInstruction, exerciseProblem, answer1, answer2, answer3;
    Button showAddExerciseActivity;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Toolbar toolbar = findViewById(R.id.exercise_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Exercise");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(ExerciseActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        exerciseTitle = findViewById(R.id.exerciseTitle);
        exerciseInstruction = findViewById(R.id.exerciseInstruction);
        exerciseProblem = findViewById(R.id.exerciseProblem);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);

        exerciseTitle.setMovementMethod(new ScrollingMovementMethod());
        exerciseInstruction.setMovementMethod(new ScrollingMovementMethod());
        exerciseProblem.setMovementMethod(new ScrollingMovementMethod());
        answer1.setMovementMethod(new ScrollingMovementMethod());
        answer2.setMovementMethod(new ScrollingMovementMethod());
        answer3.setMovementMethod(new ScrollingMovementMethod());



        showAddExerciseActivity = findViewById(R.id.showAddExerciseActivity);

        showAddExerciseActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExerciseActivity.this, ExerciseAddActivity.class));
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}