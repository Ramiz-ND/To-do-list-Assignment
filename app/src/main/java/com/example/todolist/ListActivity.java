package com.example.todolist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity_tag";

    private String listTitle;
    private String listId;
    private int listSize;

    private ImageView btnBack;
    private TextView tvListTitle;
    private TextView tvDeleteList;
    private Button btnCreateNewTask;
    private RecyclerView rvTodos;

    private FirebaseFirestore database;

    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        rvTodos = findViewById(R.id.rvTodos);
        btnBack = findViewById(R.id.btnBack);
        tvListTitle = findViewById(R.id.tvListTitle);
        tvDeleteList = findViewById(R.id.tvDeleteList);
        btnCreateNewTask = findViewById(R.id.btnCreateNewTask);

        setupRecycler();

        listTitle = getIntent().getStringExtra("list_title");
        listId = getIntent().getStringExtra("list_id");
        tvListTitle.setText(listTitle);

        database = FirebaseFirestore.getInstance();

        loadList();

        tvDeleteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteList(listId);
            }
        });

        btnCreateNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddNewTaskActivity.class);
                intent.putExtra("title", listTitle);
                intent.putExtra("list_id", listId);
                intent.putExtra("list_size", listSize);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void deleteList(String listId) {
        database.collection("list")
                .document(listId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: delete list " + listTitle);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: delete failed " + e.getMessage());
                    }
                });
    }

    private void setupRecycler() {

        adapter = new TodoAdapter();
        rvTodos.setAdapter(adapter);

        adapter.setOnCheckedListener(new TodoAdapter.OnChecked() {
            @Override
            public void onChecked(final Todo todo) {

                database.collection("task")
                        .document(todo.getId())
                        .update("checked", !todo.isChecked())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: check the todo " + todo.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });
            }

            @Override
            public void onItemClicked(String id, String title, String description, String date) {
                Intent intent = new Intent(ListActivity.this, TaskActivity.class);

                intent.putExtra("task_title", title);
                intent.putExtra("task_description", description);
                intent.putExtra("task_date", date);
                intent.putExtra("task_id", id);
                intent.putExtra("list_id", listId);
                intent.putExtra("list_size", listSize);

                startActivity(intent);
            }
        });

    }

    private void loadList() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        database.collection("task")
                .whereEqualTo("list_id", listId)
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        Log.d(TAG, "onSuccess: list task get all the list");

                        if (error != null) {
                            Log.d(TAG, "onEvent: error " + error.getMessage());
                            return;
                        }

                        if (value != null) {
                            Log.d(TAG, "onEvent: list != null");

                            List<Todo> list = value.toObjects(Todo.class);

                            if (list.size() != 0) {
                                Log.d(TAG, "onEvent: list size " + list.size());

                                adapter.setList(list);
                                listSize = list.size();
                            } else {
                                adapter.setList(list);
                                Toast.makeText(ListActivity.this, "No Todos", Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

    }


}