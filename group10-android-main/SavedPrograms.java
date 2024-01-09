package com.example.fliopp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SavedPrograms extends AppCompatActivity {
    private String host;
    private int port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedprograms);


    }

    public void onReturnButtonClick(View v){
        Intent i = new Intent();
        i.putExtra("LOGIN_STATUS", "Returned to Homepage");
        setResult(RESULT_OK, i);
        //end activity
        finish();
    }

}
