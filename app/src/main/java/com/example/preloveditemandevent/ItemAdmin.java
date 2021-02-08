package com.example.preloveditemandevent;

public class ItemAdmin {
    String itemId;
    String itemName;
    String itemDesc;
    String itemCategory;
    String itemAvailability;
    String itemDate;
    String itemImage;
    String itemPrice;

    public ItemAdmin() {
    }

    public ItemAdmin(String itemId, String itemName, String itemDesc, String itemImage,  String itemCategory,  String itemAvailability,    String itemDate,  String itemPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemCategory = itemCategory;
        this.itemAvailability = itemAvailability;
        this.itemDate = itemDate;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
    }

    public ItemAdmin(String trim, String s){

    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemAvailability() {
        return itemAvailability;
    }

    public void setItemAvailability(String itemAvailability) {
        this.itemAvailability = itemAvailability;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}
