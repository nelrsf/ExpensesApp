package com.unab.tads.expensesapp.model.entities;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;


public class Spending implements Serializable {
    private String spendingId;
    private String description;
    private Double amount;
    private Integer category;
    private Date createdAt;
    private String imageUrl;
    private String projectId;
    private Uri imageUri;
    private int recyclerViewPosition;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Spending(String description, Double amount, Integer category, String imageUrl) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.imageUrl = imageUrl;
        createdAt = new Date();
        this.projectId = "";
        this.spendingId = "";
        this.imageUri = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Spending() {
        this.description = "";
        this.amount = 0d;
        this.category = 0;
        this.imageUrl = "https://firebasestorage.googleapis.com/v0/b/expensesapp-1751a.appspot.com/o/images.png?alt=media&token=d3f13502-87f3-41a2-96b4-b488917e1add";
        createdAt = new Date();
        this.projectId = "";
        this.spendingId = "";
        this.imageUri = null;
    }

    @Exclude
    public int getRecyclerViewPosition() {
        return recyclerViewPosition;
    }

    @Exclude
    public void setRecyclerViewPosition(int recyclerViewPosition) {
        this.recyclerViewPosition = recyclerViewPosition;
    }

    @Exclude
    public Uri getImageUri() {
        return imageUri;
    }

    @Exclude
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getSpendingId() {
        return spendingId;
    }

    public void setSpendingId(String spendingId) {
        this.spendingId = spendingId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAmountFormatted(){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        return format.format(this.amount);
    }
}
