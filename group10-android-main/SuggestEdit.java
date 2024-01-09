package com.example.fliopp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Set;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.net.*;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Map;

public class SuggestEdit extends AppCompatActivity{
    private String host;
    private int port;

    protected String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestedit);

        Spinner spinner = (Spinner) findViewById(R.id.editFeature);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.editFeature_array,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
    }

    public void onHomePageButtonClick(View v){
        finish();
    }
    public void onSubmitEditButtonClick(View v) {
        EditText programName = findViewById(R.id.programName);
        String programNameStr = programName.getText().toString();
        EditText suggestedEdit = findViewById(R.id.suggestedEdit);
        String suggestedEditStr = suggestedEdit.getText().toString();

        Spinner editFeature = (Spinner) findViewById(R.id.editFeature);
        String editFeatureStr = editFeature.getSelectedItem().toString();

        if(suggestedEditStr.isEmpty() || programNameStr.isEmpty()){
            Toast.makeText(this, "Enter a Program and Suggested Edit to submit!", Toast.LENGTH_LONG).show();
            return;
        }

        //make a String that will be your URL and add ids to it
        try{
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try{
                    String urlString = "http://10.0.2.2:3010/suggestEdit?";

                    //"http://" + host + ":" + port + "/suggestEdit?";
                    //THE ENDPOINT IS GOING TO BE CALLED suggestEdit
                    urlString += ("programName=" + programNameStr);
                    urlString += "&";
                    urlString += ("suggestedEdit=" + suggestedEditStr);
                    Log.v("CHECK FOR THIS STRING", urlString);
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();

                    JSONObject jo = new JSONObject(response);

                    message = jo.getString("status");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                    // now the response comes back
//                    int responsecode = conn.getResponseCode();
//                    if (responsecode != 200) {
//                        Toast.makeText(this, "boo error occurred " + responsecode, Toast.LENGTH_LONG).show();
                    }
                catch(Exception e){
                    Toast.makeText(this, "boo error in first catch", Toast.LENGTH_LONG).show();
                }
            });
            executor.awaitTermination(2, TimeUnit.SECONDS);
        }
        catch(Exception e){
            Toast.makeText(this, "boo error occurred try again", Toast.LENGTH_LONG).show();
        }

        // make sure the response has "200 OK" as the status
//        Intent i = new Intent();
//        i.putExtra("RETURN_STATUS", "Successfully submitted edit");
//        setResult(RESULT_OK, i);
        Toast.makeText(this, "successfully connect", Toast.LENGTH_LONG).show();
        //finish();
    }
}
