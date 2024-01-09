package com.example.fliopp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team10project.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EditPassword extends AppCompatActivity {
    private Map<String, String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);
        users = (Map<String, String>) getIntent().getSerializableExtra("users");
    }


    public void onReturnButtonClick(View v) {
        Intent i = new Intent();
        i.putExtra("RETURN_STATUS", "Successfully returned");
        setResult(RESULT_OK, i);
        finish();
    }

    public void onEditPasswordSubmit(View v) {
        EditText usernamefield = findViewById(R.id.UsernameText);
        EditText passwordfield = findViewById(R.id.editPassword);
        String name = usernamefield.getText().toString();
        String newPassword = passwordfield.getText().toString();

        updatePassword(name, newPassword);
        //notify the user that their password was updated

    }

    public void updatePassword(String name, String newPassword) {
        if (users.containsKey(name)) {
            users.put(name, newPassword);

            //notifies the user of the update!
            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Username not found", Toast.LENGTH_LONG).show();
        }
        connecttoMongo(name,newPassword);
    }

    protected void connecttoMongo(String name, String password) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    //reminder: getAll is an endpoint that returns JSON Array of programs
                    URL url = new URL("http://10.0.2.2:3010/changePassword?name="+name+"&newPassword="+password);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();
                    JSONObject jo = new JSONObject(response);

//                    JSONArray profileArr = new JSONArray(response);
//                    for (int i = 0; i < profileArr.length(); i++) {
//                        JSONObject readProfile = profileArr.getJSONObject(i);
//                        Profile newProfile = createProfile(readProfile);
//                        profiles.put(newProfile.getUsername(), newProfile);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //e.toString();
                }
            });
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

