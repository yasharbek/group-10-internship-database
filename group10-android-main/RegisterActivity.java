package com.example.fliopp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private Map<String, String> usernames;
    private String usernameStr;
    private List<String> usernameList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameStr = getIntent().getStringExtra("usernames");
        setUsernameList();
    }

    /*takes the message from the */
    public void setUsernameList() {
        String [] tempUserArr = usernameStr.split("/");
        for (int i = 0; i < tempUserArr.length; i++) {
            usernameList.add(tempUserArr[i]);
        }
    }

    public void onRegisterButtonClick(View v) {
        EditText usernameField = findViewById(R.id.registerUser);
        String usernameStr = usernameField.getText().toString();
        EditText passwordField = findViewById(R.id.registerPass);
        String passwordStr = passwordField.getText().toString();

        if (checkUsername(usernameStr) && checkPassword(passwordStr)) {
            usernameField.setText("");
            passwordField.setText("");
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, RegisterActivity2.class);
            String [] loginArr = {usernameStr, passwordStr};
            i.putExtra("loginInfo", loginArr);
            startActivity(i);
        }
        else {
            usernameField.setText("");
            passwordField.setText("");
        }
    }

    protected boolean checkUsername(String username) {
        /* checks if the username list from mongo is ok! */
        if (usernameList == null || usernameList.isEmpty()) {
            Toast.makeText(this, "ERROR: Issue with database!", Toast.LENGTH_LONG).show();
            return false;
        }

        /* check if the password is not null or empty */
        if (username == null || username.equals("") || username.equals(" ")) {
            Toast.makeText(this, "REGISTER FAIL: username invalid!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (usernameList.contains(username)) {
            Toast.makeText(this, "REGISTER FAIL: username exists!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (username.length() < 5) {
            Toast.makeText(this, "REGISTER FAIL: username too short!", Toast.LENGTH_LONG).show();
            return false;
        }
        
        char [] usernameArr = username.toCharArray();
        int count = 0;
        for (int i = 0; i < usernameArr.length; i++) {
            if (usernameArr[i]==' ') {
                Toast.makeText(this, "REGISTER FAIL: no empty spaces!", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        //add only empty spaces?
        return true;
    }

    protected boolean checkPassword(String password) {
        /* check if the password is not null or empty */
        if (password == null || password.equals("")) {
            Toast.makeText(this, "Password invalid!", Toast.LENGTH_LONG).show();
            return false;
        }

        /* check if the password is more than or = to 5 characters */
        if (password.length() < 5) {
            Toast.makeText(this, "Password invalid!", Toast.LENGTH_LONG).show();
            return false;
        }
        
        char [] passwordArr = password.toCharArray();
        int count = 0;
        for (int i = 0; i < passwordArr.length; i++) {
            if (passwordArr[i]==' ') {
                Toast.makeText(this, "REGISTER FAIL: no empty spaces!", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    /* returning back to the main page */
    public void onReturnButtonClick(View v) {
        Intent i = new Intent();
        i.putExtra("RETURN_STATUS", "Successfully returned");
        setResult(RESULT_OK, i);
        finish();
    }
}



