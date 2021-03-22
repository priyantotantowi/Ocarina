package me.pete.ocarinalibrary.helper;

/**
 * Created by Priyanto Tantowi.
 *
 * IntegerHelper is helper to all about data type of integer.
 */
public final class IntegerHelper {
    /**
     * This function returns value of integer from string.
     * This function can handle if your string can't to parse then
     * this function returns 0 as result.
     */
    public static int parseInt(String value) {
        int result = 0;
        try {
            result = Integer.parseInt(value);
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }
}
