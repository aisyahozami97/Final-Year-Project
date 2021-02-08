package com.example.preloveditemandevent;

import java.util.ArrayList;

public class PurchaseOrder {

     ArrayList<CartItem> listCart;
    String orderId, userId, itemId;
    String  custFirstName, custLastName, custPhone, custAddress;
    //String orderItemName, orderItemDesc, orderItemPrice,orderItemAvailability,orderTotalPrice;
    String orderStatus, orderDate, totalPrice, paymentReceipt;

    public PurchaseOrder() {
    }

    public PurchaseOrder(String userid, String orderId, String custFirstName, String custLastName, String custPhone, String custAddress, ArrayList<CartItem> listCart, String orderStatus, String orderDate, String totalPrice, String paymentReceipt ) {
        this.orderId = orderId;
        this.userId = userid;
        this.custFirstName = custFirstName;
        this.custLastName = custLastName;
        this.custPhone = custPhone;
        this.custAddress = custAddress;
        this.listCart = listCart;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.paymentReceipt = paymentReceipt;

    }

    public PurchaseOrder(String userid, String orderId, String custFirstName, String custLastName, String custPhone, String custAddress, String orderStatus, String orderDate, String totalPrice, String paymentReceipt) {
        this.orderId = orderId;
        this.userId = userid;
        this.custFirstName = custFirstName;
        this.custLastName = custLastName;
        this.custPhone = custPhone;
        this.custAddress = custAddress;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.paymentReceipt = paymentReceipt;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<CartItem> getListCart() {
        return listCart;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName = custFirstName;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = custLastName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }



    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentReceipt() {
        return paymentReceipt;
    }

    public void setPaymentReceipt(String paymentReceipt) {
        this.paymentReceipt = paymentReceipt;
    }
}
