package com.example.rayantodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rayantodo.R;
import com.example.rayantodo.db.DatabaseAdapter;
import com.example.rayantodo.db.TodoModel;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private final Context context;
    private static DatabaseAdapter dbAdapter; // Static variable for database adapter
    private List<TodoModel> todoList; // List to hold TodoModel objects


    // Constructor for TodoAdapter
    public TodoAdapter(DatabaseAdapter dbAdapter, Context context) {
        this.context = context;
        TodoAdapter.dbAdapter = dbAdapter; // Initialize the database adapter
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout.xml layout as the item view for the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoViewHolder holder, int position) {

        dbAdapter.openDatabase(); // Open the database

        TodoModel todo = todoList.get(position); // Get the TodoModel object at the given position

        // Set the task title, status, and description
        holder.taskStatus.setText(todo.getDescription());
        holder.taskStatus.setChecked(toBoolean(todo.getStatus()));
        holder.taskTitle.setText(todo.getTitle());

        // Set an event listener for CheckBox state changes
        holder.taskStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                dbAdapter.updateTaskStatus(todo.getId(), 1); // Update task status in the database
            } else {
                dbAdapter.updateTaskStatus(todo.getId(), 0); // Update task status in the database
            }
        });

        // Set an event listener for deleting a task
        holder.deleteTask.setOnClickListener(v -> {
            dbAdapter.deleteTask(todo.getId()); // Delete the task from the database
            Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show(); // Display a toast message
            todoList.remove(position); // Remove the task from the list
            notifyItemRemoved(position); // Notify the adapter of data changes
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size(); // Return the size of the list
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle; // TextView for task title
        CheckBox taskStatus; // CheckBox for task status
        ImageView deleteTask; // ImageView for deleting a task

        // Constructor for TodoViewHolder
        public TodoViewHolder(@NonNull View parent) {
            super(parent);
            taskTitle = parent.findViewById(R.id.todo_title); // Initialize task title TextView
            taskStatus = parent.findViewById(R.id.todo_cb); // Initialize task status CheckBox
            deleteTask = parent.findViewById(R.id.delete_image); // Initialize delete task ImageView
        }
    }

    // Helper method to convert an integer to a boolean
    private boolean toBoolean(int n) {
        return n != 0;
    }

    // Method to set the list of tasks and notify the adapter of data changes
    public void setTasks(List<TodoModel> todoList) {
        this.todoList = todoList; // Set the new list of tasks
        notifyDataSetChanged(); // Notify the adapter of data changes
    }
}
