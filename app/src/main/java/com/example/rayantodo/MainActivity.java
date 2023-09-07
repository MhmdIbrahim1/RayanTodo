package com.example.rayantodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.rayantodo.adapter.TodoAdapter;
import com.example.rayantodo.db.DatabaseAdapter;
import com.example.rayantodo.db.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnAddTask; // Floating action button for adding tasks
    private TodoAdapter todoAdapter; // Adapter for the RecyclerView
    private DatabaseAdapter dbAdapter; // Database adapter for managing tasks
    List<TodoModel> todoList; // List to hold TodoModel objects for tasks in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database adapter
        dbAdapter = new DatabaseAdapter(this);

        dbAdapter.openDatabase(); // Open the database for writing

        todoAdapter = new TodoAdapter(dbAdapter, this); // Initialize the RecyclerView adapter

        btnAddTask = findViewById(R.id.floatingActionButton); // Initialize the FAB for adding tasks

        // Set an event listener for the button to open the AddUpdateActivity for adding tasks
        btnAddTask.setOnClickListener(v ->{
            // Go  to the AddUpdateActivity for adding tasks
            Intent intent = new Intent(this, AddUpdateActivity.class);
            startActivity(intent);
        });

        // Initialize and set up the RecyclerView
        setupRecyclerView();

        // Set up the ItemTouchHelper for the RecyclerView to handle swipe  for editing tasks
        setUpItemTouchHelperForTaskRv();
    }

    // Method to set up the ItemTouchHelper for the  RecyclerView to handle swipe for editing tasks
    private void setUpItemTouchHelperForTaskRv() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    TodoModel task = todoList.get(position);

                    // Navigate to the AddUpdateActivity for editing the task
                    Intent intent = new Intent(MainActivity.this, AddUpdateActivity.class);
                    intent.putExtra("title", task.getTitle());
                    intent.putExtra("description", task.getDescription());
                    intent.putExtra("id", task.getId());
                    startActivity(intent);
                }
            }
        };

        // Attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView((RecyclerView) findViewById(R.id.recyclerView));
    }

    // Method to set up  the RecyclerView
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager
        recyclerView.setAdapter(todoAdapter); // Set adapter for the RecyclerView

        // Load tasks from the database and set them in the RecyclerView
        todoList = dbAdapter.getAllTasks();
        todoAdapter.setTasks(todoList);
    }
}
