package com.unab.tads.expensesapp.model.entities;

import com.google.firebase.firestore.Exclude;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

public class Project implements Serializable {
    private static final String DEFAULT_NAME = "My new project";
    private static final String DEFAULT_DESCRIPTION = "Project description";
    private String name;
    private String description;
    private Date date;
    private ArrayList<Spending> expensesList;
    private String userId;
    private String projectId;
    private Double totalAmount;

    public Project(String name, String description, Date date) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.expensesList = new ArrayList<>();
        this.userId = "";
        this.projectId = "";
        this.totalAmount = 0d;
    }

    public Project() {
        this.name = DEFAULT_NAME;
        this.date = new Date();
        this.description = DEFAULT_DESCRIPTION;
        this.expensesList = new ArrayList<>();
        this.userId ="";
        this.projectId = "";
        this.totalAmount = 0d;
    }



    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setSpending(Spending spending){
        this.expensesList.add(spending);
    }

    public ArrayList<Spending> getExpensesList() {
        return expensesList;
    }

    public void setExpensesList(ArrayList<Spending> expensesList) {
        this.expensesList = expensesList;
    }

    public String getTotalAmountFormatted(){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        return format.format(this.totalAmount);
    }
}
