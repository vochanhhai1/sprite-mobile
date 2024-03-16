package com.example.appqlsv.Models;

public class Cart {
    private int idct;
    private int userid;
    private int productid;
    private int quantity;
    private Product product;

    public Cart(int idct, int userid, int productid, int quantity, Product product) {
        this.idct = idct;
        this.userid = userid;
        this.productid = productid;
        this.quantity = quantity;
        this.product = product;
    }

    public int getIdct() {
        return idct;
    }

    public void setIdct(int idct) {
        this.idct = idct;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
