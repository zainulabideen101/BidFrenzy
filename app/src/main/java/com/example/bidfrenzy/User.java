package com.example.bidfrenzy;

public class User {
    private String UserName, UserEmail, UserPass, PhoneNumber;


    public User(String UserName, String UserEmail, String UserPass, String PhoneNumber) {
        UserName=this.UserName;
        UserEmail=this.UserEmail;
        UserPass=this.UserPass;
        PhoneNumber=this.PhoneNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


}
