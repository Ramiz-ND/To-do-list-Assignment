package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class TaskActivity extends AppCompatActivity {
    private static final String TAG = "TaskActivity_tag";
    String task_title;
    String task_description;
    String task_date;
    String task_id;
    String list_id;
    int list_size;

    private ImageView btnBack;
    private EditText etTaskTitle, etTaskDescription;
    private TextView btnEdit;
    private TextView tvDeleteTask;
    private TextView tvDate;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        btnBack = findViewById(R.id.btnBack);
        etTaskTitle = findViewById(R.id.etTaskTitle);
        etTaskDescription = findViewById(R.id.etTaskDescription);
        btnEdit = findViewById(R.id.btnEdit);
        tvDeleteTask = findViewById(R.id.tvDeleteTask);
        tvDate = findViewById(R.id.tvDate);

        database = FirebaseFirestore.getInstance();


        Intent i = getIntent();

        task_title = i.getStringExtra("task_title");
        task_description = i.getStringExtra("task_description");
        task_date = i.getStringExtra("task_date");
        task_id = i.getStringExtra("task_id");
        list_id = i.getStringExtra("list_id");
        list_size = i.getIntExtra("list_size", 0);

        etTaskTitle.setText(task_title);
        etTaskDescription.setText(task_description);

        // date
        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy, HH:mm");
        String stringDate = DateFor.format(Long.parseLong(task_date));
        tvDate.setText(stringDate);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: task id  : " + task_id);
                Log.d(TAG, "onClick: task_date  : " + task_date);

                String title = etTaskTitle.getText().toString().trim();
                String description = etTaskDescription.getText().toString().trim();

                if (!title.equals(task_title)) {
                    database.collection("task")
                            .document(task_id)
                            .update("title", title)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: update task title");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: error " + e.getMessage());
                                }
                            });
                }

                if (!description.equals(task_description)) {
                    database.collection("task")
                            .document(task_id)
                            .update("description", description)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: success to create task");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: error " + e.getMessage());
                                }
                            });
                }
                finish();
            }
        });


        tvDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask(task_id);
            }
        });
    }

    private void deleteTask(final String task_id) {
        database.collection("task")
                .document(task_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: delete task " + task_id);
                        updateListSize();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: delete task " + e.getMessage());
                    }
                });
    }

    private void updateListSize() {
        database.collection("list")
                .document(list_id)
                .update("size", --list_size)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: update");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: failed " + e.getMessage());
                    }
                });
    }
}