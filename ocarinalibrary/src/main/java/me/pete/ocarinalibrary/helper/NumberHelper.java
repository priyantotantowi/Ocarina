package me.pete.ocarinalibrary.helper;

public final class NumberHelper {
    /**
     * This function returns beautiful number with string type from your value.
     *
     * If you put "1000.0" on parameter, this function will return "1,000".
     * If you put "2500.5" on parameter, this function will return "2,500.5" also.
     */
    public static String beautifulNumber(double value) {
        String newValue = removeDecimalIfInteger(value);
        String currencyValue = CurrencyHelper.doubleToCurrency(DoubleHelper.parseDouble(newValue)).split("\\.")[0];
        if(newValue.contains(".")) {
            return currencyValue + "." + newValue.split("\\.")[0];
        } else {
            return currencyValue;
        }
    }

    /**
     * This function returns remove decimal number if the decimal value is 0.
     *
     * If you put "2.0" on parameter, this function will return "2".
     * If you put "3.5" on parameter, this function will return "3.5" also.
     */
    public static String removeDecimalIfInteger(double value) {
        int iValue = (int) value;
        if(iValue < value) {
            return Double.toString(value);
        } else {
            return Integer.toString(iValue);
        }
    }
}
