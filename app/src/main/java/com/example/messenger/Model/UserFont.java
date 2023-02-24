package com.example.messenger.Model;

import java.io.Serializable;

public class UserFont implements Serializable {
    private String name;
    private String userName;
    private String img;

    public UserFont(String name, String userName, String img) {
        this.name = name;
        this.userName = userName;
        this.img = img;
    }

    public UserFont() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "UserFont{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
