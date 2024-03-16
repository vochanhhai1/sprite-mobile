package com.example.appqlsv.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {
    private Integer id;
    private Integer totalPrice = 0;
    private String notes;
    private boolean isReviewed = true;
    private String status;
    private String createdAt;
    private String updatedAt;
    private Integer buyerId;
    private ArrayList<OrderDetails> orderDetails;
    private Users buyer;
    private int surcharge;

    public Order(Integer id, Users buyer) {
        this.id = id;
        this.buyer = buyer;
    }

    public Order(Integer id, Integer totalPrice, String notes, boolean isReviewed, String status, String createdAt, String updatedAt, Integer buyerId) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.notes = notes;
        this.isReviewed = isReviewed;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.buyerId = buyerId;
    }

    public Order() {
    }

    public int getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(int surcharge) {
        this.surcharge = surcharge;
    }

    public Order(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public ArrayList<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Users getBuyer() {
        return buyer;
    }

    public void setBuyer(Users buyer) {
        this.buyer = buyer;
    }
}
