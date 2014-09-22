package com.gamecockmobile;

public class NavDrawerItem {

    private String title;
    private int resId;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem() {
    }

    public NavDrawerItem(String title, int resId) {
        this.title = title;
        this.resId = resId;
    }

    public NavDrawerItem(String title, int resId, boolean isCounterVisible, String count) {
        this.title = title;
        this.resId = resId;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle() {
        return this.title;
    }

    public int getResId() {
        return this.resId;
    }

    public String getCount() {
        return this.count;
    }

    public boolean getCounterVisibility() {
        return this.isCounterVisible;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }
}
