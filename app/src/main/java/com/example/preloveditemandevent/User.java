package com.example.preloveditemandevent;

import android.widget.TextView;

public class User {

    String userId,firstName,lastName,userName,phoneNo,email,address;

    public User() {
    }

    public User(String userId, String firstName, String lastName, String userName, String phoneNo, String email, String address) {

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.email = email;
        this.address = address;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
