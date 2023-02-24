package com.example.messenger.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryMess implements Serializable {
   private String idMess;
   private String idUserLastSent;
   private String messLastSent;
   private String messWithUser;
   private Boolean seen;
   private String timeLastSent;

   private List<String> member = new ArrayList<>();

    public HistoryMess() {
    }



    public HistoryMess( String idMess, String idUserLastSent, String messLastSent, String messWithUser, Boolean seen, String timeLastSent) {
        this.idMess = idMess;
        this.idUserLastSent = idUserLastSent;
        this.messLastSent = messLastSent;
        this.messWithUser = messWithUser;
        this.seen = seen;
        this.timeLastSent = timeLastSent;
    }

    public HistoryMess(String idMess, String idUserLastSent, String messLastSent, String messWithUser, Boolean seen, String timeLastSent, List<String> member) {
        this.idMess = idMess;
        this.idUserLastSent = idUserLastSent;
        this.messLastSent = messLastSent;
        this.messWithUser = messWithUser;
        this.seen = seen;
        this.timeLastSent = timeLastSent;
        this.member = member;
    }

    public String getIdMess() {
        return idMess;
    }

    public void setIdMess(String idMess) {
        this.idMess = idMess;
    }

    public String getIdUserLastSent() {
        return idUserLastSent;
    }

    public void setIdUserLastSent(String idUserLastSent) {
        this.idUserLastSent = idUserLastSent;
    }

    public String getMessLastSent() {
        return messLastSent;
    }

    public void setMessLastSent(String messLastSent) {
        this.messLastSent = messLastSent;
    }

    public String getMessWithUser() {
        return messWithUser;
    }

    public void setMessWithUser(String messWithUser) {
        this.messWithUser = messWithUser;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getTimeLastSent() {
        return timeLastSent;
    }

    public void setTimeLastSent(String timeLastSent) {
        this.timeLastSent = timeLastSent;
    }

    public List<String> getMember() {
        return member;
    }

    public void setMember(List<String> member) {
        this.member = member;
    }

    // map update cho nhanh
    public Map<String, Object> toMap(){
        HashMap<String, Object> mapReulf = new HashMap<>();
        mapReulf.put("idMess", this.idMess);
        mapReulf.put("idUserLastSent", this.idUserLastSent);
        mapReulf.put("messLastSent", this.messLastSent);
        mapReulf.put("messWithUser", this.messWithUser);
        mapReulf.put("seen", this.seen);
        mapReulf.put("timeLastSent", this.timeLastSent);
        return mapReulf;
    }

    @Override
    public String toString() {
        return "HistoryMess{" +
                "idMess='" + idMess + '\'' +
                ", idUserLastSent='" + idUserLastSent + '\'' +
                ", messLastSent='" + messLastSent + '\'' +
                ", messWithUser='" + messWithUser + '\'' +
                ", seen=" + seen +
                ", timeLastSent='" + timeLastSent + '\'' +
                ", member=" + member.size() +
                '}';
    }
}
