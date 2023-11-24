package com.example.insquare;

public class List_User {
    private String logo;
    private String username;
    private String department;
    private String position;

    private String company;

    public List_User(){}

    public String getLogo() {
        return logo;
    }

    public void setProfile(String logo) {
        this.logo = logo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.position = company;
    }
}
