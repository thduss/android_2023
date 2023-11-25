package com.example.insquare;

public class UserModel {
    private String firstName;
    private String lastName;
    private String locationName;
    private String userCompanyName;

    private double loc1; //위도
    private  double loc2; //경도
    public UserModel(String firstName, String lastName, String userCompanyName, String locationName, double loc1, double loc2){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userCompanyName = userCompanyName;
        this.locationName = locationName;
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserCompany() {
        return userCompanyName;
    }

    public void setUserCompany(String userCompanyName) {
        this.userCompanyName = userCompanyName;
    }

    public String getLocationName(){
        return locationName;
    }

    public double getLoc1 () { return loc1; }
    public void setLoc1(double loc1) { this.loc1 = loc1; }
    public double getLoc2 () { return loc2; }
    public void setLoc2(double loc2) { this.loc2 = loc2; }
}
