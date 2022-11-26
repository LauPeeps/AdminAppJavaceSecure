package com.example.javaceadminapp;

import static com.example.javaceadminapp.Category.category_index;
import static com.example.javaceadminapp.Category.category_list;
import static com.example.javaceadminapp.Sets.idOfSets;
import static com.example.javaceadminapp.Sets.set_index;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
    String subid, subname, moduleid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Bundle bundle = getIntent().getExtras();

        subid = bundle.getString("subid");
        subname = bundle.getString("subname");
        moduleid = bundle.getString("moduleid");

        topicTitle = findViewById(R.id.topicTitle);
        topicContent = findViewById(R.id.topicContent);

        firestore = FirebaseFirestore.getInstance();




        num = set_index;

        questionBtn = findViewById(R.id.qBtn);

        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TopicActivity.this, Questions.class);
                startActivity(intent);
                finish();

            }
        });



        firestore.collection("Quizzes").document(moduleid)
                .collection(subid).document("Topic_List").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        topicTitle.setText(documentSnapshot.getString("topic_title"));
                        topicContent.setText(documentSnapshot.getString("topic_content"));
                    }
                });

        topicContent.setMovementMethod(new ScrollingMovementMethod());

    }



}