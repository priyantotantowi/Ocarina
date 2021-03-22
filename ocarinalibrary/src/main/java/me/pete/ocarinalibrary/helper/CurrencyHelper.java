package me.pete.ocarinalibrary.helper;

import java.text.NumberFormat;
import java.util.Locale;

import me.pete.ocarinalibrary.enumerator.CurrencyEnum;

/**
 * Created by Priyanto Tantowi.
 *
 * CurrencyHelper is helper all about currency.
 */
public final class CurrencyHelper {
    /**
     * This function returns currency format from double without currency symbol.
     */
    public static String doubleToCurrency(double value) {
        try {
            String sign = "";
            boolean minus = false;

            if(value < 0) {
                minus = true;
                value = value * -1;
            }

            String currency = DoubleHelper.bigValue(value);
            String[] currencies = currency.split("\\.");
            String result = "";
            int index = 1;
            for(int i = currencies[0].length() - 1; i >= 0; i--) {
                result = currencies[0].substring(i, i+1) + result;
                if(index == 3 && i > 0) {
                    result = "," + result;
                    index = 1;
                } else {
                    index++;
                }
            }

            String cent = currencies[1];

            if(minus) {
                return "(" + result + "." + cent + ")";
            } else {
                return result + "." + cent;
            }
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * This function returns currency format from double without currency symbol.
     * If cent is 0 then returns value without cent.
     */
    public static String doubleToCurrencyRemoveDecimalIfInteger(double value) {
        try {
            String cent = "";
            String result = "";
            boolean minus = false;
            String currency = doubleToCurrency(value);
            if(currency.contains("(")) {
                minus = true;
                currency = currency.replace("(", "");
                currency = currency.replace(")", "");
            }

            String[] currencies = currency.split("\\.");

            if(currencies[1].contentEquals("0000") || currencies[1].contentEquals("00")) {
                result = currencies[0];
            } else {
                result = currencies[0] + "." + currencies[1];
            }

            if(minus) {
                return "(" + result + ")";
            } else {
                return result;
            }
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * This function returns currency format from double with currency symbol.
     */
    public static String doubleToCurrencySigned(double value, CurrencyEnum currencyEnum) {
        try {
            String sign = "";
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
            if(currencyEnum == CurrencyEnum.DOLLAR){
                numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
                sign = "$";
            } else if(currencyEnum == CurrencyEnum.RUPIAH){
                numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
                sign = "$";
            }
            String currency = numberFormat.format(value);
            currency = currency.replace(sign, "");
            return getCurrency(currencyEnum) + currency;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * This function returns double from currency format.
     *
     * @param currency  This currency must be without currency symbols
     */
    public static double currencyToDouble(String currency) {
        double result = 0;
        boolean minus = false;
        if(currency.startsWith("(") && currency.endsWith(")")) {
            minus = true;
            currency = currency.replace("(", "");
            currency = currency.replace(")", "");
        }

        currency = currency.replace(",", "");

        result = DoubleHelper.parseDouble(currency);
        if(minus) {
            result = result * -1;
        }

        return result;
    }

    private static String getCurrency(CurrencyEnum currencyEnum) {
        if(currencyEnum == CurrencyEnum.RUPIAH){
            return "Rp. ";
        }else if(currencyEnum == CurrencyEnum.DOLLAR) {
            return "$ ";
        }
        return "";
    }

    private static String getSeparator(String value) {
        String result = "";
        int index = 0;
        for(int i = value.length(); i > 0; i--){
            if(index == 3){
                result = "," + result;
                index = 1;
            }else{
                index++;
            }
            result = value.substring(i - 1, i) + result;
        }
        return result;
    }
}
