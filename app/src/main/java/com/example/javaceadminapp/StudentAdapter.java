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

public class StudentAdapter extends RecyclerView.Adapter<StudentViewholder> {

    Student student;
    List<StudentModel> studentModelList;


    public StudentAdapter(Student student, List<StudentModel> studentModelList) {
        this.student = student;
        this.studentModelList = studentModelList;
    }


    @NonNull
    @Override
    public StudentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);

        StudentViewholder studentViewholder = new StudentViewholder(itemView);

        studentViewholder.setOnClickListener(new StudentViewholder.ListenerClicker() {
            @Override
            public void onOneClick(View view, int position) {

            }

            @Override
            public void onOneLongClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(student);

                String[] options = {"Update", "Delete", "Change Password"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            String uid = studentModelList.get(position).getUid();
                            String name = studentModelList.get(position).getName();
                            String email = studentModelList.get(position).getEmail();

                            Intent intent = new Intent(student, StudentAddActivity.class);
                            intent.putExtra("aid", uid);
                            intent.putExtra("aname", name);
                            intent.putExtra("aemail", email);

                            student.startActivity(intent);
                        } if (i == 1) {
                            student.deleteStudent(position);
                        }
                        if (i == 2) {
                            String uid = studentModelList.get(position).getUid();
                            Intent intent = new Intent(student, ChangePassword.class);
                            intent.putExtra("aid", uid);
                            student.startActivity(intent);
                        }

                    }
                }).create().show();

            }
        });


        return studentViewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull StudentViewholder holder, int position) {
        holder.textEmail.setText(studentModelList.get(position).getEmail());
        holder.textName.setText(studentModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return studentModelList.size();
    }
}
