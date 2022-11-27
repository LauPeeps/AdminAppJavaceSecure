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
                AlertDialog.Builder builder = new AlertDialog.Builder(admin);

                String[] options = {"Update", "Delete", "Change Password"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            String uid = adminModelList.get(position).getUid();
                            String name = adminModelList.get(position).getName();
                            String email = adminModelList.get(position).getEmail();

                            Intent intent = new Intent(admin, AdminAddActivity.class);
                            intent.putExtra("aid", uid);
                            intent.putExtra("aname", name);
                            intent.putExtra("aemail", email);

                            admin.startActivity(intent);
                        } if (i == 1) {
                            admin.deleteAdmin(position);
                        }
                        if (i == 2) {
                            String uid = adminModelList.get(position).getUid();
                            Intent intent = new Intent(admin, ChangePassword.class);
                            intent.putExtra("aid", uid);
                            admin.startActivity(intent);
                        }

                    }
                }).create().show();

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
