package com.example.fliopp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.*;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final int HOME_ACTIVITY_ID = 1;
    public static final int REGISTER_ACTIVITY_ID = 2;
    public static final int FILTER_ACTIVITY_ID = 3;

    protected Map<String, String> profiles = new HashMap<>();
    protected String loggedInUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profiles = connectMongo();
        Map<String, String> p = displayProfiles();
    }

    protected Map<String, String> connectMongo() {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    //reminder: getAll is an endpoint that returns JSON Array of programs
                    URL url = new URL("http://10.0.2.2:3010/getProfiles");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();
                    JSONArray profileArr = new JSONArray(response);
                    for (int i = 0; i < profileArr.length(); i++) {
                        JSONObject readProfile = profileArr.getJSONObject(i);
                        String username = readProfile.getString("username");
                        if (username != null) {
                            String password = readProfile.getString("password");
                            if (password != null) {
                                profiles.put(username, password);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //e.toString();
                }
            });
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profiles;
    }

    /* this function is solely to test if profiles are being taken from node: */
    public Map<String, String> displayProfiles() {
        Map <String, String> editedprofiles = new HashMap<>();
        //String s = "";
        for (String userName : profiles.keySet()) {
            //s += userName;
            editedprofiles.put(userName, profiles.get(userName));
        }
        TextView t = findViewById(R.id.profiles);
        //t.setText(s);
        return editedprofiles;
    }

/*
    protected Profile createProfile(JSONObject jsonProfile) {
        Profile newProfile = new Profile();
        try {
            String name = jsonProfile.getString("name");
            if (name == null) {
                newProfile.setName("");
            }
            else {
                newProfile.setName(name);
            }

            String username = jsonProfile.getString("username");
            if (username == null) {
                newProfile.setUsername("");
            }
            else {
                newProfile.setUsername(username);
            }

            String password = jsonProfile.getString("password");
            if (password == null) {
                newProfile.setPassword("");
            }
            else {
                newProfile.setPassword(password);
            }

            String pronouns = jsonProfile.getString("pronouns");
            if (pronouns == null) {
                newProfile.setPronouns("");
            }
            else {
                newProfile.setPronouns(pronouns);
            }

            String classYear = jsonProfile.getString("classyear");
            if (pronouns == null || pronouns.equals("")) {
                newProfile.setClassYear(0);
            }
            else {
                newProfile.setClassYear(Integer.parseInt(classYear));
            }

            String programObj = jsonProfile.getString("programs");
            JSONObject programArr = new JSONObject(programObj);
            Set<Program> programSet = new HashSet<>();
            //how to parse through JSON OBJECT?
            //for ()
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return newProfile;
    }*/

    public void onRegisterButtonClick(View v) {
        String users = "";
        for (String user : profiles.keySet()) {
            users = users + "/" + user;
        }
        TextView t = findViewById(R.id.profiles);
        //t.setText(users);
        Intent i = new Intent(this, RegisterActivity.class);
        i.putExtra("usernames", users);
        startActivity(i);
    }
    public void onEditPasswordClick(View v) {
        Map<String,String> users = new HashMap<>();
        Map<String, Profile> profiles = connectMongo();
        for (Map.Entry<String, Profile> entry: profiles.entrySet()) {
            String name = entry.getValue().getName();
            String password = entry.getValue().getPassword();
            users.put(name, password);
        }

        Intent i = new Intent(this, EditPassword.class);
        i.putExtra("users", (Serializable) users);
        startActivity(i);
    }
    public void onLoginButtonClick(View v) {
        EditText usernameField = findViewById(R.id.userUsername);
        String userNameStr = usernameField.getText().toString();
        EditText passwordField = findViewById(R.id.userPassword);
        String passwordStr = passwordField.getText().toString();

        boolean isRight = checkProfiles(userNameStr, passwordStr);
        if (isRight) {
            usernameField.setText("");
            passwordField.setText("");
            Intent i = new Intent(this, HomePage.class);
            i.putExtra("USER_LOGGED_IN", loggedInUser);
            Log.v("IS THIS EVEN WORKING", loggedInUser);
            startActivity(i);
        }
        else {
            usernameField.setText("");
            passwordField.setText("");
        }

    }

    protected boolean checkProfiles(String username, String password) {
        //Map<String, Profile> editedprofiles = connectMongo();
        TextView t = findViewById(R.id.profiles);

        // ensures that neither user nor pass could be blank
        if (username == null || username.equals(" ") || username.equals("")) {
            //t.setText("Please enter a username");
            Toast.makeText(this, "LOGIN FAIL: Enter a username!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (password == null || password.equals(" ") || password.equals("")) {
            //t.setText("Please enter a password");
            Toast.makeText(this, "LOGIN FAIL: Enter a password!", Toast.LENGTH_LONG).show();

            return false;
        }

        // verifying the username & password below:
        try {
            if (profiles.containsKey(username)) {
                if (profiles.get(username).equals(password)) {
                    //t.setText("successfully logged in");
                    Toast.makeText(this, "LOGIN SUCCESSFUL", Toast.LENGTH_LONG).show();
                    loggedInUser = username;
                    return true;
                }
                else {
                    //t.setText("wrong password");
                    Toast.makeText(this, "LOGIN FAIL: Wrong password!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                //t.setText("username not found");
                Toast.makeText(this, "LOGIN FAIL! User not found", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            //TextView t = findViewById(R.id.profiles);
            //t.setText("something went wrong");
            Toast.makeText(this, "LOGIN FAIL! Error occurred", Toast.LENGTH_LONG).show();
        }

        // when user & pass are wrong return false
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode){
            case HOME_ACTIVITY_ID:
                String loginStatus = intent.getStringExtra("LOGIN_STATUS");
                Toast.makeText(
                                this,
                                loginStatus,
                                Toast.LENGTH_LONG)
                        .show();
                break;
        }
    }
}
