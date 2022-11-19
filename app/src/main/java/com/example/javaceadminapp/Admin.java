package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Admin extends AppCompatActivity {
    Dialog progressDialog, addPage;
    Button addAdmin, registerAdmin;
    FirebaseAuth firebaseAuth;
    EditText adminName, adminEmail, adminPassword;

    List<AdminModel> adminModelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firestore;
    AdminAdapter adminAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(Admin.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        addPage = new Dialog(Admin.this);
        addPage.setContentView(R.layout.add_admin_page);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        addAdmin = findViewById(R.id.addAdminBtn);

        adminName = addPage.findViewById(R.id.adminName);
        adminEmail = addPage.findViewById(R.id.adminEmail);
        adminPassword = addPage.findViewById(R.id.adminPassword);
        registerAdmin = addPage.findViewById(R.id.registerAdmin);


        recyclerView = findViewById(R.id.admin_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPage.show();
            }
        });

        registerAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });


        fetchAdmin();


    }

    private void fetchAdmin() {
        progressDialog.show();
        firestore.collection("Admins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressDialog.dismiss();

                for (DocumentSnapshot documentSnapshot: task.getResult()) {
                    AdminModel adminModel = new AdminModel(documentSnapshot.getString("uid"),
                            documentSnapshot.getString("name"),
                            documentSnapshot.getString("email"));
                    adminModelList.add(adminModel);
                }
                adminAdapter = new AdminAdapter(Admin.this, adminModelList);

                recyclerView.setAdapter(adminAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void addAdmin(String name, String email, String password) {
        addPage.dismiss();
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

                    Toast.makeText(Admin.this, "Admin added successfully", Toast.LENGTH_SHORT).show();

                    adminAdapter.notifyItemInserted(adminModelList.size());

                    progressDialog.dismiss();

                    finish();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Admin.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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