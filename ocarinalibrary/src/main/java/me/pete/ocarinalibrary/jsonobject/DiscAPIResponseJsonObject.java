package me.pete.ocarinalibrary.jsonobject;

import java.util.ArrayList;

public class DiscAPIResponseJsonObject {
    private int CODE = 0;
    private String MESSAGE = "";
    private ArrayList<DiscAPIDataResponseJsonObject> DATA = new ArrayList<>();

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

    public ArrayList<DiscAPIDataResponseJsonObject> getDATA() {
        return DATA;
    }

    public void setDATA(ArrayList<DiscAPIDataResponseJsonObject> DATA) {
        this.DATA = DATA;
    }
}
