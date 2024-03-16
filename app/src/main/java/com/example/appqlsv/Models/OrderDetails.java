package com.example.appqlsv.Models;

import java.io.Serializable;

public class OrderDetails implements Serializable {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer price = 0;
    private Integer quantity = 0;
    private Product product;
    private Order orderid;
    private Integer totalquantity;

    public OrderDetails(Integer id, Integer price, Product product) {
        this.id = id;
        this.price = price;
        this.product = product;
    }

    public OrderDetails(Integer id, Integer productId, Integer price, Integer quantity, Product product) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.product = product;
    }

    public OrderDetails(Integer id, Integer productId, Integer price, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderDetails() {
    }

    public OrderDetails(Product product) {
        this.product = product;
    }

    public Integer getTotalquantity() {
        return totalquantity;
    }

    public void setTotalquantity(Integer totalquantity) {
        this.totalquantity = totalquantity;
    }

    public Order getOrderid() {
        return orderid;
    }

    public void setOrderid(Order orderid) {
        this.orderid = orderid;
    }

    public OrderDetails(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
