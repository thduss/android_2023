package com.example.insquare;

public class UserModel {
    private String firstName;
    private String lastName;
    private String p_name;
    private String p_location;
    private String p_company;

    private double loc1; //위도
    private  double loc2; //경도

    // 기본 생성자 추가
    public UserModel() {}

    public UserModel(String firstName, String lastName, String p_company, String p_location, double loc1, double loc2){
        this.firstName = firstName;
        this.lastName = lastName;
        this.p_company = p_company;
        this.p_location = p_location;
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

    public String getP_name(){
        return p_name;
    }
    public void setP_name(String userName){
        this.p_name = userName;
    }

    public String getP_company() {
        return p_company;
    }

    public void setP_company(String userCompanyName) {
        this.p_company = userCompanyName;
    }

    public String getP_location(){
        return p_location;
    }

    public double getLoc1 () { return loc1; }
    public void setLoc1(double loc1) { this.loc1 = loc1; }
    public double getLoc2 () { return loc2; }
    public void setLoc2(double loc2) { this.loc2 = loc2; }
}
