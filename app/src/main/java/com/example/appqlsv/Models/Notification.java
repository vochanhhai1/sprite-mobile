package com.example.appqlsv.Models;

public class Notification {
    private Integer id;
    private String message;
    private Boolean isSeen;
    private String createdAt;
    private String updatedAt;
    private Integer userId;
    private String type;
    private Order order;

    public Notification(Integer id, Boolean isSeen, String createdAt, String updatedAt, Order order) {
        this.id = id;
        this.isSeen = isSeen;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.order = order;
    }

    public Notification() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
