package com.example.javaceadminapp;

import static com.example.javaceadminapp.TopicActivity.moduleidfromtopicactivity;
import static com.example.javaceadminapp.TopicActivity.subidfromtopicactivity;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExerciseActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
                firestore.collection("Quizzes").document(moduleidfromtopicactivity).collection(subidfromtopicactivity).document("Exercise_List").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Intent intent = new Intent(ExerciseActivity.this, ExerciseAddActivity.class);
                            String title = documentSnapshot.getString("exercise_title");
                            String instruction = documentSnapshot.getString("exercise_instruction");
                            String problem = documentSnapshot.getString("exercise_problem");
                            String answerCode1 = documentSnapshot.getString("answer1");
                            String answerCode2 = documentSnapshot.getString("answer2");
                            String answerCode3 = documentSnapshot.getString("answer3");

                            intent.putExtra("title", title);
                            intent.putExtra("instruction", instruction);
                            intent.putExtra("problem", problem);
                            intent.putExtra("answerCode1", answerCode1);
                            intent.putExtra("answerCode2", answerCode2);
                            intent.putExtra("answerCode3", answerCode3);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(ExerciseActivity.this, ExerciseAddActivity.class));
                        }
                    }
                });
            }
        });

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Quizzes").document(moduleidfromtopicactivity).collection(subidfromtopicactivity).document("Exercise_List").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    String title = documentSnapshot.getString("exercise_title");
                    String instruction = documentSnapshot.getString("exercise_instruction");
                    String problem = documentSnapshot.getString("exercise_problem");
                    String answerCode1 = documentSnapshot.getString("answer1");
                    String answerCode2 = documentSnapshot.getString("answer2");
                    String answerCode3 = documentSnapshot.getString("answer3");

                    exerciseTitle.setText(title);
                    exerciseInstruction.setText(instruction);
                    exerciseProblem.setText(problem);
                    answer1.setText("Correct code 1 = " + answerCode1);
                    answer2.setText("Correct code 2 = " + answerCode2);
                    answer3.setText("Correct code 3 = " + answerCode3);
                }
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