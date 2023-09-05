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

    FloatingActionButton btnAddTask;
    private TodoAdapter todoAdapter;
    private DatabaseAdapter dbAdapter;

    List<TodoModel> todoList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database adapter
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.openDatabase(); // Open the database
        todoAdapter = new TodoAdapter(dbAdapter, this);

        btnAddTask = findViewById(R.id.floatingActionButton);

        // Set an event listener for the FAB
        btnAddTask.setOnClickListener(v ->{
            Intent intent = new Intent(this, AddUpdateActivity.class);
            startActivity(intent);
        });
        setupRecyclerView();
        setUpItemTouchHelperForTaskRv();
    }

    private void setUpItemTouchHelperForTaskRv() {
        // Create an ItemTouchHelper instance
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
                    // Navigate to the AddUpdateActivity
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

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(todoAdapter);

        todoList = dbAdapter.getAllTasks();
        todoAdapter.setTasks(todoList);

    }
}