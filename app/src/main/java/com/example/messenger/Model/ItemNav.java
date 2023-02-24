package com.example.messenger.Model;

public class ItemNav {
    private int logo;
    private String title;
    private int index;

    public ItemNav(int logo, String title, int index) {
        this.logo = logo;
        this.title = title;
        this.index = index;
    }

    public ItemNav() {
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
