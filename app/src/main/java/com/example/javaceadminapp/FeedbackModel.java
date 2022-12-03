package com.example.javaceadminapp;

public class FeedbackModel {
    String feedback_id, feedback_user, feedback_title, feedback_message;

    public FeedbackModel(String feedback_id, String feedback_user, String feedback_title, String feedback_message) {
        this.feedback_id = feedback_id;
        this.feedback_user = feedback_user;
        this.feedback_title = feedback_title;
        this.feedback_message = feedback_message;
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
}
