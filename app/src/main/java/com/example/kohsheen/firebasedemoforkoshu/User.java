package com.example.kohsheen.firebasedemoforkoshu;

/**
 * Created by kohsheen on 7/1/18.
 */

public class User {

    public String userName;
    public String fullName;

    public User(String userName, String fullName) {
        this.userName = userName;
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

}
