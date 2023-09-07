package com.example.rayantodo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.rayantodo.db.DatabaseAdapter;
import com.example.rayantodo.db.TodoModel;
import java.util.Objects;

public class AddUpdateActivity extends AppCompatActivity {


    // Declare the UI elements
    private EditText edTitle, edDescription;
    private Button btnAdd;

    // Declare the database adapter
    private DatabaseAdapter dbAdapter;

    // Declare a flag to check if it's an update operation
    private boolean isUpdate = false;

    // Declare a variable to store the ID of the todo item to update
    private int todoIdToUpdate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_udpate);

        // Initialize the database adapter and open the database for writing
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.openDatabase();

        // Initialize UI elements (EditText and Button)
        edTitle = findViewById(R.id.edTitle);
        edDescription = findViewById(R.id.ed_description);
        btnAdd = findViewById(R.id.add_btn);

        // Retrieve data from the intent's bundle (if it exists)
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Check if a todo item ID is provided in the bundle
            todoIdToUpdate = bundle.getInt("id", -1);
            isUpdate = (todoIdToUpdate != -1);

            // If it's an update operation, pre-fill the UI with existing data
            if (isUpdate) {
                String title = bundle.getString("title");
                String description = bundle.getString("description");

                edTitle.setText(title);
                edDescription.setText(description);

                btnAdd.setText("Update");
            }
        }

        // Set a click listener for the "Add" or "Update" button
        btnAdd.setOnClickListener(v -> {
            String title = edTitle.getText().toString();
            String description = edDescription.getText().toString();

            // Check if title or description is empty and show a toast message if so
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please enter all the data", Toast.LENGTH_SHORT).show();
            } else {
                // Perform either an update or an insert operation based on 'isUpdate' flag
                // If it's an update operation, check if the todoIdToUpdate is valid and then update the database
                if (isUpdate) {
                    // Check if the todoIdToUpdate is valid
                    if (todoIdToUpdate != -1) {
                        // Update the database
                        dbAdapter.updateTask(todoIdToUpdate, title, description);
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

                        // Back to the MainActivity after updating
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    // Create a new TodoModel and insert it into the database
                    TodoModel todoModel = new TodoModel(0, title, description);
                    dbAdapter.insertTask(todoModel);
                    Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();

                    // Back to the MainActivity after adding
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    // Override the onResume method to set the title of the activity based on the 'isUpdate' flag
    // If it's an update operation, set the title to "Update Task"
    // If it's an insert operation, set the title to "Add Task"
    @Override
    public void onResume() {
        super.onResume();
        if (isUpdate) {
            Objects.requireNonNull(((AppCompatActivity) this).getSupportActionBar()).setTitle("Update Task");
        } else {
            Objects.requireNonNull(((AppCompatActivity) this).getSupportActionBar()).setTitle("Add Task");
        }
    }

    // Override the onDestroy method to close the database
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.closeDatabase();
    }
}
