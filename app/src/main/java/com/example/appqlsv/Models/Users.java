package com.example.appqlsv.Models;

import java.io.Serializable;

public class Users implements Serializable {
    private int id_khachhang;
    private String fullname;
    private String email;
    private String password;
    private String sodienthoai;
    private String diachi;
    private int is_admin;
    private Boolean status;
    private String photo;
    private int studentid;

    public Users(int id_khachhang, String fullname, String email, int is_admin,String photo,String sodienthoai) {
        this.id_khachhang = id_khachhang;
        this.fullname = fullname;
        this.email = email;
        this.is_admin = is_admin;
        this.photo = photo;
        this.sodienthoai = sodienthoai;
    }

    public Users(int id_khachhang, String fullname, String email, String sodienthoai, String diachi, String photo) {
        this.id_khachhang = id_khachhang;
        this.fullname = fullname;
        this.email = email;
        this.sodienthoai = sodienthoai;
        this.diachi = diachi;
        this.photo = photo;
    }


    public Users(int id_khachhang, String fullname, String photo) {
        this.id_khachhang = id_khachhang;
        this.fullname = fullname;
        this.photo = photo;
    }

    public Users(String email) {
        this.email = email;
    }

    public Users(int id_khachhang, String fullname, String email, String password, String sodienthoai, String diachi, int is_admin, Boolean status) {
        this.id_khachhang = id_khachhang;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.sodienthoai = sodienthoai;
        this.diachi = diachi;
        this.is_admin = is_admin;
        this.status = status;
    }

    public Users(int id_khachhang, String fullname) {
        this.id_khachhang = id_khachhang;
        this.fullname = fullname;
    }
    public Users(int id_khachhang, String fullname, String email, String password, String sodienthoai, int is_admin, String photo, int studentid) {
        this.id_khachhang = id_khachhang;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.sodienthoai = sodienthoai;
        this.is_admin = is_admin;
        this.photo = photo;
        this.studentid = studentid;
    }
    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId_khachhang() {
        return id_khachhang;
    }

    public void setId_khachhang(int id_khachhang) {
        this.id_khachhang = id_khachhang;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
