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

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesViewholder> {

    MainActivity2 mainActivity2;
    List<ExercisesModel> exercisesModelList;


    public ExercisesAdapter(MainActivity2 mainActivity2, List<ExercisesModel> exercisesModelList) {
        this.mainActivity2 = mainActivity2;
        this.exercisesModelList = exercisesModelList;
    }

    @NonNull
    @Override
    public ExercisesViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);

        ExercisesViewholder exercisesViewholder = new ExercisesViewholder(itemView);

        exercisesViewholder.setOnClickListener(new ExercisesViewholder.ListenerClicker() {
            @Override
            public void onOneClick(View view, int position) {

            }

            @Override
            public void onOneLongClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity2);

                String[] options = {"Update", "Delete", "Change Password"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            String eId = exercisesModelList.get(position).geteId();
                            String title = exercisesModelList.get(position).getExercise_title();
                            String content = exercisesModelList.get(position).getExercise_content();
                            String score = exercisesModelList.get(position).getExercise_score();

                            Intent intent = new Intent(mainActivity2, AdminAddActivity.class);
                            intent.putExtra("eId", eId);
                            intent.putExtra("title", title);
                            intent.putExtra("content", content);
                            intent.putExtra("score", score);

                            mainActivity2.startActivity(intent);
                        } if (i == 1) {
                            //mainActivity2.deleteAdmin(position);
                        }
                        if (i == 2) {

                        }

                    }
                }).create().show();

            }
        });


        return exercisesViewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull ExercisesViewholder holder, int position) {
        holder.title.setText(exercisesModelList.get(position).getExercise_title());
        holder.score.setText(exercisesModelList.get(position).getExercise_score());
    }

    @Override
    public int getItemCount() {
        return exercisesModelList.size();
    }
}
