package com.example.javaceadminapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.Viewholder> {
    Context context;
    ArrayList<FeedbackModel> feedbackModels;

    public FeedbackAdapter(Context context, ArrayList<FeedbackModel> feedbackModels) {
        this.context = context;
        this.feedbackModels = feedbackModels;
    }

    @NonNull
    @Override
    public FeedbackAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.feedback_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.Viewholder holder, int position) {
        FeedbackModel feedbackModel = feedbackModels.get(position);

        Date date = feedbackModel.getFeedback_created().toDate();
        String monthDayYear = (String) DateFormat.format("MMM-dd-yyyy", date);
        holder.title.setText(feedbackModel.getFeedback_title());
        holder.content.setText(feedbackModel.getFeedback_message());
        holder.user.setText(feedbackModel.getFeedback_user());
        holder.timestamp.setText(monthDayYear);

    }

    @Override
    public int getItemCount() {
        return feedbackModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, content, user, timestamp;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.feedbackTitle);
            content = itemView.findViewById(R.id.feedbackMessage);
            user = itemView.findViewById(R.id.feedbackUser);
            timestamp = itemView.findViewById(R.id.feedbackCreated);
        }
    }
}
