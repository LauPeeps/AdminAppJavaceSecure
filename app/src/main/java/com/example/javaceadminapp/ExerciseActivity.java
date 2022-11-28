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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExerciseActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    Dialog progressDialog;
    EditText title, direction, correctContent, score;
    Button addExercise;
    String eId, exerciseTitle, exerciseDesc, exerciseContent, exerciseScore;
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
        direction = findViewById(R.id.exerciseDirection);
        direction.setMovementMethod(new ScrollingMovementMethod());
        correctContent = findViewById(R.id.exerciseContent);
        correctContent.setMovementMethod(new ScrollingMovementMethod());
        score = findViewById(R.id.exerciseScore);

        addExercise = findViewById(R.id.addExercise);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            addExercise.setText("Update Exercise");
            eId = bundle.getString("eId");
            exerciseTitle = bundle.getString("title");
            exerciseDesc = bundle.getString("instruction");
            exerciseContent = bundle.getString("content");
            exerciseScore = bundle.getString("score");


            title.setText(exerciseTitle);
            direction.setText(exerciseDesc);
            correctContent.setText(exerciseContent);
            score.setText(exerciseScore);
        } else {
            addExercise.setText("Add Exercise");
        }

        firestore = FirebaseFirestore.getInstance();

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null) {
                    String eid = eId;
                    String uTitle = title.getText().toString();
                    String uDesc = direction.getText().toString();
                    String uContent = correctContent.getText().toString();
                    String uScore = score.getText().toString();

                    updateExercise(eid, uTitle, uDesc, uContent, uScore);
                } else {
                    if (title.getText().toString().isEmpty()) {
                        title.setError("Exercise title is needed");
                        return;
                    } if (correctContent.getText().toString().isEmpty()) {
                        correctContent.setError("Content is needed");
                        return;
                    } if (direction.getText().toString().isEmpty()) {
                        direction.setError("Instruction is needed");
                        return;
                    }if (score.getText().toString().isEmpty()) {
                        score.setError("Score is needed");
                        return;
                    }
                    addExercise(title.getText().toString(), direction.getText().toString(), correctContent.getText().toString(), score.getText().toString());
                    title.getText().clear();
                    direction.getText().clear();
                    correctContent.getText().clear();
                    score.getText().clear();
                }
            }
        });
    }
    private void updateExercise(String eid, String title, String instruction, String content, String score) {
        progressDialog.show();

        firestore.collection("Exercises").document(eid).update("exercise_title", title, "exercise_instruction"
        , instruction, "exercise_content", content, "exercise_score", score).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(ExerciseActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ExerciseActivity.this, "Unable to update exercise", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addExercise(String title, String instruction, String content, String score) {
        progressDialog.show();

        DocumentReference documentReference = firestore.collection("Exercises").document();

        String eId = documentReference.getId();

        Map<String, Object> exercises_data = new HashMap<>();
        exercises_data.put("eId", eId);
        exercises_data.put("exercise_title", title);
        exercises_data.put("exercise_instruction", instruction);
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