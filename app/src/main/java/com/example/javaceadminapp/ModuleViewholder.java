package com.example.javaceadminapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ModuleViewholder extends RecyclerView.ViewHolder {

    TextView moduleName, modulePreview;
    View view;

    public ModuleViewholder(@NonNull View itemView) {
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
        modulePreview = itemView.findViewById(R.id.modulePreview);

    }
    private ModuleViewholder.ListenerClicker listenerClicker;

    public interface ListenerClicker{
        void onOneClick(View view, int position);
        void onOneLongClick(View view, int position);
    }
    public void setOnClickListener(ModuleViewholder.ListenerClicker listenerClicker1) {
        listenerClicker = listenerClicker1;
    }
}