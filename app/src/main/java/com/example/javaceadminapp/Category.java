package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Category extends AppCompatActivity {
    RecyclerView recyclerView;
    Button addCategory;
    public static List<CategoryModel> category_list = new ArrayList<>();
    public static int category_index = 0;
    FirebaseFirestore firestore;
    Dialog progressDialog, addPage;
    EditText name;
    Button addBtn;
    CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Modules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.category_recycler);
        addCategory = findViewById(R.id.addbtn);

        progressDialog = new Dialog(Category.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        addPage = new Dialog(Category.this);
        addPage.setContentView(R.layout.add_category_page);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        name = addPage.findViewById(R.id.addNameDialog);
        addBtn = addPage.findViewById(R.id.addBtnDialog);

        firestore = FirebaseFirestore.getInstance();


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.getText().clear();
                addPage.show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Please enter module name");
                    return;
                }
                addNewCategory(name.getText().toString());
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        fetchData();


    }

    private void fetchData() {
        progressDialog.show();
        category_list.clear();

        firestore.collection("Quiz").document("Module").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        long count = (long) documentSnapshot.get("Exist");

                        for (int i = 1; i<= count; i++) {
                            String categoryName = documentSnapshot.getString("Module"+String.valueOf(i)+"_name");
                            String categoryId = documentSnapshot.getString("Module"+String.valueOf(i)+"_Id");
                            category_list.add(new CategoryModel(categoryId, categoryName, "0", "1"));
                        }
                        categoryAdapter = new CategoryAdapter(category_list);
                        recyclerView.setAdapter(categoryAdapter);
                    }
                    else {
                        Toast.makeText(Category.this, "No data exist", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else {
                    Toast.makeText(Category.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void addNewCategory(String categoryName) {
        addPage.dismiss();
        progressDialog.show();

        Map<String, Object> categoryData = new ArrayMap<>();
        categoryData.put("Module", categoryName);
        categoryData.put("Submodule", 0);
        categoryData.put("Base", "1");
        String document_id = firestore.collection("Quiz").document().getId();

        firestore.collection("Quiz").document(document_id).set(categoryData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Map<String, Object> categoryDate = new ArrayMap<>();
                categoryDate.put("Module"+String.valueOf(category_list.size() + 1) + "_name", categoryName);
                categoryDate.put("Module"+String.valueOf(category_list.size() + 1) + "_Id", document_id);
                categoryDate.put("Exist", category_list.size() + 1);

                firestore.collection("Quiz").document("Module").update(categoryDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Category.this, "Module added successfully", Toast.LENGTH_LONG).show();

                        category_list.add(new CategoryModel(document_id, categoryName, "0", "1"));

                        categoryAdapter.notifyItemInserted(category_list.size());

                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Category.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Category.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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