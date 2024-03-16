package com.example.appqlsv.Models;

public class RoomMember {
    private int id;
    private Users users;
    private int roomid;
    private String createAt;
    private String updateAt;

    public RoomMember(int id, Users users, int roomid, String createAt, String updateAt) {
        this.id = id;
        this.users = users;
        this.roomid = roomid;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
