package com.example.javaceadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangeModuleActivity extends AppCompatActivity {

    Dialog progressDialog;
    String moduleId, moduleName, modulePreview;
    EditText etModuleName, etModulePreview;
    Button updateBtn;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_module);


        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(ChangeModuleActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        etModuleName = findViewById(R.id.updateModuleName);
        etModulePreview = findViewById(R.id.updateModulePreview);
        etModulePreview.setMovementMethod(new ScrollingMovementMethod());

        updateBtn = findViewById(R.id.updateModule);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            moduleId = bundle.getString("moduleid");
            moduleName = bundle.getString("modulename");
            modulePreview = bundle.getString("modulepreview");
            etModuleName.setText(moduleName);
            etModulePreview.setText(modulePreview);

            updateBtn.setText("Update Module");
        } else {
            updateBtn.setText("Add Module");
        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();

                if (bundle1 != null) {
                    String mid = moduleId;
                    String mname = etModuleName.getText().toString();
                    String mpreview = etModulePreview.getText().toString();

                    updateModule(mid, mname, mpreview);
                } else {
                    if (etModuleName.getText().toString().isEmpty()) {
                        etModuleName.setError("Please enter module name");
                        return;
                    } if (etModulePreview.getText().toString().isEmpty()) {
                        etModulePreview.setError("Please enter module preview");
                        return;
                    }
                    addModule(etModuleName.getText().toString(), etModulePreview.getText().toString());
                }
            }
        });


    }
    private void addModule(String moduleName, String modulePreview) {
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
                progressDialog.dismiss();
                Toast.makeText(ChangeModuleActivity.this, "Successfully added " + moduleName + " module", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChangeModuleActivity.this, Module.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ChangeModuleActivity.this, "Failed to add module", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateModule(String id, String name, String email) {
        progressDialog.show();

        firestore.collection("Quizzes").document(id).update("module_name", name, "module_message", email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(ChangeModuleActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ChangeModuleActivity.this, Module.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(ChangeModuleActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}