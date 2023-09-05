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

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder>{
    private final Context context;
    private static DatabaseAdapter dbAdapter;
    private List<TodoModel> todoList;

    public TodoAdapter(DatabaseAdapter dbAdapter, Context context) {
        this.context = context;
        TodoAdapter.dbAdapter = dbAdapter;
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
        TodoModel todo = todoList.get(position);
        holder.taskStatus.setText(todo.getDescription());
        holder.taskStatus.setChecked(toBoolean(todo.getStatus()));
        holder.taskTitle.setText(todo.getTitle());

        // Set an event listener for CheckBox state changes
        holder.taskStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                dbAdapter.updateTaskStatus(todo.getId(), 1);
            } else {
                dbAdapter.updateTaskStatus(todo.getId(), 0);
            }
        });

        // Set an event listener for deleting a task
        holder.deleteTask.setOnClickListener(v -> {
            dbAdapter.deleteTask(todo.getId());
            int itemPosition = todoList.indexOf(todo);
            if (itemPosition != -1) {
                todoList.remove(itemPosition);
                notifyItemRemoved(itemPosition);
                Toast.makeText(context, "Todo Deleted!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        CheckBox taskStatus;
        ImageView deleteTask;
        public TodoViewHolder(@NonNull View parent) {
            super(parent);
            taskTitle = parent.findViewById(R.id.todo_title);
            taskStatus = parent.findViewById(R.id.todo_cb);
            deleteTask = parent.findViewById(R.id.delete_image);
        }
    }

    private boolean toBoolean(int n) {

        return n != 0;
    }

    public void setTasks(List<TodoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

}
