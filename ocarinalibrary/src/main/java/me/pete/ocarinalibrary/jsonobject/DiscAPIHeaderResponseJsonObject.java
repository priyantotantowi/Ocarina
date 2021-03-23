package me.pete.ocarinalibrary.jsonobject;

import java.util.ArrayList;

public class DiscAPIHeaderResponseJsonObject {
    private int CODE = 0;
    private String MESSAGE = "";
    private ArrayList<DiscAPIHeaderDataResponseJsonObject> DATA = new ArrayList<>();

    public int getCODE() {
        return CODE;
    }

    public void setCODE(int CODE) {
        this.CODE = CODE;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public ArrayList<DiscAPIHeaderDataResponseJsonObject> getDATA() {
        return DATA;
    }

    public void setDATA(ArrayList<DiscAPIHeaderDataResponseJsonObject> DATA) {
        this.DATA = DATA;
    }
}
