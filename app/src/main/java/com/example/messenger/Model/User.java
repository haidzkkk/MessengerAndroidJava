package com.example.messenger.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String name;
    private String userName;
    private String pass;
    private String email;
    private String img;
    private boolean status;
    private boolean login;


    public User() {
    }


    public User(String id, String name, String userName, String pass, String email, String img, boolean status, boolean login) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.pass = pass;
        this.email = email;
        this.img = img;
        this.status = status;
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                ", img='" + "img" + '\'' +
                ", status=" + status +
                ", login=" + login +
                '}';
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> mapResult = new HashMap<>();
            mapResult.put("name", this.name);
            mapResult.put("pass", this.pass);
            mapResult.put("email", this.email);
            mapResult.put("img", "this.img");
            mapResult.put("status", this.status);
            mapResult.put("login", this.login);
//            mapResult.put("historyMap", this.listHistory);
            return mapResult;
    }
}
