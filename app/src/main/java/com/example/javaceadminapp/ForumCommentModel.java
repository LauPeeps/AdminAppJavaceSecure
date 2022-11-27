package com.example.javaceadminapp;

public class ForumCommentModel {

    public ForumCommentModel() {}


    String commentId, timePosted, commenter, comment, uid, email, username;


    public ForumCommentModel(String commentId, String timePosted, String commenter, String comment, String uid, String email, String username) {
        this.commentId = commentId;
        this.timePosted = timePosted;
        this.commenter = commenter;
        this.comment = comment;
        this.uid = uid;
        this.email = email;
        this.username = username;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
