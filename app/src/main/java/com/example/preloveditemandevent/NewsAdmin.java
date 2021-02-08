package com.example.preloveditemandevent;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Date;

public class NewsAdmin {
    String newsId;
    String newsDesc;
    String newsImage;
    String newsName;
    //Date newsDate;
    //Spinner newsTime;
    String newsLocation;
    //Integer numVolunteer;
    String newsDate, newsTime, numVolunteer;

    public NewsAdmin() {
    }

    public NewsAdmin(String newsId, String newsDesc, String imgurl, String newsName, String newsLocation, String mDisplayDate, SpinnerAdapter spinnerNewsTime, SpinnerAdapter numVolunteer) {
    }

    public NewsAdmin(String newsId, String trim, String s){

    }

    public NewsAdmin(String newsId, String newsDesc,String newsImage, String newsName, String newsLocation, String newsDate, String newsTime,  String numVolunteer) {
        this.newsId = newsId;
        this.newsDesc = newsDesc;
        this.newsImage = newsImage;
        this.newsName = newsName;
        this.newsLocation = newsLocation;
        this.newsDate =newsDate;
        this.newsTime = newsTime;
        this.numVolunteer = numVolunteer;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public String getNewsLocation() {
        return newsLocation;
    }

    public void setNewsLocation(String newsLocation) {
        this.newsLocation = newsLocation;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNumVolunteer() {
        return numVolunteer;
    }

    public void setNumVolunteer(String numVolunteer) {
        this.numVolunteer = numVolunteer;
    }
}
