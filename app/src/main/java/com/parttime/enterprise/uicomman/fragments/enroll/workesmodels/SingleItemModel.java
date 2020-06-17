package com.parttime.enterprise.uicomman.fragments.enroll.workesmodels;

public class SingleItemModel {


    private String title;
    private String id;
    private String imgUser;
    private String subTitle;
    private String time;
    private String wifyStatus;
    private int workNetwork;


    public SingleItemModel() {
    }

    public SingleItemModel(String title, String id, String imgUser, String subTitle, String time, String wifyStatus,int workNetwork) {
        this.title = title;
        this.id = id;
        this.imgUser = imgUser;
        this.subTitle = subTitle;
        this.time = time;
        this.wifyStatus = wifyStatus;
        this.workNetwork=workNetwork;
    }

    public int getWorkNetwork() {
        return workNetwork;
    }

    public void setWorkNetwork(int workNetwork) {
        this.workNetwork = workNetwork;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWifyStatus() {
        return wifyStatus;
    }

    public void setWifyStatus(String wifyStatus) {
        this.wifyStatus = wifyStatus;
    }
}
