package com.example.newbudgetapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class Transaction implements Parcelable {
    private long id;
    private String type;
    private double amount;
    private String description;
    private Date date;
    private String category;

    public Transaction(String type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = new Date();
        this.category = "Autre"; // Catégorie par défaut
    }

    protected Transaction(Parcel in) {
        id = in.readLong();
        type = in.readString();
        amount = in.readDouble();
        description = in.readString();
        date = new Date(in.readLong());
        category = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(type);
        dest.writeDouble(amount);
        dest.writeString(description);
        dest.writeLong(date.getTime());
        dest.writeString(category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
} 