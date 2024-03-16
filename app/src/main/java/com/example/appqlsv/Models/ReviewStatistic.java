package com.example.appqlsv.Models;

public class ReviewStatistic {
    private Integer quantity;
    private Integer rating;

    public ReviewStatistic(Integer quantity, Integer rating) {
        this.quantity = quantity;
        this.rating = rating;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
