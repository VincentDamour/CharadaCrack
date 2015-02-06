package com.example.administrateur.charadacrack;

public class Charade {
    private int CharadeID;
    private String CharadeText;
    private String Reponse;

    public Charade(){}

    public Charade( String text, String rep){
        //CharadeID = ID;
        CharadeText = text;
        Reponse = rep;
    }

    public int getId() {
        return CharadeID;
    }
    public void setId(int id) {
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
