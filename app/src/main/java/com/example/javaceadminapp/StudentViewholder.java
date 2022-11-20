package com.example.javaceadminapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentViewholder extends RecyclerView.ViewHolder {

    TextView textName, textEmail;
    View view;

    public StudentViewholder(@NonNull View itemView) {
        super(itemView);

        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerClicker.onOneClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listenerClicker.onOneLongClick(view, getAdapterPosition());
                return true;
            }
        });

        textName = itemView.findViewById(R.id.name);
        textEmail = itemView.findViewById(R.id.email);
    }
    private StudentViewholder.ListenerClicker listenerClicker;

    public interface ListenerClicker{
        void onOneClick(View view, int position);
        void onOneLongClick(View view, int position);
    }
    public void setOnClickListener(StudentViewholder.ListenerClicker listenerClicker1) {
        listenerClicker = listenerClicker1;
    }
}
