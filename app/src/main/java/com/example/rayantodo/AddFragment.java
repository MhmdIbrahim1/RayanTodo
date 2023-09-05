package com.example.rayantodo;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.rayantodo.db.DatabaseAdapter;
import com.example.rayantodo.db.TodoModel;

import java.util.Objects;


public class AddFragment extends Fragment {

    public static final String TAG = "AddFragment";
    private EditText edTitle, edDescription;
    private Button btnAdd;

    private DatabaseAdapter dbAdapter;

    private boolean isUpdate = false;

    private int todoIdToUpdate = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_add, container, false);
        dbAdapter = new DatabaseAdapter(requireContext()); // Initialize your database adapter
        dbAdapter.openDatabase(); // Open the database for writing

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edTitle = view.findViewById(R.id.edTitle);
        edDescription = view.findViewById(R.id.ed_description);
        btnAdd = view.findViewById(R.id.add_btn);

        Bundle bundle = getArguments();

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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edTitle.getText().toString();
                String description = edDescription.getText().toString();

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter all the data", Toast.LENGTH_SHORT).show();
                } else {
                    if (isUpdate) {
                        if (todoIdToUpdate != -1) {
                            dbAdapter.updateTask(todoIdToUpdate, title, description);
                            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(v).navigate(R.id.action_addFragment_to_listFragment);
                        }
                    } else {
                        TodoModel todoModel = new TodoModel(0, title, description);
                        dbAdapter.insertTask(todoModel);
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.action_addFragment_to_listFragment);
                    }
                }

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isUpdate) {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Update Task");
        } else {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Add Task");

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbAdapter.closeDatabase(); // Close the database
    }

}