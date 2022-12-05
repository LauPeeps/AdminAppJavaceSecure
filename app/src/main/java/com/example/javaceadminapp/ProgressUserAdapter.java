package com.example.javaceadminapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ProgressUserAdapter extends RecyclerView.Adapter<ProgressUserViewholder> {

    ProgressUser progressUser;
    List<ProgressUserModel> progressUserModelList;

    public ProgressUserAdapter(ProgressUser progressUser, List<ProgressUserModel> progressUserModelList) {
        this.progressUser = progressUser;
        this.progressUserModelList = progressUserModelList;
    }

    @NonNull
    @Override
    public ProgressUserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_user_item, parent, false);

        ProgressUserViewholder progressUserViewholder = new ProgressUserViewholder(itemView);

        progressUserViewholder.setOnClickListener(new ProgressUserViewholder.ListenerClicker() {
            @Override
            public void onOneClick(View view, int position) {

            }

            @Override
            public void onOneLongClick(View view, int position) {


            }
        });


        return progressUserViewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull ProgressUserViewholder holder, int position) {
        Long value = progressUserModelList.get(position).getProgress();

        holder.moduleName.setText(progressUserModelList.get(position).getModule_name());
        holder.progressPercentText.setText(String.valueOf(value) + "%");

    }

    @Override
    public int getItemCount() {
        return progressUserModelList.size();
    }


}
