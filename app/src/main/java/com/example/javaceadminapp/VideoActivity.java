package com.example.javaceadminapp;

import static com.example.javaceadminapp.TopicActivity.moduleidfromtopicactivity;
import static com.example.javaceadminapp.TopicActivity.subidfromtopicactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.HashMap;
import java.util.Map;

public class VideoActivity extends AppCompatActivity {

    Button add_url_button, showAddDialog, goToQuestionsActivity;
    Dialog addPage, progressDialog;
    YouTubePlayerView youTubePlayerView;
    EditText video_url;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        youTubePlayerView = findViewById(R.id.youtube_player_view);
        firestore = FirebaseFirestore.getInstance();

        progressDialog = new Dialog(VideoActivity.this);
        progressDialog.setContentView(R.layout.loading_progressbar);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.progressbar_background);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addPage = new Dialog(VideoActivity.this);
        addPage.setContentView(R.layout.video_add_page);
        addPage.setCancelable(true);
        addPage.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        video_url = addPage.findViewById(R.id.video_url);
        add_url_button = addPage.findViewById(R.id.add_url_button);

        showAddDialog = findViewById(R.id.showAddDialog);
        goToQuestionsActivity = findViewById(R.id.goToQuestionsActivity);

        goToQuestionsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoActivity.this, Questions.class));
                finish();
            }
        });



        showAddDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPage.show();
            }
        });

        add_url_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video_url.getText().toString().isEmpty()) {
                    video_url.setText("Enter Youtube ID");
                    return;
                }
                addVideoId(video_url.getText().toString());
            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                firestore.collection("Quizzes").document(moduleidfromtopicactivity).collection(subidfromtopicactivity).document("Video").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String videoId = documentSnapshot.getString("video_id");
                            assert videoId != null;
                            youTubePlayer.cueVideo(videoId, 0);
                        } else {
                            String noVid = "RickRoll";
                            youTubePlayer.cueVideo(noVid, 0);
                        }
                    }
                });
            }
        });



    }

    private void addVideoId(String yid) {
        addPage.dismiss();
        progressDialog.show();
        Map<String, Object> data = new HashMap<>();
        data.put("video_id", yid);

        firestore.collection("Quizzes").document(moduleidfromtopicactivity).collection(subidfromtopicactivity).document("Video").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(VideoActivity.this, "Successfully added a Youtube video", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VideoActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}