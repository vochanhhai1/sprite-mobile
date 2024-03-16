package com.example.appqlsv.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private int price;
    private String image;
    private String mota;
    private String createAt;
    private String deleteAt;
    private int id_danhmuc;
    private int orderCount = 0;
    private double averageRating;
    private boolean isFavorited=true;


    public Product(int id, String name, int price, String image, String mota, String createAt, String deleteAt, int id_danhmuc, boolean isFavorited) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.mota = mota;
        this.createAt = createAt;
        this.deleteAt = deleteAt;
        this.id_danhmuc = id_danhmuc;
        this.isFavorited = isFavorited;
    }


    public Product(int id, String name, int price, String image, int id_danhmuc,int orderCount,double averageRating, boolean isFavorited) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.id_danhmuc = id_danhmuc;
        this.averageRating = averageRating;
        this.orderCount = orderCount;
        this.isFavorited = isFavorited;
    }

    public Product() {
    }

    public Product(int id, String name, int price, String image, int orderCount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.orderCount = orderCount;
    }

    public Product(int id, String name, int price, String image, int orderCount, double averageRating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.orderCount = orderCount;
        this.averageRating = averageRating;
    }

    public Product(int id, String name, int price, String image, int orderCount, double averageRating,boolean isFavorited) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.orderCount = orderCount;
        this.averageRating = averageRating;
        this.isFavorited = isFavorited;
    }
    public Product(int id, String name, int price, String image,boolean isFavorited) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.isFavorited = isFavorited;
    }


    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getId_danhmuc() {
        return id_danhmuc;
    }

    public void setId_danhmuc(int id_danhmuc) {
        this.id_danhmuc = id_danhmuc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(String deleteAt) {
        this.deleteAt = deleteAt;
    }
}
