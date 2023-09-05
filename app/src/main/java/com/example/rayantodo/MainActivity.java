package com.example.rayantodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Set up the ActionBar
        NavigationUI.setupActionBarWithNavController(this, navController);


    }
    @Override
    public boolean onSupportNavigateUp() {
        // Initialize NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Return the navigateUp() method
        return navController.navigateUp();
    }
}