package com.example.preloveditemandevent;

import android.widget.TextView;

public class SpecificEvent {
    String volId, userId, volEventId;
    String  volFirstName, volLastName, volPhone, volEventName;

    public SpecificEvent() {
    }

    public SpecificEvent(String userId, String volId , String volEventId, String volFirstName, String volLastName, String volPhone, String volEventName) {
        this.userId = userId;
        this.volId = volId;
        this.volEventId = volEventId;
        this.volFirstName = volFirstName;
        this.volLastName = volLastName;
        this.volPhone = volPhone;
        this.volEventName = volEventName;
    }

    public String getVolEventId() {
        return volEventId;
    }

    public void setVolEventId(String volEventId) {
        this.volEventId = volEventId;
    }

    public String getVolId() {
        return volId;
    }

    public void setVolId(String volId) {
        this.volId = volId;
    }

    public String getVolFirstName() {
        return volFirstName;
    }

    public void setVolFirstName(String volFirstName) {
        this.volFirstName = volFirstName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVolLastName() {
        return volLastName;
    }

    public void setVolLastName(String volLastName) {
        this.volLastName = volLastName;
    }

    public String getVolPhone() {
        return volPhone;
    }

    public void setVolPhone(String volPhone) {
        this.volPhone = volPhone;
    }

    public String getVolEventName() {
        return volEventName;
    }

    public void setVolEventName(String volEventName) {
        this.volEventName = volEventName;
    }
}
