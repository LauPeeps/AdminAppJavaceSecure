package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePassword extends AppCompatActivity {

    Dialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    EditText oldPass, newPass;
    Button updatePassBtn;
    String aid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        progressDialog = new Dialog(ChangePassword.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        newPass = findViewById(R.id.newPass);
        updatePassBtn = findViewById(R.id.updatePass);

        Bundle bundle = getIntent().getExtras();

        aid = bundle.getString("aid");

        updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (newPass.getText().toString().isEmpty()) {
                    newPass.setError("New password cannot be empty");
                    return;
                }
                updatePassword(aid, newPass.getText().toString());
            }
        });



    }

    private void updatePassword(String id, String newp) {
        progressDialog.show();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(newp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(ChangePassword.this, "Password updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChangePassword.this, Admin.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ChangePassword.this, "Unable to update password", Toast.LENGTH_SHORT).show();
            }
        });

    }


}