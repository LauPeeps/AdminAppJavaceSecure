package com.example.javaceadminapp;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingForItem extends RecyclerView.ItemDecoration {

    int verticalSpaceHeight;

    public SpacingForItem(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}
