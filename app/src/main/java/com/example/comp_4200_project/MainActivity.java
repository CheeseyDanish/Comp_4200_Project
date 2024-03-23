package com.example.comp_4200_project;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    Intent intent;
    String username;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.helloUser);
        intent = getIntent();
        username = intent.getStringExtra("username");
        token = intent.getStringExtra("token");

        tv.setText("Hello, " + username + ". Create a new post or page.");

        // #TODO Create Main activity
    }
}
