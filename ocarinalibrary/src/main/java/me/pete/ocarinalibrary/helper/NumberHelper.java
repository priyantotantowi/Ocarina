package me.pete.ocarinalibrary.helper;

public final class NumberHelper {
    /**
     * This function returns beautiful number with string type from your value.
     *
     * If you put "2.0" on parameter, this function will return "2".
     * If you put "3.5" on parameter, this function will return "3.5" also.
     */
    public static String beautifulNumber(double value) {
        int iValue = (int) value;
        if(iValue < value) {
            return Double.toString(value);
        } else {
            return Integer.toString(iValue);
        }
    }
}
