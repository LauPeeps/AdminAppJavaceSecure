package com.example.javaceadminapp;


import static com.example.javaceadminapp.TopicActivity.moduleidfromtopicactivity;
import static com.example.javaceadminapp.TopicActivity.subidfromtopicactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Questions extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addQuestionBtn;
    public static List<QuestionsModel> questionsModelList = new ArrayList<>();
    QuestionsAdapter questionsAdapter;
    FirebaseFirestore firestore;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);


        Toolbar toolbar = findViewById(R.id.questions_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        recyclerView = findViewById(R.id.questions_recycler);
        addQuestionBtn = findViewById(R.id.addQuestionBtn);

        progressDialog = new Dialog(Questions.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Questions.this, QuestionsAdderActivity.class);
                intent.putExtra("ACTION", "ADD");
                startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        firestore = FirebaseFirestore.getInstance();

        fetchQuestions();
    }


    private void fetchQuestions() {
        questionsModelList.clear();

        progressDialog.show();

        firestore.collection("Quizzes").document(moduleidfromtopicactivity)
                .collection(subidfromtopicactivity).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> document_list = new ArrayMap<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            document_list.put(documentSnapshot.getId(), documentSnapshot);
                        }
                        QueryDocumentSnapshot question_list_document = document_list.get("Question_List");

                        String questions_exist = question_list_document.getString("QNO");

                        for (int i = 0; i < Integer.valueOf(questions_exist); i++) {
                            String question_id = question_list_document.getString("Q" + String.valueOf(i + 1) + "_Id");

                            QueryDocumentSnapshot queryDocumentSnapshot = document_list.get(question_id);

                            questionsModelList.add(new QuestionsModel(
                                    question_id,
                                    queryDocumentSnapshot.getString("Question"),
                                    queryDocumentSnapshot.getString("A"),
                                    queryDocumentSnapshot.getString("B"),
                                    queryDocumentSnapshot.getString("C"),
                                    queryDocumentSnapshot.getString("D"),
                                    Integer.valueOf(queryDocumentSnapshot.getString("Correct"))

                            ));
                        }
                        questionsAdapter = new QuestionsAdapter(questionsModelList);
                        recyclerView.setAdapter(questionsAdapter);

                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Questions.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (questionsAdapter != null) {
            questionsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}