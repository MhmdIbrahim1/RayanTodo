package com.example.rayantodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rayantodo.db.DatabaseAdapter;
import com.example.rayantodo.db.TodoModel;

import java.util.Objects;

public class AddUpdateActivity extends AppCompatActivity {

    private EditText edTitle, edDescription;
    private Button btnAdd;

    private DatabaseAdapter dbAdapter;

    private boolean isUpdate = false;

    private int todoIdToUpdate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_udpate);


        dbAdapter = new DatabaseAdapter(this); // Initialize your database adapter
        dbAdapter.openDatabase(); // Open the database for writing

        edTitle = findViewById(R.id.edTitle); // Add this line to initialize edTitle
        edDescription = findViewById(R.id.ed_description);
        btnAdd = findViewById(R.id.add_btn);

        edDescription = findViewById(R.id.ed_description);
        btnAdd = findViewById(R.id.add_btn);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            todoIdToUpdate = bundle.getInt("id", -1);
            isUpdate = (todoIdToUpdate != -1);

            if (isUpdate) {
                String title = bundle.getString("title");
                String description = bundle.getString("description");

                edTitle.setText(title);
                edDescription.setText(description);

                btnAdd.setText("Update");
            }
        }

        btnAdd.setOnClickListener(v -> {
            String title = edTitle.getText().toString();
            String description = edDescription.getText().toString();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please enter all the data", Toast.LENGTH_SHORT).show();
            } else {
                if (isUpdate) {
                    if (todoIdToUpdate != -1) {
                        dbAdapter.updateTask(todoIdToUpdate, title, description);
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    TodoModel todoModel = new TodoModel(0, title, description);
                    dbAdapter.insertTask(todoModel);
                    Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isUpdate) {
            Objects.requireNonNull(((AppCompatActivity) this).getSupportActionBar()).setTitle("Update Task");
        } else {
            Objects.requireNonNull(((AppCompatActivity) this).getSupportActionBar()).setTitle("Add Task");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.closeDatabase();
    }
}