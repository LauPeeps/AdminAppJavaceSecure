package com.example.javaceadminapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicViewholder> {

    TopicActivity topicActivity;
    List<TopicModel> topicModelList;


    public TopicAdapter(TopicActivity topicActivity, List<TopicModel> topicModelList) {
        this.topicActivity = topicActivity;
        this.topicModelList = topicModelList;
    }

    @NonNull
    @Override
    public TopicViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        TopicViewholder topicViewholder = new TopicViewholder(itemView);

        topicViewholder.setOnClickListener(new TopicViewholder.ListenerClicker() {
            @Override
            public void onOneClick(View view, int position) {
                Intent intent = new Intent(topicActivity, VideoActivity.class);

                topicActivity.startActivity(intent);

            }

            @Override
            public void onOneLongClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(topicActivity);

                String[] options = {"Update", "Delete"};

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            String title = topicModelList.get(position).getTopic_title();
                            String content = topicModelList.get(position).getTopic_content();

                            topicActivity.titleOfTopic.setText(title);
                            topicActivity.contentOfContent.setText(content);
                            topicActivity.addPage.show();

                            topicActivity.dialogAddTopicBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    topicActivity.updateTopic(position, topicActivity.titleOfTopic.getText().toString(), topicActivity.contentOfContent.getText().toString());
                                }
                            });


                        } if (i == 1) {

                        }

                    }
                }).create().show();

            }
        });


        return topicViewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull TopicViewholder holder, int position) {
        holder.topicTitle.setText(topicModelList.get(position).getTopic_title());
    }

    @Override
    public int getItemCount() {
        return topicModelList.size();
    }
}
