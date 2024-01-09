package com.example.fliopp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/*
*
* */
public class RegisterActivity2 extends AppCompatActivity {
    private String username = "";
    private String password = "";
    private String name = "";
    private String pronouns = "";
    private int classYear = 0;
    private LinkedList<String> programNames = new LinkedList<String>();
    private LinkedList<String> savePrograms = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        String [] loginArr = getIntent().getStringArrayExtra("loginInfo");
        username = loginArr[0];
        password = loginArr[1];

        //http://localhost:3010/register?name=Adam&pronouns=he/him&username=password&password=orgoslayer&classyear=2025&programs=HELLO&programs=ANOTHERONE
        //add programs here:
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    //reminder: getAll is an endpoint that returns JSON Array of programs
                    URL url = new URL("http://10.0.2.2:3010/getAllPrograms");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();
                    JSONArray programsArr = new JSONArray(response);
                    for (int i = 0; i < programsArr.length(); i++) {
                        JSONObject readProgram = programsArr.getJSONObject(i);
                        programNames.add(readProgram.getString("name"));

//                        Profile newProfile = createProfile(readProfile);
//                        profiles.put(newProfile.getUsername(), newProfile);
                    }
                    Log.v("program:", programNames.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    //e.toString();
                }
            });
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayout linearLayout = findViewById(R.id.checkPrograms);


        for (String programName : programNames) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(programName);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (linearLayout != null) {
                linearLayout.addView(checkBox);
            }
        }
    }

    /* this is to check the name: */
    public void createProfile() {
        //connecting to mongo here

        //http://localhost:3010/registerProfile?name="Hello"&pronouns="she/her"&classYear="2025"
        String statusMessage = "";
        TextView t = findViewById(R.id.text);
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    String urlStr = "http://10.0.2.2:3010" + "/register?";
                    urlStr = urlStr + "name=" + name + "&pronouns=" + pronouns + "&username=" + username + "&password=" + password + "&classyear=" + classYear;
                    for (String program : savePrograms) {
                        program = program.replace(" ", "%20");
                        urlStr = urlStr + "&programs=" + program;
                    }

                    URL url = new URL(urlStr);

                    Log.v("MY CONNECTION MESSAGE", "CONNECTING URL: " + urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    Log.v("MY CONNECTION MESSAGE", "ENDING CONNECTION ISH HERE------------------------------------------");
                    //Toast.makeText(this, "connection opened here!", Toast.LENGTH_LONG).show();
//                  int responseCode = conn.getResponseCode();
//                  Toast.makeText(this, "getting responseCode here!", Toast.LENGTH_LONG).show();
//                  if (responseCode != 200) {
//                      Toast.makeText(this, "ERROR: response code failed!", Toast.LENGTH_LONG).show();
//                  }
//                  else {
                    /*error gets thrown at the response code & at the scanner line */
                    //Log.v("MY ERROR MESSAGE", "SOMETHING IS HAPPENING HERE ------------------------------------------");
                    Scanner in = new Scanner(url.openStream());
                    //Log.v("THIS IS WHAT COMES OUT OF THE CONNECTION", in.toString());
                    while (in.hasNext()) {
                        String response = in.nextLine();
                        JSONObject status = new JSONObject(response);
                        //t.setText(status.getString("status"));
                        //Log.v("END OF ERROR MESSAGE", "END OF MESSAGE ------------------------------------------");
                        Toast.makeText(this, status.getString("status"), Toast.LENGTH_LONG).show();
                    }
                    //}
                }
                catch (Exception e) {
                    //Log.v("MY ERROR MESSAGE", "SOMETHING IS HAPPENING HERE ------------------------------------------");
                    Toast.makeText(this, "ERROR OCCURRED", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            });
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            Toast.makeText(this, "ERROR OCCURRED", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public boolean checkName() {
        EditText nameField = findViewById(R.id.usersName);
        String nameStr = nameField.getText().toString();
        if (name == null || nameStr == "") {
            Toast.makeText(this, "ERROR: please enter a name!", Toast.LENGTH_LONG).show();
            return false;
        }

        String nameLower = nameStr.toLowerCase();
        int emptyCount = 0;
        char [] nameArr = nameLower.toCharArray();
        for (int i = 0; i < nameArr.length; i++) {
            if (!(nameArr[i] >= 'a') || !(nameArr[i] <= 'z')) {
                Toast.makeText(this, "ERROR: name invalid!", Toast.LENGTH_LONG).show();
                return false;
            }
            else if (nameArr[i] == ' ') {
                emptyCount++;
            }
        }
        if (emptyCount == nameStr.length()) {
            Toast.makeText(this, "ERROR: name empty!", Toast.LENGTH_LONG).show();
            return false;
        }

        name = nameStr;
        return true;
    }

    public boolean checkPronouns() {
        EditText pronounsField = findViewById(R.id.usersPronouns);
        String pronounsStr = pronounsField.getText().toString();
        if (pronounsStr == null || pronounsStr == "") {
            Toast.makeText(this, "ERROR: pronouns invalid!", Toast.LENGTH_LONG).show();
            return false;
        }

        int emptyCount = 0;
        char [] pronounsChar = pronounsStr.toCharArray();
        for (int i = 0; i < pronounsChar.length; i++) {
            if (pronounsChar[i] == ' ') {
                emptyCount++;
            }
        }
        if (emptyCount == pronounsStr.length()) {
            Toast.makeText(this, "ERROR: pronouns empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        pronouns = pronounsStr;
        return true;
    }
    public boolean checkClassYear() {
        EditText classYearField = findViewById(R.id.usersClassYear);
        String classYearStr = classYearField.getText().toString();
        if (classYearStr == null || classYearStr == "") {
            Toast.makeText(this, "ERROR: class year invalid!", Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            int classYearNum = Integer.parseInt(classYearStr);
            if (classYearNum <= 0) {
                Toast.makeText(this, "ERROR: class year should be positive!", Toast.LENGTH_LONG).show();
                return false;
            }
            classYear = classYearNum;
            return true;
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "ERROR: class year non numeric!", Toast.LENGTH_LONG).show();
        }
        return false;
    }
 
    public boolean saveCheckboxes(LinearLayout checkPrograms) {
        int count = checkPrograms.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = checkPrograms.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox checkBoxChild = (CheckBox) child;
                String programName = checkBoxChild.getText().toString();;
                //.replaceAll("\\s", "")
                boolean check = checkBoxChild.isChecked();

                if (check) {
                    savePrograms.add(programName);
                }
            }

        }

        if (savePrograms.isEmpty()) {
            Toast.makeText(this, "No checkboxes were selected", Toast.LENGTH_LONG).show();
            Log.v("saved programs: ", savePrograms.toString());
            return false;
        } else {
            return true;
        }
        //Log.v("saved programs: ", savePrograms.toString());

    }
    public void onSubmitButtonClick(View v) {
        if (checkName()) {
            if (checkPronouns()) {
                if (checkClassYear()) {
                    if (saveCheckboxes(findViewById(R.id.checkPrograms))) {
                        //add from android to MONGO:
                        Toast.makeText(this, "Profile successfully made!", Toast.LENGTH_LONG).show();
                        createProfile();
                        return;
                    }
                }
            }
        }
        Toast.makeText(this, "ERROR: profile was wrong ", Toast.LENGTH_LONG).show();
    }

    public void onReturnButtonClick(View v) {
        /* QUESTION: how to get from this intent to the main page */
        //Intent i = new Intent();
        //i.putExtra("RETURN_STATUS", "Successfully returned");
        //setResult(RESULT_OK, i);
        finish();
    }
}

//onSubmitButtonClick