package com.example.javaceadminapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.Viewholder> {

    Context context;
    ArrayList<LeaderboardModel> leaderboards;

    public LeaderboardAdapter(Context context, ArrayList<LeaderboardModel> leaderboards) {
        this.context = context;
        this.leaderboards = leaderboards;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_item, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        LeaderboardModel modelLeaderboard = leaderboards.get(position);

        holder.nameText.setText(modelLeaderboard.getUsername());
        holder.scoreText.setText(modelLeaderboard.getScore());

        if (Integer.parseInt(String.valueOf(holder.scoreText.getText().toString())) <= 10) {
            int bronze = Color.parseColor("#CD7F32");
            holder.trophy.setColorFilter(bronze);
        }
        if (Integer.parseInt(String.valueOf(holder.scoreText.getText().toString())) > 10 && Integer.parseInt(String.valueOf(holder.scoreText.getText().toString())) <= 50) {
            int copper = Color.parseColor("#B87333");
            holder.trophy.setColorFilter(copper);
        } if (Integer.parseInt(String.valueOf(holder.scoreText.getText().toString())) > 50 && Integer.parseInt(String.valueOf(holder.scoreText.getText().toString())) <= 99) {
            int silver = Color.parseColor("#C0C0C0");
            holder.trophy.setColorFilter(silver);
        } if (Integer.parseInt(String.valueOf(holder.scoreText.getText().toString())) >= 100) {
            int gold = Color.parseColor("#E5B80B");
            holder.trophy.setColorFilter(gold);
        }
    }

    @Override
    public int getItemCount() {
        return leaderboards.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nameText, scoreText;
        ImageView trophy;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name);
            scoreText = itemView.findViewById(R.id.score);
            trophy = itemView.findViewById(R.id.trophy);


        }
    }
}
