package com.example.comp_4200_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private WordPressAPI api;

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

                login(username, password);

                // Perform login (for now, just show a success message)
                // In the future, replace this with actual login logic using WordPress API
                /*if (isCredentialsValid(username, password)) {
                    // If login is successful, navigate to the next activity (e.g., MainActivity)
                    navigateToMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    // Placeholder method to simulate login authentication #TODO connect API
    private void login(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://wp.davenpos.myweb.cs.uwindsor.ca/wp/").addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(WordPressAPI.class);
        Call<JsonObject> call = api.authenticateUser(username, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    navigateToMainActivity();
                } else {
                    failToastMessage();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                failToastMessage();
            }
        });
    }

    // Method to navigate to the main activity
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the LoginActivity to prevent users from going back using the back button
    }

    private void failToastMessage() {
        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }
}
