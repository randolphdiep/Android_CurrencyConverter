package eu.rafalsiuta.simplecalc;

import android.content.Intent;
import android.provider.VoicemailContract;

import com.bumptech.glide.Priority;

import java.text.ParseException;
import java.util.Date;

public class Currency {

    private final static String CODE = "code";
    private final static String FLAG = "flag";

    public static String getCODE() {
        return CODE;
    }

    public static String getFLAG() {
        return FLAG;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    private String currencyCode;
    private String currencyName;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


    public Currency(String cn, String cc) {
        currencyName = cn;
        currencyCode = cc;
    }

    public Currency(Intent intent) {
        this.currencyCode = intent.getStringExtra(Currency.CODE);
        this.flag = intent.getIntExtra(Currency.FLAG, 0);
    }

    public static void packageIntent(Intent intent, String code, int flag) {
        intent.putExtra(Currency.CODE, code);
        intent.putExtra(Currency.FLAG, flag);
    }
}
