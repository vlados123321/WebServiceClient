package com.example.lyskin.webserviceclient;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PostActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        Button buttonPost = findViewById(R.id.buttonPost);
        editTextName = findViewById(R.id.editTextUserName);
        editTextJob = findViewById(R.id.editTextUserJob);

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextJob.getText().toString().equals("") ||
                        !editTextName.getText().toString().equals("")){
                    addUser();
                }else {
                    Toast.makeText(PostActivity.this,"Data can't be empty" , Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void addUser(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(MainActivity.apiEndpoint);
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("POST");
                    Map<String,String> data = new HashMap<>();
                    data.put("name",editTextName.getText().toString());
                    data.put("job",editTextJob.getText().toString());

                    JSONObject sendData = new JSONObject(data);

                    httpsURLConnection.setDoOutput(true);
                    httpsURLConnection.getOutputStream().write(sendData.toString().getBytes());

                    if(httpsURLConnection.getResponseCode() == 201){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PostActivity.this,"User created",Toast.LENGTH_LONG).show();
                            }
                        });
                        httpsURLConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}