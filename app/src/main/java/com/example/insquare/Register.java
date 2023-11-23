package com.example.insquare;

public class Register {
    String p_id;
    String p_pwd;
    String p_name;
    String p_number;
    String p_gender;

    public Register() { }
    public String getP_email() {
        return p_id;
    }
    public void setP_email(String p_email) {
        this.p_id = p_email;
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

    public Register(String p_id, String p_pwd, String p_name, String p_number, String p_gender) {
        this.p_id = p_id;
        this.p_pwd = p_pwd;
        this.p_name = p_name;
        this.p_number = p_number;
        this.p_gender = p_gender;
    }
}
