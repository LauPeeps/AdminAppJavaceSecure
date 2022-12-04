package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProgressUser extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Dialog progressDialog;
    String userId, userFullName;
    List<ProgressUserModel> progressUserModelList = new ArrayList<>();
    ProgressUserAdapter progressUserAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_user);

        firestore = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("uid");
        userFullName = bundle.getString("fullname");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(userFullName + "'s Progress");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(ProgressUser.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fetchUserProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserProgress();
    }

    private void fetchUserProgress() {
        progressDialog.show();

        firestore.collection("Users").document(userId).collection(userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressUserModelList.clear();
                progressDialog.dismiss();

                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    ProgressUserModel progressUserModel = new ProgressUserModel(documentSnapshot.getString("module_id"),
                            documentSnapshot.getString("module_name"),
                            documentSnapshot.getLong("progress"));
                    progressUserModelList.add(progressUserModel);
                }
                progressUserAdapter = new ProgressUserAdapter(ProgressUser.this, progressUserModelList);

                recyclerView.setAdapter(progressUserAdapter);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}