package com.cmsc436.quickbite;

import java.util.ArrayList;

/**
 * Created by dominic on 5/17/16.
 */
public class User {
    public static String uidKey = "currUID";
    public static String firstNameKey = "currFirstName";
    public static String lastNameKey = "currLastName";

    private String uid;
    private String firstName;
    private String lastName;

    public User() {}

    public User(String uid, String firstName, String lastName) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public String abbreviatedName() {
        return firstName + " " + lastName.substring(0, 1) + ".";
    }
}
