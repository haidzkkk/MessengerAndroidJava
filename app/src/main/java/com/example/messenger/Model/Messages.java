package com.example.messenger.Model;

public class Messages {
    private int id;
    private String sentBy;
    private String text;
    private int typeText;
    private String time;

    public Messages(int id, String sentBy, String text, int typeText, String time) {
        this.id = id;
        this.sentBy = sentBy;
        this.text = text;
        this.typeText = typeText;
        this.time = time;
    }

    public Messages() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTypeText() {
        return typeText;
    }

    public void setTypeText(int typeText) {
        this.typeText = typeText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", sentBy='" + sentBy + '\'' +
                ", text= asdsad'" + '\'' +
                ", typeText=" + typeText +
                ", time='" + time + '\'' +
                '}';
    }
}

