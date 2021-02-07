package com.example.ecommerce;

import java.io.Serializable;

public class Customer implements Serializable {
    String name;
    String username;
    String pass;
    String gender;
    String birthdate;
    String job;


    public Customer(String name, String username, String pass, String gender, String birthdate, String job) {
        this.name = name;
        this.username = username;
        this.pass = pass;
        this.gender = gender;
        this.birthdate = birthdate;
        this.job = job;
    }
}
