package com.example.javaceadminapp;

public class ExercisesModel {

    String eId, exercise_title, exercise_content, exercise_score;

    public ExercisesModel(String eId, String exercise_title, String exercise_content, String exercise_score) {
        this.eId = eId;
        this.exercise_title = exercise_title;
        this.exercise_content = exercise_content;
        this.exercise_score = exercise_score;
    }

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public String getExercise_title() {
        return exercise_title;
    }

    public void setExercise_title(String exercise_title) {
        this.exercise_title = exercise_title;
    }

    public String getExercise_content() {
        return exercise_content;
    }

    public void setExercise_content(String exercise_content) {
        this.exercise_content = exercise_content;
    }

    public String getExercise_score() {
        return exercise_score;
    }

    public void setExercise_score(String exercise_score) {
        this.exercise_score = exercise_score;
    }
}
