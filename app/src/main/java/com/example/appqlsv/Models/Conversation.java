package com.example.appqlsv.Models;

import java.util.ArrayList;

public class Conversation {
    private Integer id;
    private String createdAt;
    private String lastMessage;
    private boolean isSeen;
    private ArrayList<Users> members;
    private String updatedAt;
    private ArrayList<Message> messages;
    private RoomMember roomMembers;

    public Conversation(Integer id, String createdAt, String lastMessage, boolean isSeen, ArrayList<Users> members, String updatedAt, ArrayList<Message> messages) {
        this.id = id;
        this.createdAt = createdAt;
        this.lastMessage = lastMessage;
        this.isSeen = isSeen;
        this.members = members;
        this.updatedAt = updatedAt;
        this.messages = messages;
    }

    public Conversation(Integer id, String createdAt, String lastMessage, boolean isSeen, String updatedAt, RoomMember roomMembers) {
        this.id = id;
        this.createdAt = createdAt;
        this.lastMessage = lastMessage;
        this.isSeen = isSeen;
        this.updatedAt = updatedAt;
        this.roomMembers = roomMembers;
    }

    public RoomMember getRoomMembers() {
        return roomMembers;
    }

    public void setRoomMembers(RoomMember roomMembers) {
        this.roomMembers = roomMembers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public ArrayList<Users> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Users> members) {
        this.members = members;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
