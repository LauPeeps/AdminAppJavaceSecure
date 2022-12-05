package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Feedback extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<FeedbackModel> feedbackModels;
    FeedbackAdapter feedbackAdapter;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Feedback");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching feedbacks data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        recyclerView = findViewById(R.id.feedbackRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();
        feedbackModels = new ArrayList<FeedbackModel>();
        feedbackAdapter = new FeedbackAdapter(Feedback.this, feedbackModels);

        recyclerView.setAdapter(feedbackAdapter);

        fetchLeaderboard();
    }


    private void fetchLeaderboard() {
        progressDialog.show();
        firestore.collection("Feedbacks").orderBy("feedback_created").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                feedbackModels.clear();
                progressDialog.dismiss();

                for (DocumentSnapshot documentSnapshot: task.getResult()) {
                    FeedbackModel feedbackModel = new FeedbackModel(documentSnapshot.getString("feedback_id"),
                            documentSnapshot.getString("feedback_user"),
                            documentSnapshot.getString("feedback_title"),
                            documentSnapshot.getString("feedback_message"),
                            documentSnapshot.getTimestamp("feedback_created"));
                    feedbackModels.add(feedbackModel);
                }
                feedbackAdapter = new FeedbackAdapter(Feedback.this, feedbackModels);

                recyclerView.setAdapter(feedbackAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Feedback.this, "No feedback data exist!", Toast.LENGTH_SHORT).show();
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