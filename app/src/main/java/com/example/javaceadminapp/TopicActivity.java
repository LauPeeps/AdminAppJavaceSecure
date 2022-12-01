package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TopicActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    Dialog progressDialog;


    List<TopicModel> topicModelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TopicAdapter topicAdapter;
    Button addTopicBtn, dialogAddTopicBtn;
    Dialog addPage;
    static String subidfromtopicactivity, moduleidfromtopicactivity;

    EditText titleOfTopic, contentOfContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Bundle bundle = getIntent().getExtras();
        subidfromtopicactivity = bundle.getString("subid");
        moduleidfromtopicactivity = bundle.getString("moduleid");

        firestore = FirebaseFirestore.getInstance();

        addTopicBtn = findViewById(R.id.addTopicBtn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Topics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(TopicActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        recyclerView = findViewById(R.id.topic_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fetchTopics();

        addPage = new Dialog(TopicActivity.this);
        addPage.setContentView(R.layout.add_topic_page);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        titleOfTopic = addPage.findViewById(R.id.topic_name_dialog);
        contentOfContent = addPage.findViewById(R.id.topic_content_dialog);
        contentOfContent.setMovementMethod(new ScrollingMovementMethod());
        dialogAddTopicBtn = addPage.findViewById(R.id.addTopicDialogBtn);

        addTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleOfTopic.getText().clear();
                contentOfContent.getText().clear();
                addPage.show();
            }
        });

        dialogAddTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleOfTopic.getText().toString().isEmpty()) {
                    titleOfTopic.setError("Please enter the topic title");
                    return;
                } if (contentOfContent.getText().toString().isEmpty()) {
                    contentOfContent.setError("Please enter the topic content");
                    return;
                }
                addTopic(titleOfTopic.getText().toString(), contentOfContent.getText().toString());
            }
        });


    }

    private void fetchTopics() {
        progressDialog.show();

        firestore.collection("Quizzes").document(moduleidfromtopicactivity).collection(subidfromtopicactivity).document("Topic_List").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                topicModelList.clear();
                long existing_topics = documentSnapshot.getLong("topic_exist");
                for (int i = 1; i <= existing_topics; i++) {
                    TopicModel topicModel = new TopicModel(documentSnapshot.getString("topic" +String.valueOf(i) + "_title"),
                            documentSnapshot.getString("topic" + String.valueOf(i) + "_name"));
                    topicModelList.add(topicModel);
                }
                progressDialog.dismiss();
                topicAdapter = new TopicAdapter(TopicActivity.this, topicModelList);
                recyclerView.setAdapter(topicAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TopicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTopic(String titleOf, String contentOf) {
        addPage.dismiss();
        progressDialog.show();

        DocumentReference  documentReference = firestore.collection("Quizzes").document(moduleidfromtopicactivity).collection(subidfromtopicactivity).document("Topic_List");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Long howMany = documentSnapshot.getLong("topic_exist");
                Map<String, Object> data = new HashMap<>();
                data.put("topic" + String.valueOf(howMany + 1) + "_title", titleOf);
                data.put("topic" + String.valueOf(howMany + 1) + "_content", contentOf);
                data.put("topic_exist", howMany + 1);

                documentReference.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        topicAdapter.notifyItemInserted(topicModelList.size());
                        Toast.makeText(TopicActivity.this, "Successfully added topic", Toast.LENGTH_SHORT).show();
                        fetchTopics();
                    }
                });
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