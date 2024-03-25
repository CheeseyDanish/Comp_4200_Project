package com.example.comp_4200_project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    Intent intent;
    String username, token, title, content, postType;
    private WordPressAPI api;
    Button publish;
    EditText etTitle, etContent;
    RadioGroup rg;
    RadioButton checkedBtn;
    private TrustManager[] trustCerts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.helloUser);
        intent = getIntent();
        username = intent.getStringExtra("username");
        token = intent.getStringExtra("token");
        publish = findViewById(R.id.buttonPublish);
        etTitle = findViewById(R.id.editTextTitle);
        etContent = findViewById(R.id.editTextContent);
        rg = findViewById(R.id.radioGroupType);
        postType = "None selected";

        tv.setText("Hello, " + username + ". Create a new post or page.");

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postType.equals("None selected")) {
                    showToastMessage("Please select a post type.");
                } else {
                    title = etTitle.getText().toString();
                    content = etContent.getText().toString();
                    //Much of the following code is identical to that in the LoginActivity, with the most notable exception being the addition of the .addInterceptor part. Perhaps both these files can pull from the same file somehow to avoid code duplication?
                    try {
                        trustCerts = new TrustManager[]{new TrustCertificates()};
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustCerts, new SecureRandom());
                        OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustCerts[0]).hostnameVerifier((hostname, session) -> true).addInterceptor(new Interceptor() {
                            @NotNull
                            @Override
                            public Response intercept(@NotNull Chain chain) throws IOException {
                                Request original = chain.request();
                                Request.Builder rb = original.newBuilder().header("Authorization", "Bearer " + token).method(original.method(), original.body());
                                Request req = rb.build();
                                return chain.proceed(req);
                            }
                        }).build();
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://simon31.myweb.cs.uwindsor.ca").client(client).addConverterFactory(GsonConverterFactory.create()).build();
                        api = retrofit.create(WordPressAPI.class);
                        createPost();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastMessage("There was a problem creating the post.");
                    }
                }
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkedBtn = (RadioButton)findViewById(i);
                if (checkedBtn != null) {
                    postType = checkedBtn.getText().toString();
                }
            }
        });

        // #TODO Create Main activity
    }

    private void showToastMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void createPost() {
        Call<JsonObject> call;
        if (postType.equals("Post")) {
            call = api.createPost(title, content, "publish");
        } else {
            call = api.createPage(title, content, "publish");
        }
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                //Maybe take user to new activity that says "Post successfully created with a link to the new activity [which can be retrieved via response.body().get("link").getAsString())].
                showToastMessage("New post successfully created!");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showToastMessage("There was a problem creating the post.");
            }
        });
    }
}
