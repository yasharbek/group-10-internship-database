package com.example.fliopp;

import java.util.HashSet;
import java.util.Set;

public class Program {
    private String name, link, category, location, status, programStart, deadline, alumnEmail, applicableYear;
    private Double GPA;

    public Program () {
        name = "";
        link = "";
        category = "";
        location = "";
        status = "";
        applicableYear = "";
        programStart = "";
        deadline = "";
        GPA = 4.0;
        alumnEmail = "";
    }

    /* All the get methods are below: */
    public String getName() { return name; }

    public String getLink() { return link; }
 
    public String getCategory() { return category; }

    public String getLocation() { return location; }

    public String getStatus() { return status; }

    public String getApplicableYear() { return applicableYear; }

    public String getDeadline() { return deadline; }

    public String getProgramStart() { return programStart; }

    public Double getGPA() { return GPA; }

    public String getAlumnEmail() { return alumnEmail; }


    /* All the set methods are below: */
    public void setName(String newName) { name = newName; }

    public void setLink(String newLink) { link = newLink; }

    public void setCategory(String newCategory) { category = newCategory; }

    public void setLocation(String newLocation) { location = newLocation; }

    public void setStatus(String newStatus) { status = newStatus; }

    public void setApplicableYear(String newApplicableYear) { applicableYear = newApplicableYear; }

    public void setProgramStart(String newProgramStart) { programStart = newProgramStart; }

    public void setDeadline(String newDeadline) { deadline = newDeadline; }

    public void setGPA(Double newGPA) { GPA = newGPA; }

    public void setAlumnEmail(String newAlumnEmail) { alumnEmail = newAlumnEmail; }

}

