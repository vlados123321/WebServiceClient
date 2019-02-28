package com.example.lyskin.webserviceclient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class GetActivity extends AppCompatActivity {


    private static InputStream inputStream;
    private EditText editTextUserId;
    private TextView textViewUserDataGet;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        Button buttonGet = findViewById(R.id.buttonGet);

        editTextUserId = findViewById(R.id.editTextUserGetID);
        textViewUserDataGet = findViewById(R.id.textViewUserDataGet);


        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextUserId.getText().toString().equals("")) {
                    fetchUser();
                }else {
                    editTextUserId.setError("Id can't be empty");
                }
            }
        });

    }

    private void fetchUser(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://fakerestapi.azurewebsites.net/api/Users/" + editTextUserId.getText().toString());
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    if(httpsURLConnection.getResponseCode() == 200){
                        inputStream = httpsURLConnection.getInputStream();
                        retriveData();
                        httpsURLConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private void retriveData() {
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    JsonReader jsonReader = new JsonReader(inputStreamReader);

                    jsonReader.beginObject();
                    while (jsonReader.hasNext()){
                        String key = jsonReader.nextName();
                        if(key.equals("UserName")){
                            String value = jsonReader.nextString();
                            textViewUserDataGet.setText("User name: " + value + "\n");
                        }else  if(key.equals("Password")){
                            String value = jsonReader.nextString();
                            textViewUserDataGet.append("Password: " + value);
                        }else {
                            jsonReader.skipValue();
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }
}