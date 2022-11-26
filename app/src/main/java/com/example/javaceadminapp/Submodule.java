package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

public class Submodule extends AppCompatActivity {

    Dialog progressDialog, addPage;
    EditText submoduleName, subModulePreivew;
    Button submoduleButton, showSubmoduleDialog;
    String moduleId, moduleName;
    Long submodules;
    FirebaseFirestore firestore;
    List<SubmoduleModel> submoduleModelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SubmoduleAdapter submoduleAdapter;
    List<String> subId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submodule);

        Bundle bundle = getIntent().getExtras();
        moduleId = bundle.getString("moduleid");
        moduleName = bundle.getString("modulename");
        submodules = bundle.getLong("submodules");

        firestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.submodule_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(moduleName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new Dialog(Submodule.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addPage = new Dialog(Submodule.this);
        addPage.setContentView(R.layout.add_submodule_page);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        submoduleName = addPage.findViewById(R.id.addSubmoduleName);
        subModulePreivew = addPage.findViewById(R.id.addSubmodulePreview);
        subModulePreivew.setMovementMethod(new ScrollingMovementMethod());
        submoduleButton = addPage.findViewById(R.id.addSubModuleBtnDialog);


        recyclerView = findViewById(R.id.submodule_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showSubmoduleDialog = findViewById(R.id.addSubmodule);

        showSubmoduleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submoduleName.getText().clear();
                subModulePreivew.getText().clear();
                addPage.show();
            }
        });

        submoduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submoduleName.getText().toString().isEmpty()) {
                    submoduleName.setError("Please enter submodule name");
                    return;
                } if (subModulePreivew.getText().toString().isEmpty()) {
                    subModulePreivew.setError("Please enter submodule preview");
                    return;
                }
                addSubmodule(submoduleName.getText().toString(), subModulePreivew.getText().toString());
            }
        });

        fetchSubmodules();

    }
    private void fetchSubmodules() {

        progressDialog.show();
        firestore.collection("Quizzes").document(moduleId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                submoduleModelList.clear();
                long existingSubs = documentSnapshot.getLong("submodules");
                for (int i  = 1; i <= existingSubs; i++) {
                    SubmoduleModel submoduleModel = new SubmoduleModel(documentSnapshot.getString("submodule" + String.valueOf(i) + "_id"),
                            documentSnapshot.getString("submodule" + String.valueOf(i) + "_preview"));
                    submoduleModelList.add(submoduleModel);
                }
                progressDialog.dismiss();
                submoduleAdapter = new SubmoduleAdapter(Submodule.this, submoduleModelList);
                recyclerView.setAdapter(submoduleAdapter);
            }
        });
    }

    private void addSubmodule(String name, String preview) {
        addPage.dismiss();
        progressDialog.show();

        Map<String, Object> sub_data = new HashMap<>();
        sub_data.put("submodule" + (submodules + 1) + "_id", name);
        sub_data.put("submodule" + (submodules + 1) + "_preview", preview);
        sub_data.put("submodules", submodules + 1);

        firestore.collection("Quizzes").document(moduleId).update(sub_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
               Map<String, Object> sub1_data = new HashMap<>();
               sub1_data.put("QNO", 0);

               firestore.collection("Quizzes").document(moduleId).collection(name).document("Question_List").set(sub1_data)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Map<String, Object> sub2_data = new HashMap<>();
                               sub2_data.put("topic_title", name);
                               sub2_data.put("topic_content", preview);

                               firestore.collection("Quizzes").document(moduleId).collection(name).document("Topic_List")
                                       .set(sub2_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {
                                               progressDialog.dismiss();
                                               Toast.makeText(Submodule.this, "Submodule added successfully", Toast.LENGTH_SHORT).show();
                                               fetchSubmodules();
                                           }
                                       });
                           }
                       });
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