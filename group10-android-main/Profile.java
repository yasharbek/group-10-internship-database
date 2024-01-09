package com.example.fliopp;

import java.util.HashSet;
import java.util.Set;

public class Profile {
    private String name, pronouns, username, password, programsAsStr;
    private int classYear;
    private Set<Program> savedPrograms;

    public Profile () {
        name = "";
        username = "";
        password = "";
        pronouns = "";
        classYear = 0;
        savedPrograms = new HashSet<Program>(); //was previously Program
        programsAsStr = "";
    }

    public String getName() {return name;}

    public void setUsername(String newUsername) {username = newUsername;}

    public void setPassword(String newPassword) {password = newPassword;}

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public String getProgramsAsStr() {return programsAsStr;}

    public String getPronouns()  {return pronouns;}

    public int getClassYear() {return classYear;}

    public Set<Program> getSavedPrograms() {return savedPrograms;}
    //was previously a Program

    public void setName(String newName) {name = newName;}
    public void setPronouns(String newPronouns) {pronouns = newPronouns;}

    public void setClassYear(int newClassYear) {classYear = newClassYear;}

    public void setSavedPrograms(Set<Program> newPrograms) {savedPrograms = newPrograms;}

    public void setProgramsAsStr(String newProgramsAsStr) {programsAsStr = newProgramsAsStr;}
}
