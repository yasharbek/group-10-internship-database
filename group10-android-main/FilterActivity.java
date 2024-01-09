package com.example.fliopp;

import androidx.annotation.IntegerRes;
import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import org.json.*;
//import org.json.JSONArray;
//import org.json.JSONObject;
import java.util.*;

public class FilterActivity extends AppCompatActivity {
    //this map has the original set of programs
    protected Map<String, Program> originalPrograms = new TreeMap<String, Program>();
    //this map is the filtered set of programs
    protected Map<String, Program> filteredPrograms = new TreeMap<String, Program>();
    //this variable stores all filters
    protected Filters savedFilters = new Filters();
    protected String loggedInUser = "";
    private Program spotlightProgram = new Program();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        loggedInUser = getIntent().getStringExtra("USER_LOGGED_IN");
        Log.v("OK CHECKING THIS SENT HERE TOO", loggedInUser);

        initializingSpinners();
        initializingPrograms();

        connectMongo();
        displaySpotlighted();
        displayNames();
    }

    protected void initializingPrograms() {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    //reminder: getAll is an endpoint that returns JSON Array of programs
                    URL url = new URL("http://10.0.2.2:3010/getPrograms");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();
                    JSONArray programArr = new JSONArray(response);
                    for (int i = 0; i < programArr.length(); i++) {
                        JSONObject readProgram = programArr.getJSONObject(i);
                        Program newProgram = createProgram(readProgram);

                        //displayedProgramsSet.add(newProgram.getName());
                        //originalProgramsSet.add(newProgram.getName());
                        originalPrograms.put(newProgram.getName(), newProgram);
                        filteredPrograms.put(newProgram.getName(), newProgram);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //e.toString();
                }
            });
            executor.awaitTermination(2, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void initializingSpinners() {
        /* start of initializing spinners */
        Spinner spinner1 = findViewById(R.id.userCategoryFilter);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this,
                R.array.category_array,
                android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        spinner1.setAdapter(adapter1);

        Spinner spinner2 = findViewById(R.id.userDocumentedFilter);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this,
                R.array.documentation_array,
                android.R.layout.simple_spinner_item
        );
        adapter3.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        spinner2.setAdapter(adapter3);

        Spinner spinner3 = findViewById(R.id.userLocationFilter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.location_array,
                android.R.layout.simple_spinner_item
        );
        adapter2.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        spinner3.setAdapter(adapter2);

        Spinner spinner4 = findViewById(R.id.userYearFilter);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
                this,
                R.array.year_array,
                android.R.layout.simple_spinner_item
        );
        adapter4.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        spinner4.setAdapter(adapter4);
        /* end of initializing spinners */
    }
    protected void connectMongo() {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    //reminder: getAll is an endpoint that returns JSON Array of programs
                    URL url = new URL("http://10.0.2.2:3010/getSpotlightedProgram");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();
                    JSONArray programArr = new JSONArray(response);
                    for (int i = 0; i < programArr.length(); i++) {
                        JSONObject readProfile = programArr.getJSONObject(i);
                        Program newProgram = createProgram(readProfile);
                        spotlightProgram = newProgram;
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
    }

    protected void displaySpotlighted() {
        TextView t = findViewById(R.id.spotlight);
        t.setText(spotlightProgram.getName());
    }

    protected Program createProgram(JSONObject readProgram) {
        Program newProgram = new Program();
        try {
            String name = readProgram.getString("name");
            if (name == null) {
                newProgram.setName("");
            }
            else {
                newProgram.setName(name);
            }

            String link = readProgram.getString("link");
            if (link == null) {
                newProgram.setLink("");
            }
            else {
                newProgram.setLink(link);
            }

            String category = readProgram.getString("category");
            if (category == null) {
                newProgram.setCategory("");
            }
            else {
                newProgram.setCategory(category);
            }

            String location = readProgram.getString("location");
            if(location == null) {
                newProgram.setLocation("");
            }
            else {
                newProgram.setLocation(location);
            }

            String status = readProgram.getString("documentedStatus");
            if(status == null) {
                newProgram.setStatus("");
            }
            else {
                newProgram.setStatus(status);
            }

            String programStart = readProgram.getString("programDates");
            if (programStart == null) {
                newProgram.setProgramStart("");
            }
            else {
                newProgram.setProgramStart(programStart);
            }

            String applicableYear = readProgram.getString("applicableYear");
            if (applicableYear == null) {
                newProgram.setApplicableYear("");
            }
            else {
                newProgram.setApplicableYear(applicableYear);
            }

            String gpaStr = readProgram.getString("gpaReq");
            if (gpaStr == null) {
                newProgram.setGPA(0.0);
            }
            else {
                Double gpa = Double.parseDouble(gpaStr);
                newProgram.setGPA(gpa);
            }

            String alumnEmail = readProgram.getString("alumniEmail");
            if (alumnEmail == null) {
                newProgram.setAlumnEmail("");
            }
            else {
                newProgram.setAlumnEmail(alumnEmail);
            }

        }
        catch (Exception e) {

        }
        return newProgram;
    }

    /* THE FOLLOWING IS USED TO TEST THE CONNECTION BETWEEN NODE & ANDROID*/
    public void displayNames() {
        //TextView programDisplay = findViewById(R.id.programDisplay);
        String [] programsToDisplay = new String [originalPrograms.size()];
        int arrCount = 0;

        for (String programKey : originalPrograms.keySet()) {
            programsToDisplay[arrCount] = originalPrograms.get(programKey).getName();
            arrCount++;
        }

        ArrayAdapter<String> arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, programsToDisplay);
        ListView newList = (ListView) findViewById(R.id.list);
        newList.setAdapter(arr);
    }

    public void onResetFiltersButton(View v) {
        Toast.makeText(this, "Programs are reset!", Toast.LENGTH_LONG).show();
        //filteredPrograms.clear();
        filteredPrograms = originalPrograms;
        //changeDisplay();
        displayNames();
        //originalPrograms = filteredPrograms;
    }
    
    protected void changeDisplay()  {
        //TextView programDisplay = findViewById(R.id.programDisplay);
        String [] programsToDisplay;
        if (filteredPrograms.size() == 0) {
            programsToDisplay = new String[1];
            programsToDisplay[0] = "No Programs! Try a new filter";
        }
        else {
            programsToDisplay = new String[filteredPrograms.size()];
            //totalProgramStr = "";
            int arrCount = 0;
            for (String p : filteredPrograms.keySet()) {
                programsToDisplay[arrCount] = filteredPrograms.get(p).getName();
                arrCount++;
            }
        }
        ArrayAdapter<String> arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, programsToDisplay);
        ListView newList = (ListView) findViewById(R.id.list);
        newList.setAdapter(arr);
    }
    
    public void onCategoryFilterButton(View v) {
        Spinner spinner = findViewById(R.id.userCategoryFilter);
        if (spinner.getSelectedItem().toString() == null || spinner.getSelectedItem().toString() == "") {
            String toastText = "Please select a category!";
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
            return;
        }
        String userCategoryFilter = spinner.getSelectedItem().toString();
        savedFilters.setCategoryFilter(userCategoryFilter);
        String toastText = "Category filter: " + userCategoryFilter;
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();

        Map<String, Program> tempMap = new HashMap<>();
        tempMap.putAll(filteredPrograms);
        //tempMap = filteredPrograms;
        for (String programKey : filteredPrograms.keySet()) {
            String tempCategory = filteredPrograms.get(programKey).getCategory();
            if (tempMap.containsKey(programKey) && (!tempCategory.equals(userCategoryFilter))) {
                tempMap.remove(programKey);
            }
            else if (tempCategory.equals(userCategoryFilter) && (!tempMap.containsKey(programKey))) {
                tempMap.put(programKey, originalPrograms.get(programKey));
            }
        }
        filteredPrograms = tempMap;
        changeDisplay();
    }

    public void onStatusFilterButton(View v) {
        Spinner spinner = findViewById(R.id.userDocumentedFilter);
        if (spinner.getSelectedItem().toString() == null || spinner.getSelectedItem().toString() == "") {
            String toastText = "Please select a status!";
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
            return;
        }
        savedFilters.setStatusFilter(spinner.getSelectedItem().toString());
        String statusFilter = savedFilters.getStatusFilter();

        Map<String, Program> tempMap = new HashMap<>();
        tempMap.putAll(filteredPrograms);
        //tempMap = filteredPrograms;
        for (String programKey : filteredPrograms.keySet()) {
            String tempStatus = filteredPrograms.get(programKey).getStatus();
            if (tempMap.containsKey(programKey) && (!tempStatus.equals(statusFilter))) {
                tempMap.remove(programKey);
            }
            else if (tempStatus.equals(statusFilter) && (!tempMap.containsKey(programKey))) {
                tempMap.put(programKey, originalPrograms.get(programKey));
            }
        }
        filteredPrograms = tempMap;
        changeDisplay();
    }

    public void onLocationFilterButton(View v) {
        Spinner spinner = findViewById(R.id.userLocationFilter);
        if (spinner.getSelectedItem().toString() == null || spinner.getSelectedItem().toString() == "") {
            String toastText = "Please select a location!";
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
            return;
        }
        savedFilters.setLocationFilter(spinner.getSelectedItem().toString());
        String locationFilter = savedFilters.getLocationFilter();

        Map<String, Program> tempMap = new HashMap<>();
        tempMap.putAll(filteredPrograms);
        //tempMap = filteredPrograms;
        for (String programKey : filteredPrograms.keySet()) {
            String tempLocation = filteredPrograms.get(programKey).getLocation();
            if (tempMap.containsKey(programKey) && (!tempLocation.equals(locationFilter))) {
                tempMap.remove(programKey);
            }
            else if (tempLocation.equals(locationFilter) && (!tempMap.containsKey(programKey))) {
                tempMap.put(programKey, originalPrograms.get(programKey));
            }
        }
        filteredPrograms = tempMap;
        changeDisplay();
    }

    public void onYearFilterButton(View v) {
        Spinner spinner = findViewById(R.id.userYearFilter);
        if (spinner.getSelectedItem().toString() == null || spinner.getSelectedItem().toString() == "") {
            String toastText = "Please select a year!";
            Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
            return;
        }
        String userYear = spinner.getSelectedItem().toString();
        savedFilters.setCategoryFilter(userYear);
        String toastText = "Year filter: " + userYear;
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();

        Map<String, Program> tempMap = new HashMap<>();
        tempMap.putAll(filteredPrograms);
        //tempMap = filteredPrograms;
        for (String programKey : filteredPrograms.keySet()) {
            String tempCategory = filteredPrograms.get(programKey).getApplicableYear();
            if (tempMap.containsKey(programKey) && (!tempCategory.equals(userYear))) {
                tempMap.remove(programKey);
            }
            else if (tempCategory.equals(userYear) && (!tempMap.containsKey(programKey))) {
                tempMap.put(programKey, originalPrograms.get(programKey));
            }
        }
        filteredPrograms = tempMap;
        changeDisplay();
    }

    protected void filterByGPA() {
        Double filteredGPA = savedFilters.getGPAFilter();
        Map<String, Program> tempMap = new HashMap<>();
        tempMap.putAll(filteredPrograms);
        for (String programKey : filteredPrograms.keySet()) {
            Double tempGPA = filteredPrograms.get(programKey).getGPA();
            if (tempMap.containsKey(programKey) && (tempGPA < filteredGPA)) {
                tempMap.remove(programKey);
            }
            else if (tempGPA >= filteredGPA && (!tempMap.containsKey(programKey))) {
                tempMap.put(programKey, originalPrograms.get(programKey));
            }
        }
        filteredPrograms = tempMap;
        changeDisplay();
    }

    public void onGPAFilterButton(View v) {
        EditText userGPA = findViewById(R.id.userGPAFilter);
        String userGPAStr = userGPA.getText().toString();
        if (userGPAStr.equals("")) {
            Toast.makeText(this, "GPA filter invalid!", Toast.LENGTH_LONG).show();
            userGPA.setText("");
            return;
        }
        else {
            try {
                Double userGPADouble = Double.parseDouble(userGPAStr);
                if ((userGPADouble < 0) || (userGPADouble > 4)) {
                    Toast.makeText(this, "GPA filter invalid!", Toast.LENGTH_LONG).show();
                    userGPA.setText("");
                    return;
                }
                savedFilters.setGPAFilter(userGPADouble);
                filterByGPA();
            }
            catch (NumberFormatException e) {
                Toast.makeText(this, "GPA filter invalid!", Toast.LENGTH_LONG).show();
                userGPA.setText("");
            }
        }
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
    public void onHomePageButtonClick(View v){
        Intent i = new Intent();
        i.putExtra("LOGIN_STATUS", "Returned to Homepage");
        setResult(RESULT_OK, i);
        //end activity
        finish();
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
