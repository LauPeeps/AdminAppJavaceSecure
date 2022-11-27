package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Module extends AppCompatActivity {

    FirebaseFirestore firestore;
    Button showDialog, addBtnDialog;
    Dialog progressDialog, addPage;
    EditText moduleName, modulePreview;

    List<ModuleModel> moduleModelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ModuleAdapter moduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        Toolbar toolbar = findViewById(R.id.module_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Modules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(Module.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        recyclerView = findViewById(R.id.module_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showDialog = findViewById(R.id.addModuleBtn);

        addPage = new Dialog(Module.this);
        addPage.setContentView(R.layout.add_module_page);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        moduleName = addPage.findViewById(R.id.addModuleName);
        modulePreview = addPage.findViewById(R.id.addModuleMessage);
        addBtnDialog = addPage.findViewById(R.id.addModuleBtnDialog);

        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moduleName.getText().clear();
                modulePreview.getText().clear();
                addPage.show();
            }
        });

        addBtnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moduleName.getText().toString().isEmpty()) {
                    moduleName.setError("Please enter module name");
                    return;
                } if (modulePreview.getText().toString().isEmpty()) {
                    modulePreview.setError("Please enter module message");
                    return;
                }
                addModule(moduleName.getText().toString(), modulePreview.getText().toString());
            }
        });

        fetchModules();

    }

    private void fetchModules() {
        progressDialog.show();

        firestore.collection("Quizzes").orderBy("module_created").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                moduleModelList.clear();
                progressDialog.dismiss();

                for (DocumentSnapshot documentSnapshot: task.getResult()) {
                    ModuleModel moduleModel = new ModuleModel(documentSnapshot.getString("module_id"),
                            documentSnapshot.getString("module_name"),
                            documentSnapshot.getString("module_message"),
                            documentSnapshot.getTimestamp("module_created"),
                            documentSnapshot.getLong("submodules"));
                    moduleModelList.add(moduleModel);
                }
                moduleAdapter = new ModuleAdapter(Module.this, moduleModelList);

                recyclerView.setAdapter(moduleAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void addModule(String moduleName, String modulePreview) {
        addPage.dismiss();
        progressDialog.show();

        String document_id = firestore.collection("Quizzes").document().getId();
        Map<String, Object> module_data = new HashMap<>();
        module_data.put("module_id", document_id);
        module_data.put("module_name", moduleName);
        module_data.put("module_message", modulePreview);
        module_data.put("module_created", FieldValue.serverTimestamp());
        module_data.put("submodules", 0);


        firestore.collection("Quizzes").document(document_id).set(module_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                fetchModules();
                progressDialog.dismiss();
                Toast.makeText(Module.this, "Successfully added " + moduleName + " module", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Module.this, "Failed to add module", Toast.LENGTH_SHORT).show();
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