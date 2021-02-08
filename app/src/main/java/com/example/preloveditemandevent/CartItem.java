package com.example.preloveditemandevent;

public class CartItem {
    String cartId, userId, itemId;
    String cartItemName, cartItemDesc,cartItemAvailability,cartTotalPrice;
    String cartItemPrice;

    public CartItem() {
    }

    public CartItem(String cartId, String userId, String itemId, String cartItemName, String cartItemDesc, String cartItemPrice, String cartItemAvailability, String cartTotalPrice) {
        this.cartId = cartId;
        this.userId = userId;
        this.itemId = itemId;
        this.cartItemName = cartItemName;
        this.cartItemDesc = cartItemDesc;
        this.cartItemPrice = cartItemPrice;
        this.cartItemAvailability = cartItemAvailability;
        this.cartTotalPrice = cartTotalPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCartItemName() {
        return cartItemName;
    }

    public void setCartItemName(String cartItemName) {
        this.cartItemName = cartItemName;
    }

    public String getCartItemDesc() {
        return cartItemDesc;
    }

    public void setCartItemDesc(String cartItemDesc) {
        this.cartItemDesc = cartItemDesc;
    }

    public String getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(String cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }

    public String getCartItemAvailability() {
        return cartItemAvailability;
    }

    public void setCartItemAvailability(String cartItemAvailability) {
        this.cartItemAvailability = cartItemAvailability;
    }

    public String getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(String cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
