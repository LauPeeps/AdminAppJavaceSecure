package com.example.javaceadminapp;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProgressUserViewholder extends RecyclerView.ViewHolder {

    TextView moduleName, progressPercentText;
    View view;

    public ProgressUserViewholder(@NonNull View itemView) {
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

        moduleName = itemView.findViewById(R.id.moduleName);
        progressPercentText = itemView.findViewById(R.id.progressPercentText);

    }
    private ProgressUserViewholder.ListenerClicker listenerClicker;

    public interface ListenerClicker{
        void onOneClick(View view, int position);
        void onOneLongClick(View view, int position);
    }
    public void setOnClickListener(ProgressUserViewholder.ListenerClicker listenerClicker1) {
        listenerClicker = listenerClicker1;
    }
}
