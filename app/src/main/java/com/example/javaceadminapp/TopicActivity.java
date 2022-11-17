package com.example.javaceadminapp;

import static com.example.javaceadminapp.Category.category_index;
import static com.example.javaceadminapp.Category.category_list;
import static com.example.javaceadminapp.Sets.idOfSets;
import static com.example.javaceadminapp.Sets.set_index;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TopicActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    TextView topicTitle, topicContent;
    Button questionBtn;
    int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);



        topicTitle = findViewById(R.id.topicTitle);
        topicContent = findViewById(R.id.topicContent);

        firestore = FirebaseFirestore.getInstance();



        questionBtn = findViewById(R.id.qBtn);

        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TopicActivity.this, Questions.class);
                startActivity(intent);
                finish();

            }
        });



        firestore.collection("Quiz").document(category_list.get(category_index).getId())
                .collection(idOfSets.get(set_index)).document("Topic_List").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        topicTitle.setText(documentSnapshot.getString("Topic_Title"));
                        topicContent.setText(documentSnapshot.getString("Topic_Content"));
                    }
                });

    }



}