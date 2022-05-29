package com.my_pls.application.components;

import com.my_pls.securePassword;

import java.sql.Connection;

public class User {
    private String firstName = "";
    private String lastName = "";
    private String password = "";
    private String email = "";
    private int id = -1;
    private String role = "learner";

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

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

    public boolean checkErrorEmail() {
        return !this.email.contains("rit.edu");
    }

    public boolean login(securePassword pwd_manager, Connection conn) {
        User u = Proxy.login(password, email, pwd_manager, conn);
        if (u.getFirstName().length() > 0) {
            this.firstName = u.getFirstName();
            this.lastName = u.getLastName();
            this.role = u.getRole();
            this.id = u.getId();
            Proxy.updateCourses(conn);
            return true;
        } else return false;
    }

    public void erase() {
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        this.email = "";
    }

    public boolean checkPassword(String pass) {
        return (password.equals(pass) && password.length() >= 6);
    }

    public boolean register(securePassword pwd_manager, Connection conn) {
        return Proxy.register(firstName, lastName, email, pwd_manager.hashPassword(password), conn);
    }

    public boolean forgetPassword(Connection conn) {
        return Proxy.forgetPasswordSendEmail(email, conn);
    }

    public boolean changePassword(securePassword pwd_manager, String confirmCode, Connection conn) {
        return Proxy.changePassword(confirmCode, email, pwd_manager.hashPassword(password), conn);
    }

    public void verifyEmailofUser(String hash, Connection conn) {
        boolean flag =  Proxy.verifyEmailofUser(email, hash, conn);
        if (flag) {
            int id = Proxy.getUserIdFromEmail(email, conn);
            Proxy.addDGmember(id,311,conn);
        }
    }

    public boolean resendVerificationEmail(Connection conn) {
        return Proxy.resendEmailConfirmation(email, conn);
    }
}
