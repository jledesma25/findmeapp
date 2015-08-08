package com.imast.findingme.model;


public class ErrorUser {

    private String[] username;
    private String[] password;
    private String[] password_confirmation;
    private String[] email;

    public ErrorUser(String[] username, String[] password, String[] password_confirmation, String[] email) {
        this.username = username;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.email = email;
    }

    public String[] getUsername() {
        return username;
    }

    public void setUsername(String[] username) {
        this.username = username;
    }

    public String[] getPassword() {
        return password;
    }

    public void setPassword(String[] password) {
        this.password = password;
    }

    public String[] getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String[] password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

}
