package com.example.preloveditemandevent;


public class Event {
    public String event_id;
    public String event_name;
    public String event_image;

    public Event(){}

    public Event(String event_id, String event_name, String event_image) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_image = event_image;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }
}
