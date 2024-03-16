package com.example.appqlsv.Models;

public class Message {
    private int id;
    private String text;
    private Integer senderId;
    private String createdAt;
    private Integer roomId;
    private Users sender;

    public Message(int id, String text, String createdAt, Integer roomId, Users sender) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.roomId = roomId;
        this.sender = sender;
    }

    public Message() {
    }

    public Message(int id, String text, Integer senderId, String createdAt, Integer roomId) {
        this.id = id;
        this.text = text;
        this.senderId = senderId;
        this.createdAt = createdAt;
        this.roomId = roomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }
}
