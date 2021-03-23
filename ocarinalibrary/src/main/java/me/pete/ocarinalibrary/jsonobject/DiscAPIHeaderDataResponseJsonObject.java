package me.pete.ocarinalibrary.jsonobject;

public class DiscAPIHeaderDataResponseJsonObject {
    private String ORDERNO = "";
    private String DMSNO = "";
    private String PCODE = "";
    private int QTY = 0;
    private int PRICE = 0;
    private String FLAG_DISCOUNT = "";
    private int DISCOUNT = 0;
    private String ORDER_TYPE = "";
    private int LINE_NO = 0;

    public String getORDERNO() {
        return ORDERNO;
    }

    public void setORDERNO(String ORDERNO) {
        this.ORDERNO = ORDERNO;
    }

    public String getDMSNO() {
        return DMSNO;
    }

    public void setDMSNO(String DMSNO) {
        this.DMSNO = DMSNO;
    }

    public String getPCODE() {
        return PCODE;
    }

    public void setPCODE(String PCODE) {
        this.PCODE = PCODE;
    }

    public int getQTY() {
        return QTY;
    }

    public void setQTY(int QTY) {
        this.QTY = QTY;
    }

    public int getPRICE() {
        return PRICE;
    }

    public void setPRICE(int PRICE) {
        this.PRICE = PRICE;
    }

    public String getFLAG_DISCOUNT() {
        return FLAG_DISCOUNT;
    }

    public void setFLAG_DISCOUNT(String FLAG_DISCOUNT) {
        this.FLAG_DISCOUNT = FLAG_DISCOUNT;
    }

    public int getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(int DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    public String getORDER_TYPE() {
        return ORDER_TYPE;
    }

    public void setORDER_TYPE(String ORDER_TYPE) {
        this.ORDER_TYPE = ORDER_TYPE;
    }

    public int getLINE_NO() {
        return LINE_NO;
    }

    public void setLINE_NO(int LINE_NO) {
        this.LINE_NO = LINE_NO;
    }
}
