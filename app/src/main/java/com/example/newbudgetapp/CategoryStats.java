package com.example.newbudgetapp;

public class CategoryStats {
    private String category;
    private double amount;
    private double percentage;

    public CategoryStats(String category, double amount, double percentage) {
        this.category = category;
        this.amount = amount;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public double getPercentage() {
        return percentage;
    }
} 