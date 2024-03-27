package com.example.comp_4200_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.JsonObject;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private WordPressAPI api;
    private TrustManager[] trustCerts;
    String username;

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
                username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Perform basic validation
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                login(username, password);
            }
        });
    }

    // Placeholder method to simulate login authentication
    private void login(String username, String password) {
        try {
            trustCerts = new TrustManager[]{new TrustCertificates()};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustCerts, new SecureRandom());
            OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustCerts[0]).hostnameVerifier((hostname, session) -> true).build();
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://simon31.myweb.cs.uwindsor.ca").client(client).addConverterFactory(GsonConverterFactory.create()).build();
            api = retrofit.create(WordPressAPI.class);
            RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), username);
            RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
            Call<JsonObject> call = api.authenticateUser(usernameBody, passwordBody);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        String token = response.body().get("jwt_token").getAsString();
                        navigateToMainActivity(token);
                    } else {
                        failToastMessage();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    failToastMessage();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            failToastMessage();
        }
    }

    // Method to navigate to the main activity
    private void navigateToMainActivity(String jwtToken) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("token", jwtToken);
        intent.putExtra("username", username);
        startActivity(intent);
        finish(); // Finish the LoginActivity to prevent users from going back using the back button
    }

    private void failToastMessage() {
        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }
}
