package com.example.fliopp;

import java.util.*;

public class Filters {
    private String categoryFilter;
    private String statusFilter; 
    private String locationFilter; 
    private Set<Integer> yearFilter;
    private Double gpaFilter;

    public Filters () {
        categoryFilter = "";
        statusFilter = ""; 
        locationFilter = ""; 
        yearFilter = new HashSet<Integer>();
        gpaFilter = 0.0;
    }

    /* All the get methods are below: */
    public String getCategoryFilter() { return categoryFilter; }

    public String getStatusFilter() { return statusFilter; }
    
    public String getLocationFilter() { return locationFilter; }
    
    public Set<Integer> getYearFilter() { return yearFilter; }
    
    public Double getGPAFilter() { return gpaFilter; }

    /* All the set methods are below: */

    public void setCategoryFilter(String newCategoryFilter) { categoryFilter = newCategoryFilter; }

    public void  setStatusFilter(String newStatusFilter) { statusFilter = newStatusFilter; }
    
    public void setLocationFilter(String newLocationFilter) { locationFilter = newLocationFilter; }
    
    public void setYearFilter(Set<Integer> newYearFilter) { yearFilter = newYearFilter; }

    public void setGPAFilter(Double newGPAFilter) { gpaFilter = newGPAFilter; }

}
