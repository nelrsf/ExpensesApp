package com.unab.tads.expensesapp.model.entities;

import java.io.Serializable;


public class User implements Serializable {
    private static final String DEFAULT_NAME = "Jon Snow";
    private static final String DEFAULT_COMPANY = "The Nigth Watch";
    private static final String DEFAULT_EMAIL_ADDRESS = "jonsnow@nigthwatch.wall";
    private String name;
    private String company;
    private String emailAddress;

    public User(String name, String company, String emailAddress) {
        this.name = name;
        this.company = company;
        this.emailAddress = emailAddress;
    }
    public User() {
        this.name = DEFAULT_NAME;
        this.company = DEFAULT_COMPANY;
        this.emailAddress = DEFAULT_EMAIL_ADDRESS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
