package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText adminFirst, adminSecond;
    Button loginBtn;
    FirebaseAuth firebaseAuth;
    Dialog progressDialog;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firestore = FirebaseFirestore.getInstance();

        adminFirst = findViewById(R.id.adminEmail);
        adminSecond = findViewById(R.id.adminPassword);
        loginBtn = findViewById(R.id.adminLoginBtn);

        progressDialog = new Dialog(MainActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uEmail = adminFirst.getText().toString();
                String uPass = adminSecond.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(uEmail).matches()) {
                    adminFirst.setError("Invalid Email");
                    adminFirst.setFocusable(true);

                } if (uPass.isEmpty()) {
                    adminSecond.setError("Please enter your password");
                    adminSecond.setFocusable(true);
                }else {
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(uEmail, uPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.dismiss();
                            checkRole(authResult.getUser().getUid());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        }


    }
    private void checkRole(String uid) {
        DocumentReference documentReference = firestore.collection("Admins").document(uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("admin") != null) {
                    Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Admin does not exist!", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    progressDialog.dismiss();
                }
            }
        });
    }

}