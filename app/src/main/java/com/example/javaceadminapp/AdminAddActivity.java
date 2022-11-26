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
import android.view.MenuItem;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminAddActivity extends AppCompatActivity {

    Dialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    EditText adminName, adminEmail, adminPassword;
    TextView registerText;
    Button registerAdmin;
    String aid, aname, aemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);


        adminName = findViewById(R.id.adminUsername);
        adminEmail = findViewById(R.id.adminEmail);
        adminPassword = findViewById(R.id.adminPassword);
        registerAdmin = findViewById(R.id.adminRegBtn);

        registerText = findViewById(R.id.registerText);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(AdminAddActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            registerText.setText("Update Admin");
            registerAdmin.setText("Update");
            aid = bundle.getString("aid");
            aname = bundle.getString("aname");
            aemail = bundle.getString("aemail");

            adminName.setText(aname);
            adminEmail.setText(aemail);
            adminPassword.setVisibility(View.GONE);
        } else {
            registerAdmin.setText("Register");
        }

        registerAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                 if (bundle1 != null) {
                    String id = aid;
                    String name = adminName.getText().toString();
                    String email = adminEmail.getText().toString();

                    updateAdmin(id, name, email);
                }else {
                     if (adminName.getText().toString().isEmpty()) {
                         adminName.setError("Please enter admin name");
                         return;
                     } if (adminPassword.getText().toString().isEmpty() || adminPassword.getText().toString().length() <= 7) {
                         adminPassword.setError("Password should not be empty and more than 7");
                         return;
                     } if (adminEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(adminEmail.getText().toString()).matches()) {
                         adminEmail.setError("Invalid email");
                         return;
                     }
                     addAdmin(adminName.getText().toString(), adminEmail.getText().toString(), adminPassword.getText().toString());
                }

            }
        });



    }

    private void updateAdmin(String id, String name, String email) {
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email);
        firestore.collection("Admins").document(id).update("name", name, "email", email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminAddActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminAddActivity.this, Admin.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(AdminAddActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void addAdmin(String name, String email, String password) {
        progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(adminEmail.getText().toString(), adminPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String uid = firebaseUser.getUid();

                    DocumentReference documentReference = firestore.collection("Admins").document(firebaseUser.getUid());

                    Map<String, Object> admin_data = new HashMap<>();
                    admin_data.put("uid", uid);
                    admin_data.put("email", email);
                    admin_data.put("name", name);
                    admin_data.put("admin", "yes");

                    documentReference.set(admin_data);

                    progressDialog.dismiss();
                    Toast.makeText(AdminAddActivity.this, "Admin added successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminAddActivity.this, Admin.class);
                    startActivity(intent);
                    finish();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAddActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}