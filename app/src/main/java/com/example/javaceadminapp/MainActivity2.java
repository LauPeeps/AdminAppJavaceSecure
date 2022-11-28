package com.example.javaceadminapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class MainActivity2 extends AppCompatActivity{
    Dialog progressDialog;
    FirebaseFirestore firestore;
    TextView adminNameText;
    ImageView imgLogout, imgAdmins, imgStudents, imgQuiz, imgResources, imgExercises, imgFeedback, imgForum;
    FirebaseAuth firebaseAuth;
    String currentAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new Dialog(MainActivity2.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imgLogout = findViewById(R.id.logoutBtn);
        imgAdmins = findViewById(R.id.goToAdmin);
        imgStudents = findViewById(R.id.goToStudents);
        imgQuiz = findViewById(R.id.goToQuiz);
        imgResources = findViewById(R.id.goToResources);
        imgExercises = findViewById(R.id.goToLeaderboard);
        imgFeedback = findViewById(R.id.goToFeedback);
        imgForum = findViewById(R.id.goToForum);

        adminNameText = findViewById(R.id.adminName);


        imgAdmins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity2.this, Admin.class);
            }
        });
        imgStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity2.this, Student.class);

            }
        });
        imgQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity2.this, Module.class);

            }
        });
        imgResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity2.this, Resources.class);

            }
        });
        imgExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity2.this, Leaderboard.class);

            }
        });
        imgFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity2.this, Feedback.class);

            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
                firebaseAuth.signOut();
            }
        });

        imgForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(MainActivity2.this, Forum.class);
            }
        });


        currentAdmin = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("Admins").document(currentAdmin);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                adminNameText.setText(value.getString("name"));
            }
        });


    }

    public void redirectActivity(Activity activity, Class pointClass) {

        Intent intent = new Intent(activity,pointClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

}