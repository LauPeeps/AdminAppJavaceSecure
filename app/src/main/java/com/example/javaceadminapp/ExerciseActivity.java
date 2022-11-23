package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExerciseActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    Dialog progressDialog;
    EditText title, correctContent, score;
    Button addExercise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Exercises");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new Dialog(ExerciseActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        title = findViewById(R.id.exerciseTitle);
        correctContent = findViewById(R.id.exerciseContent);
        correctContent.setMovementMethod(new ScrollingMovementMethod());
        score = findViewById(R.id.exerciseScore);

        addExercise = findViewById(R.id.addExercise);


        firestore = FirebaseFirestore.getInstance();

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Exercise title is needed");
                    return;
                } if (correctContent.getText().toString().isEmpty()) {
                    correctContent.setError("Content is needed");
                    return;
                } if (score.getText().toString().isEmpty()) {
                    score.setError("Score is needed");
                    return;
                }
                addExercise(title.getText().toString(), correctContent.getText().toString(), score.getText().toString());
                title.getText().clear();
                correctContent.getText().clear();
                score.getText().clear();
            }
        });

    }

    private void addExercise(String title, String content, String score) {
        progressDialog.show();

        DocumentReference documentReference = firestore.collection("Exercises").document();

        String eId = documentReference.getId();

        Map<String, Object> exercises_data = new HashMap<>();
        exercises_data.put("eId", eId);
        exercises_data.put("exercise_title", title);
        exercises_data.put("exercise_content", content);
        exercises_data.put("exercise_score", score);

        documentReference.set(exercises_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(ExerciseActivity.this, "Successfully added the exercise", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ExerciseActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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