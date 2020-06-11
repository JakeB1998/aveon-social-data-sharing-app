package com.example.madcompetition.BackEnd.account;

import java.io.Serializable;

public class PersonalInformation implements Serializable {

    private String fullName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private  int accountID;
    private boolean accountLinked;


    public PersonalInformation(String fullName, String phoneNumber, String email, int AccountId)
    {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountID = AccountId;
    }






    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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






    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
