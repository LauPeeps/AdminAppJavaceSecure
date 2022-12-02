package com.example.javaceadminapp;

import static com.example.javaceadminapp.TopicActivity.moduleidfromtopicactivity;
import static com.example.javaceadminapp.TopicActivity.subidfromtopicactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ExerciseAddActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    Button submitExercise;
    EditText exerciseTitle, exerciseInstruction, exerciseProblem, answer1, answer2, answer3;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_add);

        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(ExerciseAddActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = findViewById(R.id.exercise_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Exercise");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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



        submitExercise = findViewById(R.id.submitExercise);

        submitExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExercise(exerciseTitle.getText().toString(), exerciseInstruction.getText().toString(), exerciseProblem.getText().toString(), answer1.getText().toString(),
                        answer2.getText().toString(), answer3.getText().toString());
            }
        });
    }

    private void addExercise(String title, String instruction, String problem, String answer1, String answer2, String answer3) {
        progressDialog.show();
        DocumentReference documentReference = firestore.collection("Quizzes").document(moduleidfromtopicactivity).collection(subidfromtopicactivity).document("Exercise_List");

        Map<String, Object> data = new HashMap<>();
        data.put("exercise_title", title);
        data.put("exercise_instruction", instruction);
        data.put("exercise_problem", problem);
        data.put("answer1", answer1);
        data.put("answer2", answer2);
        data.put("answer3", answer3);

        documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(ExerciseAddActivity.this, "Exercise Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ExerciseAddActivity.this, ExerciseActivity.class));
                finish();
            }
        });
    }
}