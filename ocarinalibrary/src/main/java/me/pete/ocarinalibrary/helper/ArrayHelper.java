package me.pete.ocarinalibrary.helper;

/**
 * Created by Priyanto Tantowi.
 *
 * ArrayHelper is helper for help your case in array.
 */
public final class ArrayHelper {
    /**
     * This function returns a boolean value for true if
     * found same character in your array.
     */
    public static boolean exists(char[] array, char value) {
        boolean result = false;
        for(char s : array) {
            if(s == value) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This function returns a boolean value for true if
     * found same double in your array.
     */
    public static boolean exists(double[] array, double value) {
        boolean result = false;
        for(double s : array) {
            if(s == value) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This function returns a boolean value for true if
     * found same float in your array.
     */
    public static boolean exists(float[] array, float value) {
        boolean result = false;
        for(float s : array) {
            if(s == value) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This function returns a boolean value for true if
     * found same integer in your array.
     */
    public static boolean exists(int[] array, int value) {
        boolean result = false;
        for(int s : array) {
            if(s == value) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This function returns a boolean value for true if
     * found same long in your array.
     */
    public static boolean exists(long[] array, long value) {
        boolean result = false;
        for(long s : array) {
            if(s == value) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This function returns a boolean value for true if
     * found same string in your array.
     */
    public static boolean exists(String[] array, String value) {
        boolean result = false;
        for(String s : array) {
            if(s.contentEquals(value)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
