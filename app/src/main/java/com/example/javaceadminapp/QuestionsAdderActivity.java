package com.example.javaceadminapp;

import static com.example.javaceadminapp.Questions.module;
import static com.example.javaceadminapp.Questions.questionsModelList;
import static com.example.javaceadminapp.Questions.submodule;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArrayMap;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class QuestionsAdderActivity extends AppCompatActivity {

    EditText question, option1, option2, option3, option4, answer;
    Button addQuestionBtn;
    String questionStr, option1Str, option2Str, option3Str, option4Str, answerStr;
    Dialog progressDialog;
    FirebaseFirestore firestore;
    String action;
    int question_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_adder);


        Toolbar toolbar = findViewById(R.id.addquestions_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        question = findViewById(R.id.questionInfo);
        option1 = findViewById(R.id.choice1);
        option2 = findViewById(R.id.choice2);
        option3 = findViewById(R.id.choice3);
        option4 = findViewById(R.id.choice4);
        answer = findViewById(R.id.correctAnswer);

        addQuestionBtn = findViewById(R.id.addQbtn);


        progressDialog = new Dialog(QuestionsAdderActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        firestore = FirebaseFirestore.getInstance();

        action = getIntent().getStringExtra("ACTION");

        if (action.compareTo("EDIT") == 0) {
            question_id = getIntent().getIntExtra("Q_Id", 1);
            fetchData(question_id);
            getSupportActionBar().setTitle("Question " + String.valueOf(question_id + 1));

            addQuestionBtn.setText("Update");
        } else {
            getSupportActionBar().setTitle("Question " + String.valueOf(questionsModelList.size() + 1));
            addQuestionBtn.setText("Add");
        }

        addQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionStr = question.getText().toString();
                option1Str = option1.getText().toString();
                option2Str = option2.getText().toString();
                option3Str = option3.getText().toString();
                option4Str = option4.getText().toString();
                answerStr = answer.getText().toString();


                if (questionStr.isEmpty()) {
                    question.setError("Please enter a question");
                    return;
                }
                if (option1Str.isEmpty()) {
                    option1.setError("Please enter option 1");
                    return;
                }
                if (option2Str.isEmpty()) {
                    option2.setError("Please enter option 2");
                    return;
                }
                if (option3Str.isEmpty()) {
                    option3.setError("Please enter option 3");
                    return;
                }
                if (option4Str.isEmpty()) {
                    option4.setError("Please enter option 4");
                    return;
                }
                if (answerStr.isEmpty()) {
                    answer.setError("Please enter the correct answer");
                    return;
                }
                if (Integer.valueOf(answerStr) > 4 || Integer.valueOf(answerStr) < 1) {
                    answer.setError("Please enter number from 1 - 4");
                    return;
                }

                if (action.compareTo("EDIT") == 0) {
                    editQuestion();
                } else {
                    addQuestion();
                }
            }
        });
    }

    private void addQuestion() {
        progressDialog.show();

        Map<String, Object> question_data = new ArrayMap<>();

        question_data.put("Question", questionStr);
        question_data.put("A", option1Str);
        question_data.put("B", option2Str);
        question_data.put("C", option3Str);
        question_data.put("D", option4Str);
        question_data.put("Correct", answerStr);

        String document_id = firestore.collection("Quizzes").document(module)
                .collection(submodule).document().getId();

        firestore.collection("Quizzes").document(module)
                .collection(submodule).document(document_id)
                .set(question_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Map<String, Object> question_document = new ArrayMap<>();

                        question_document.put("Q" + String.valueOf(questionsModelList.size() + 1) + "_Id", document_id);
                        question_document.put("QNO", String.valueOf(questionsModelList.size() + 1));

                        firestore.collection("Quizzes").document(module)
                                .collection(submodule).document("Question_List")
                                .update(question_document)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(QuestionsAdderActivity.this, "Question added successfully", Toast.LENGTH_SHORT).show();

                                        questionsModelList.add(new QuestionsModel(
                                                document_id, questionStr, option1Str, option2Str, option3Str, option4Str, Integer.valueOf(answerStr)
                                        ));
                                        progressDialog.dismiss();

                                        QuestionsAdderActivity.this.finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QuestionsAdderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionsAdderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void fetchData(int question_id) {
        question.setText(questionsModelList.get(question_id).getQuestion());
        option1.setText(questionsModelList.get(question_id).getOption1());
        option2.setText(questionsModelList.get(question_id).getOption2());
        option3.setText(questionsModelList.get(question_id).getOption3());
        option4.setText(questionsModelList.get(question_id).getOption4());
        answer.setText(String.valueOf(questionsModelList.get(question_id).getCorrect()));
    }

    private void editQuestion() {
        progressDialog.show();

        Map<String, Object> question_data = new ArrayMap<>();
        question_data.put("Question", questionStr);
        question_data.put("A", option1Str);
        question_data.put("B", option2Str);
        question_data.put("C", option3Str);
        question_data.put("D", option4Str);
        question_data.put("Correct", answerStr);

        firestore.collection("Quizzes").document(module)
                .collection(submodule).document(questionsModelList.get(question_id).getQuestion_id())
                .set(question_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(QuestionsAdderActivity.this, "Question updated successfully", Toast.LENGTH_SHORT).show();
                        questionsModelList.get(question_id).setQuestion(questionStr);
                        questionsModelList.get(question_id).setOption1(option1Str);
                        questionsModelList.get(question_id).setOption2(option2Str);
                        questionsModelList.get(question_id).setOption3(option3Str);
                        questionsModelList.get(question_id).setOption4(option4Str);
                        questionsModelList.get(question_id).setCorrect(Integer.parseInt(answerStr));

                        progressDialog.dismiss();

                        QuestionsAdderActivity.this.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuestionsAdderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}