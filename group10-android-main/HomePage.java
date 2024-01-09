package com.example.fliopp;

import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import org.json.*;
//import org.json.JSONArray;
//import org.json.JSONObject;
import java.util.*;


public class HomePage extends AppCompatActivity{

    protected String loggedInUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        loggedInUser = getIntent().getStringExtra("USER_LOGGED_IN");
        Log.v("OK CHECKING THIS SENT HERE TOO", loggedInUser);
    }
    public void onSuggestEditButtonClick(View v){
        Intent i = new Intent(this, SuggestEdit.class);
        startActivity(i);
    }

    public void onAppliedProgramsButtonClick(View v){
        //Toast.makeText(this, loggedInUser, Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, AppliedPrograms.class);
        i.putExtra("USER_LOGGED_IN", loggedInUser);
        startActivity(i);
    }
    public void onLogOutButtonClick(View v){
        Intent i = new Intent();
        i.putExtra("LOGIN_STATUS", "Successfully logged out");
        setResult(RESULT_OK, i);
        //end activity
        finish();
    }

    public void onFilterActivityButtonClick(View v){
        Intent i = new Intent(this, FilterActivity.class);
        i.putExtra("USER_LOGGED_IN", loggedInUser);
        Log.v("IS THIS EVEN WORKING", loggedInUser);
        startActivity(i);
    }
    public static final int FILTER_ACTIVITY_ID = 3;

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode){
            case FILTER_ACTIVITY_ID:
                String returnStatus = intent.getStringExtra("RETURN_STATUS");
                //display pop-up
                Toast.makeText(
                                this,
                                returnStatus,
                                Toast.LENGTH_LONG)
                        .show();
                break;
        }
    }

}
