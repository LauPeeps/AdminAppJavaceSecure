package com.example.javaceadminapp;

import com.google.firebase.Timestamp;

public class FeedbackModel {
    String feedback_id, feedback_user, feedback_title, feedback_message;
    Timestamp feedback_created;


    public FeedbackModel(String feedback_id, String feedback_user, String feedback_title, String feedback_message, Timestamp feedback_created) {
        this.feedback_id = feedback_id;
        this.feedback_user = feedback_user;
        this.feedback_title = feedback_title;
        this.feedback_message = feedback_message;
        this.feedback_created = feedback_created;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(String feedback_id) {
        this.feedback_id = feedback_id;
    }

    public String getFeedback_user() {
        return feedback_user;
    }

    public void setFeedback_user(String feedback_user) {
        this.feedback_user = feedback_user;
    }

    public String getFeedback_title() {
        return feedback_title;
    }

    public void setFeedback_title(String feedback_title) {
        this.feedback_title = feedback_title;
    }

    public String getFeedback_message() {
        return feedback_message;
    }

    public void setFeedback_message(String feedback_message) {
        this.feedback_message = feedback_message;
    }

    public Timestamp getFeedback_created() {
        return feedback_created;
    }

    public void setFeedback_created(Timestamp feedback_created) {
        this.feedback_created = feedback_created;
    }
}
