package com.example.javaceadminapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ForumCommentAdapter extends RecyclerView.Adapter<com.example.javaceadminapp.ForumCommentAdapter.MyHolder> {

    Context context;
    List<ForumCommentModel> list;

    public ForumCommentAdapter(Context context, List<ForumCommentModel> list, String myuid, String postid) {
        this.context = context;
        this.list = list;
        this.myuid = myuid;
        this.postid = postid;
    }

    String myuid;
    String postid;


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String name = list.get(position).getCommenter();
        String comment = list.get(position).getComment();
        String timestamp = list.get(position).getTimePosted();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.name.setText(name);
        holder.time.setText(timedate);
        holder.comment.setText(comment);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView name, comment, time;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.commenter);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.commentTime);
        }
    }
}
