package com.example.javaceadminapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopicViewholder extends RecyclerView.ViewHolder {

    TextView topicTitle;
    View view;

    public TopicViewholder(@NonNull View itemView) {
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
        topicTitle = itemView.findViewById(R.id.catname);

    }
    private TopicViewholder.ListenerClicker listenerClicker;

    public interface ListenerClicker{
        void onOneClick(View view, int position);
        void onOneLongClick(View view, int position);
    }
    public void setOnClickListener(TopicViewholder.ListenerClicker listenerClicker1) {
        listenerClicker = listenerClicker1;
    }
}
