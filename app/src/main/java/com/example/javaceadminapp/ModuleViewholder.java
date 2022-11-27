package com.example.javaceadminapp;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModuleViewholder extends RecyclerView.ViewHolder {

    TextView moduleName, modulePreview;
    View view;
    Dialog editDialog;
    EditText editModuleName, editModulePreview;
    public ModuleViewholder(@NonNull View itemView) {
        super(itemView);

        view = itemView;


        editDialog = new Dialog(itemView.getContext());
        editDialog.setContentView(R.layout.edit_category_page);
        editDialog.setCancelable(true);
        editDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        editModuleName = view.findViewById(R.id.addModuleName);
        editModulePreview = view.findViewById(R.id.addModuleMessage);

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
