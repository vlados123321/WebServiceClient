package com.example.lyskin.webserviceclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class PutActivity extends AppCompatActivity {


    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextJob;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);


        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        editTextId = findViewById(R.id.editTextPutID);
        editTextJob = findViewById(R.id.editTextPutJob);
        editTextName = findViewById(R.id.editTextPutName);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextId.getText().toString().equals("") ||
                        !editTextName.getText().toString().equals("")||
                        !editTextJob.getText().toString().equals("")){
                    updateUser();
                }else {
                    Toast.makeText(PutActivity.this,"Enter all data",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateUser(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(MainActivity.apiEndpoint + "/" +
                            editTextId.getText().toString());
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("PUT");
                    Map<String,String> data = new HashMap<>();
                    data.put("name",editTextName.getText().toString());
                    data.put("job",editTextJob.getText().toString());

                    JSONObject updateData = new JSONObject(data);
                    httpsURLConnection.setDoOutput(true);
                    httpsURLConnection.getOutputStream().write(updateData.toString().getBytes());

                    if(httpsURLConnection.getResponseCode() == 200){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PutActivity.this, "User updated",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
