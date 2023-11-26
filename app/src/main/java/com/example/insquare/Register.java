package com.example.insquare;

public class Register {
    String p_uid;
    String p_email;
    String p_pwd;
    String p_name;
    String p_number;
    String p_gender;
    String p_logo;
    String p_department;
    String p_position;
    String p_company;
    String p_address;


    public Register() { }
    public String getP_uid() {
        return p_uid;
    }
    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }
    public String getP_email() {
        return p_email;
    }
    public void setP_email(String p_email) {
        this.p_email = p_email;
    }
    public String getP_pwd() {
        return p_pwd;
    }
    public void setP_pwd(String p_pwd) {
        this.p_pwd = p_pwd;
    }
    public String getP_name() {
        return p_name;
    }
    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
    public String getP_number() {
        return p_number;
    }
    public void setP_number(String p_number) {
        this.p_number = p_number;
    }
    public String getP_gender() {
        return p_gender;
    }
    public void setP_gender(String p_gender) {
        this.p_gender = p_gender;
    }

    public Register(String p_name, String p_company, String p_department, String p_position, String p_address, String p_number, String p_email, String p_logo) {
        this.p_name = p_name;
        this.p_company = p_company;
        this.p_department = p_department;
        this.p_position = p_position;
        this.p_address = p_address;
        this.p_number = p_number;
        this.p_email = p_email;
        this.p_logo = p_logo;
    }

    public String toQRString() {
        return p_name + "," +
                p_company + "," +
                p_department + "," +
                p_position + "," +
                p_address + "," +
                p_email + "," +
                p_number + "," +
                p_logo;
    }
}
