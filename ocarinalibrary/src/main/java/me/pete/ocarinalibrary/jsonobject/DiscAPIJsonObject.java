package me.pete.ocarinalibrary.jsonobject;

import java.util.ArrayList;

public class DiscAPIJsonObject {
    private String DISTID = "";
    private String SLSNO = "";
    private String TRANSDATE = "";
    private String IMEI = "";
    private String VERSION = "";
    private String CLIENT_ID = "";
    private String CUSTNO = "";
    private String ORDERNO = "";
    private String ORDER_TYPE = "";
    private String DMSNO = "";
    private ArrayList<DiscAPIOrderJsonObject> ORDER = new ArrayList<>();

    public String getDISTID() {
        return DISTID;
    }

    public void setDISTID(String DISTID) {
        this.DISTID = DISTID;
    }

    public String getSLSNO() {
        return SLSNO;
    }

    public void setSLSNO(String SLSNO) {
        this.SLSNO = SLSNO;
    }

    public String getTRANSDATE() {
        return TRANSDATE;
    }

    public void setTRANSDATE(String TRANSDATE) {
        this.TRANSDATE = TRANSDATE;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }

    public String getCUSTNO() {
        return CUSTNO;
    }

    public void setCUSTNO(String CUSTNO) {
        this.CUSTNO = CUSTNO;
    }

    public String getORDERNO() {
        return ORDERNO;
    }

    public void setORDERNO(String ORDERNO) {
        this.ORDERNO = ORDERNO;
    }

    public String getORDER_TYPE() {
        return ORDER_TYPE;
    }

    public void setORDER_TYPE(String ORDER_TYPE) {
        this.ORDER_TYPE = ORDER_TYPE;
    }

    public String getDMSNO() {
        return DMSNO;
    }

    public void setDMSNO(String DMSNO) {
        this.DMSNO = DMSNO;
    }

    public ArrayList<DiscAPIOrderJsonObject> getORDER() {
        return ORDER;
    }

    public void setORDER(ArrayList<DiscAPIOrderJsonObject> ORDER) {
        this.ORDER = ORDER;
    }
}
