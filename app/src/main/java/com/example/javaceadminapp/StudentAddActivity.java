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
import android.widget.TextView;
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
    EditText userName, userFullName, phone, userEmail, userPassword;
    Button registerUser;
    String uid, uname, ufullname, uphone, uemail;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    TextView regText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);

        regText = findViewById(R.id.registerText);

        userName = findViewById(R.id.userUsername);
        userFullName = findViewById(R.id.userFullName);
        phone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);



        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(StudentAddActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        registerUser = findViewById(R.id.userRegBtn);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            regText.setText("Update User");
            registerUser.setText("Update");
            uid = bundle.getString("userId");
            uname = bundle.getString("userName");
            ufullname = bundle.getString("userFullName");
            uphone = bundle.getString("userPhoneNumber");
            uemail = bundle.getString("userEmail");

            userName.setText(uname);
            userFullName.setText(ufullname);
            phone.setText(uphone);
            userEmail.setText(uemail);

            userPassword.setVisibility(View.GONE);
        } else {
            registerUser.setText("Add");
        }

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if (bundle1 != null) {
                    String id = uid;
                    String name = userName.getText().toString();
                    String fullname = userFullName.getText().toString();
                    String phone = String.valueOf(uphone);
                    String email = userEmail.getText().toString();

                    updateStudent(id, name, fullname, phone, email);
                }else {
                    if (userName.getText().toString().isEmpty()) {
                        userName.setError("Please enter username");
                        return;
                    } if (userFullName.getText().toString().isEmpty()) {
                        userFullName.setError("Please enter the full name");
                        return;
                    } if (phone.getText().toString().isEmpty()) {
                        phone.setError("Please enter phone number");
                        return;
                    }if (userPassword.getText().toString().isEmpty() || userPassword.getText().toString().length() <= 7) {
                        userPassword.setError("Password should not be empty and more than 7");
                        return;
                    } if (userEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString()).matches()) {
                        userEmail.setError("Invalid email");
                        return;
                    }
                    addUser(userName.getText().toString(), userFullName.getText().toString(), phone.getText().toString(),
                            userEmail.getText().toString(), userPassword.getText().toString());
                }

            }
        });



    }

    private void updateStudent(String id, String name, String fullname, String phone, String email) {
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email);

        firestore.collection("Users").document(id).update("username", name, "fullname", fullname,
                        "phone", phone, "email", email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        HashMap<String, Object> result = new HashMap<>();
                        result.put("username", name);
                        result.put("fullname", fullname);
                        result.put("phone", phone);
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


    private void addUser(String username, String fullname, String phone, String email, String password) {
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String uid = firebaseUser.getUid();

                DocumentReference documentReference = firestore.collection("Users").document(firebaseUser.getUid());


                Map<String, Object> student_data = new HashMap<>();
                student_data.put("uid", uid);
                student_data.put("username", username);
                student_data.put("fullname", fullname);
                student_data.put("phone", phone);
                student_data.put("email", email);
                student_data.put("user", "yes");
                student_data.put("score", "0");

                documentReference.set(student_data);

                FirebaseUser firebaseUsers = firebaseAuth.getCurrentUser();
                assert firebaseUsers != null;
                String uids = firebaseUsers.getUid();
                HashMap<Object, String> hashMap = new HashMap<>();
                hashMap.put("uid", uids);
                hashMap.put("username", username);
                hashMap.put("fullname", fullname);
                hashMap.put("phone", phone);
                hashMap.put("email", email);
                hashMap.put("user", "yes");
                hashMap.put("image", "https://firebasestorage.googleapis.com/v0/b/javacemahman-10e8a.appspot.com/o/Users_Profile_Cover_image%2Fimage_oucSYp84pETqq8m2VocnmGpm2gF3?alt=media&token=5de45866-0b88-4ad2-988b-2b117a8d44cc");

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