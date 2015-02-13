package com.example.administrateur.charadacrack;

public class Charade {
    private String CharadeID;
    private String CharadeText;
    private String Reponse;

    public Charade(){}

    public Charade(String id, String text, String rep){
        CharadeID = id;
        CharadeText = text;
        Reponse = rep;
    }

    public String getId() {
        return CharadeID;
    }
    public void setId(String id) {
        this.CharadeID = id;
    }

    public String getCharadeText() {
        return CharadeText;
    }
    public void setCharadeText(String text) {
        this.CharadeText = text;
    }

    public String getReponse() {
        return Reponse;
    }
    public void setReponse(String rep) {
        this.Reponse = rep;
    }


}
