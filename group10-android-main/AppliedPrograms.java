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


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.*;
import java.util.Arrays;
//import org.json.JSONArray;
//import org.json.JSONObject;
import java.util.*;
public class AppliedPrograms extends AppCompatActivity{

    //LOGGED_IN_USER
    protected String loggedInUser = "";

    String[] userSavedProgramsArr;
    protected Map<String, Profile> profiles = new HashMap<>();

    //protected String savedPrograms = "";
    //ListView savedPrograms;
    //String programs[];

    //private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliedprograms);

        loggedInUser = getIntent().getStringExtra("USER_LOGGED_IN");
        Log.v("OK CHECKING THIS SENT HERE TOO", loggedInUser);
        //Toast.makeText(this, loggedInUser, Toast.LENGTH_LONG).show();

        profiles = connectMongo();


//LIST VIEW AESTHETICS
//        setContentView(R.layout.activity_appliedprograms);
//        savedPrograms = findViewById(R.id.savedProgramsList);
//        listAdapter = new ArrayAdapter<String>(this, R.layout.listview_row, R.id.savedProgramsList,
//                getAppliedPrograms());
//        savedPrograms.setAdapter(listAdapter);
        TextView t = findViewById(R.id.savedProgramsList);
        //String s = "";
        //Program[] array = getAppliedPrograms();//this is wrong!!!!!!!!!!!!!!!!
//        for (int i = 0; i < array.length; i ++) {
//            s += (array[i] + " ");
//        }
        t.setText(getAppliedPrograms());
        //getAppliedPrograms()
    }

    protected Map<String, Profile> connectMongo() {
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
                        Profile newProfile = createProfile(readProfile);
                        profiles.put(newProfile.getUsername(), newProfile);
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
            if(username == null){
                newProfile.setUsername("");
            }
            else{
                newProfile.setUsername(username);
            }

            String password = jsonProfile.getString("password");
            if(password == null){
                newProfile.setPassword("");
            }
            else{
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

            //String [] programArray = jsonProfile.getStringArray("programs") THIS IS THEORHETICALLY HOW IT WOULD WORK

//            JSONObject obj = new JSONObject(sb);
//            JSONArray arr = obj.getJSONArray("value");


            String programStr = jsonProfile.getString("programs");
            if(programStr == null){
                newProfile.setProgramsAsStr("");
            }
            else{
                newProfile.setProgramsAsStr(programStr);
            }
            //print this programObj
            Log.v("HELLO THIS IS WHAT TO LOOK FOR", programStr);



            //Printing out the string with the bracket
            //We need to get rid of the brackets at the end
            //make an instance variable and set it to this String and then set that to the TextView for this
//
//            JSONObject programArr = new JSONObject(programObj);
//            Set<Program> programSet = new HashSet<>();
            //
//            for(){
//
//            }




            //making a set to set to the profile's saved programs:
            //Set<Program> progs = new HashSet<>();

            //this is to read in the saved programs from mongo bongo


//            JSONArray savedPrograms = jsonProfile.getJSONArray("programs");
//            Set<String> setOfProgs = new HashSet<>();
//            for(int k = 0; k < savedPrograms.length(); k++){
//                setOfProgs.add(savedPrograms.getString(k));
//            }
//            //savedPrograms = jsonProfile.getString("programs");
////
////            //this is because the progs from mongo is its own array? maybe string arr?
////            //JSONArray programArr = new JSONArray(savedPrograms);
////            //String [] programArr = savedPrograms;
////
////            //iterating through the each program's info
//////            for (int i = 0; i < programArr.length(); i++) {
//////                JSONObject readProgram = programArr.getJSONObject(i);
//////                Program newProg = new Program();
//////                newProg.setName(readProgram.getString("name"));
//////                progs.add(newProg);
//////            }
//            newProfile.setSavedPrograms(setOfProgs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return newProfile;
    }

    protected String getAppliedPrograms(){
        //get the profile from the map
        Profile targetUser = profiles.get(loggedInUser);
        String programsListed = targetUser.getProgramsAsStr();
        return programsListed;
    }

}
