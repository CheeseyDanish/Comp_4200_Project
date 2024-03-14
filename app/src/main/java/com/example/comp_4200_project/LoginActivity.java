package com.example.comp_4200_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Perform basic validation
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Perform login (for now, just show a success message)
                // In the future, replace this with actual login logic using WordPress API
                if (isCredentialsValid(username, password)) {
                    // If login is successful, navigate to the next activity (e.g., MainActivity)
                    navigateToMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Placeholder method to simulate login authentication #TODO connect API
    private boolean isCredentialsValid(String username, String password) {
        // We will authenticate with WordPress API here when that is setup later
        // For now, let's assume username is "admin" and password is "password"
        return username.equals("admin") && password.equals("password");
    }

    // Method to navigate to the main activity
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the LoginActivity to prevent users from going back using the back button
    }
}
