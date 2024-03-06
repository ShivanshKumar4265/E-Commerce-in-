package com.inventics.e_commerce.modal;

public class cart {
    String pro_key,timestamp;
    int qty;

    public cart(String pro_key, String timestamp, int qty) {
        this.pro_key = pro_key;
        this.timestamp = timestamp;
        this.qty = qty;
    }

    public cart() {
    }

    public String getPro_key() {
        return pro_key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getQty() {
        return qty;
    }

    public void setPro_key(String pro_key) {
        this.pro_key = pro_key;
    }
}
