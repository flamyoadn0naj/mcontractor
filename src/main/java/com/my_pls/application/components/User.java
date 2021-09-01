package com.my_pls.application.components;

public class User {
    public String firstName = "";
    public String lastName = "";
    public String password = "";
    public String email = "";
    public int id = -1;
    public String role = "";

    public User(String firstName, String lastName, String password, String email, int id, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.id = id;
        this.role=role;

    }
    public User() {
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        this.id= -1;
        this.role="learner";
    }
    public void setAll(String firstName, String lastName, String password, String email, int id, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.id= id;
        this.role = role;
    }
}
