package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

public class TopicActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    TextView topicTitle, topicContent;
    Button questionBtn, showDialog, updateTopic;
    EditText name, content;
    Dialog topicUpdate;
    String subid, subname, moduleid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Bundle bundle = getIntent().getExtras();


        subid = bundle.getString("subid");
        moduleid = bundle.getString("moduleid");

        topicTitle = findViewById(R.id.topicTitle);
        topicContent = findViewById(R.id.topicContent);

        firestore = FirebaseFirestore.getInstance();


        topicUpdate = new Dialog(TopicActivity.this);
        topicUpdate.setContentView(R.layout.edit_topic_dialog);
        topicUpdate.setCancelable(true);
        topicUpdate.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        name = topicUpdate.findViewById(R.id.editTopicTitle);
        content = topicUpdate.findViewById(R.id.editTopicContent);
        updateTopic = topicUpdate.findViewById(R.id.updateTopicBtn);

        questionBtn = findViewById(R.id.qBtn);
        showDialog = findViewById(R.id.showEditDialog);

        fetchTopic();

        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TopicActivity.this, Questions.class);
                intent.putExtra("module", moduleid);
                intent.putExtra("submodule", subid);
                startActivity(intent);
                finish();

            }
        });

        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText(subid);
                content.setText(subname);
                topicUpdate.show();
            }
        });

        updateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTopic(name.getText().toString(), content.getText().toString());
                fetchTopic();
            }
        });



        topicContent.setMovementMethod(new ScrollingMovementMethod());

    }

    private void fetchTopic() {
        firestore.collection("Quizzes").document(moduleid)
                .collection(subid).document("Topic_List").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        topicTitle.setText(documentSnapshot.getString("topic_title"));
                        topicContent.setText(documentSnapshot.getString("topic_content"));
                    }
                });
    }

    private void updateTopic(String topicT, String topicC) {
        topicUpdate.dismiss();
        firestore.collection("Quizzes").document(moduleid).collection(subid).document("Topic_List")
                .update("topic_title", topicT, "topic_content", topicC).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(TopicActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TopicActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}