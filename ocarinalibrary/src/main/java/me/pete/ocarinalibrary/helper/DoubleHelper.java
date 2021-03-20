package me.pete.ocarinalibrary.helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Priyanto Tantowi.
 *
 * DateTime is helper to all about data type of double.
 */
public final class DoubleHelper {
    /**
     * This function returns value of double in string to replacing exponential to numeric.
     */
    public static String bigValue(double value) {
        NumberFormat numberFormat = new DecimalFormat("#0.0000");
        String result = numberFormat.format(value).replace(",", ".");
        return result;
    }

    /**
     * This function returns value of double from string.
     * This function can handle if your string can't to parse then
     * this function returns 0 as result.
     */
    public static double parseDouble(String value) {
        double result;
        try {
            result = Double.parseDouble(value);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }
}
