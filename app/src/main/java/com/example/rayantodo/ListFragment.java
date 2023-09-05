package com.example.rayantodo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rayantodo.adapter.TodoAdapter;
import com.example.rayantodo.db.DatabaseAdapter;
import com.example.rayantodo.db.TodoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class ListFragment extends Fragment {
    FloatingActionButton btnAddTask;
    private TodoAdapter todoAdapter;
    private DatabaseAdapter dbAdapter;

    List<TodoModel> todoList ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize the database adapter
        dbAdapter = new DatabaseAdapter(getContext());
        dbAdapter.openDatabase(); // Open the database
        todoAdapter = new TodoAdapter(dbAdapter, getContext());
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnAddTask = view.findViewById(R.id.floatingActionButton);

        setupRecyclerView(view);
        setUpItemTouchHelperForTaskRv(view);

        // Set an event listener for the FAB
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_listFragment_to_addFragment);
            }
        });
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(todoAdapter);

        // Get all tasks from the database
       todoList = dbAdapter.getAllTasks();
        todoAdapter.setTasks(todoList); // Set the retrieved tasks to the adapter
    }

    // swipe to edit
    private void setUpItemTouchHelperForTaskRv(View view) {
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
                    // Navigate to the edit fragment
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", task.getId());
                    bundle.putString("title", task.getTitle());
                    bundle.putString("description", task.getDescription());
                    Navigation.findNavController(viewHolder.itemView).navigate(R.id.action_listFragment_to_addFragment, bundle);
                }
            }
        };

        // Attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView((RecyclerView) view.findViewById(R.id.recyclerView));
    }
}