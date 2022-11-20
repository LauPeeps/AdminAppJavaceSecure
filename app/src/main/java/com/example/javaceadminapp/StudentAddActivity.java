package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StudentAddActivity extends AppCompatActivity {

    Dialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    EditText userName, userEmail, userPassword;
    Button registerUser;
    String aid, aname, aemail;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);


        userName = findViewById(R.id.studentName);
        userEmail = findViewById(R.id.studentEmail);
        userPassword = findViewById(R.id.studentPassword);
        registerUser = findViewById(R.id.registerUser);


        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(StudentAddActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            registerUser.setText("Update");
            aid = bundle.getString("aid");
            aname = bundle.getString("aname");
            aemail = bundle.getString("aemail");

            userName.setText(aname);
            userEmail.setText(aemail);
            userPassword.setVisibility(View.GONE);
        } else {
            registerUser.setText("Add");
        }

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null) {
                    String id = aid;
                    String name = userName.getText().toString();
                    String email = userEmail.getText().toString();

                    updateStudent(id, name, email);
                }else {
                    if (userName.getText().toString().isEmpty()) {
                        userName.setError("Please enter username");
                        return;
                    } if (userPassword.getText().toString().isEmpty() || userPassword.getText().toString().length() <= 7) {
                        userPassword.setError("Password should not be empty and more than 7");
                        return;
                    } if (userEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString()).matches()) {
                        userEmail.setError("Invalid email");
                        return;
                    }
                    addUser(userName.getText().toString(), userEmail.getText().toString(), userPassword.getText().toString());
                }

            }
        });



    }

    private void updateStudent(String id, String name, String email) {
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email);

        firestore.collection("Users").document(id).update("name", name, "email", email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        HashMap<String, Object> result = new HashMap<>();
                        result.put("name", name);
                        result.put("email", email);

                        databaseReference.child(id).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                Toast.makeText(StudentAddActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(StudentAddActivity.this, "Unable to update", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(StudentAddActivity.this, Student.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(StudentAddActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void addUser(String name, String email, String password) {
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String uid = firebaseUser.getUid();

                DocumentReference documentReference = firestore.collection("Users").document(firebaseUser.getUid());

                Map<String, Object> student_data = new HashMap<>();
                student_data.put("uid", uid);
                student_data.put("email", email);
                student_data.put("name", name);
                student_data.put("user", "yes");
                student_data.put("score", "0");

                documentReference.set(student_data);

                FirebaseUser firebaseUsers = firebaseAuth.getCurrentUser();
                assert firebaseUsers != null;
                String uids = firebaseUsers.getUid();
                HashMap<Object, String> hashMap = new HashMap<>();
                hashMap.put("email", email);
                hashMap.put("name", name);
                hashMap.put("uid", uids);
                hashMap.put("user", "yes");
                hashMap.put("image", "");

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://javacemahman-10e8a-default-rtdb.firebaseio.com/");
                DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                databaseReference.child(uid).setValue(hashMap);

                progressDialog.dismiss();
                Toast.makeText(StudentAddActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(StudentAddActivity.this, Student.class);
                startActivity(intent);
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentAddActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

}