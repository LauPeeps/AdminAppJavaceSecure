package com.example.javaceadminapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminViewholder> {

    Admin admin;
    List<AdminModel> adminModelList;


    public AdminAdapter(Admin admin, List<AdminModel> adminModelList) {
        this.admin = admin;
        this.adminModelList = adminModelList;
    }


    @NonNull
    @Override
    public AdminViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item, parent, false);

        AdminViewholder userAdminViewholder = new AdminViewholder(itemView);

        userAdminViewholder.setOnClickListener(new AdminViewholder.ListenerClicker() {
            @Override
            public void onOneClick(View view, int position) {

            }

            @Override
            public void onOneLongClick(View view, int position) {

            }
        });


        return userAdminViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewholder holder, int position) {
        holder.textEmail.setText(adminModelList.get(position).getEmail());
        holder.textName.setText(adminModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return adminModelList.size();
    }
}
