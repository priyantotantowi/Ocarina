package me.pete.ocarinalibrary.helper;

import android.content.Context;
import android.text.format.Formatter;

/**
 * Created by Priyanto Tantowi.
 *
 * StringHelper is helper to all about data type of string.
 */
public abstract class StringHelper {
    /**
     * This function is used for add space character at the end of string
     * until specified string length.
     *
     * The returns of this function is result for string with additions
     * space character.
     */
    public static String extensionWithSpace(String message, int maxLenght) {
        String result = "";
        String space = "";
        for(int i = 0; i < maxLenght - message.length(); i++) {
            space += " ";
        }

        result = message + space;
        return result;
    }

    /**
     * This function returns a string with uppercase.
     */
    public static String toCapitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * This function returns a string with remove all alphabethics.
     */
    public static String removeAlphabethics(String text) {
        return text.replaceAll("[^\\d.]", "");
    }

    /**
     * This function returns a string with replacing new line with space character.
     */
    public static String removeNewline(String text) {
        return text.replace("\n", " ");
    }

    /**
     * This function returns a string with remove all numerics.
     */
    public static String removeNumerics(String text) {
        return text.replaceAll("[^A-Za-z]", "");
    }

    /**
     * This function returns a string with remove all symbol.
     * This function returns only alphabet and number characters
     * otherwise will be omitted
     */
    public static String removeSymbols(String text){
        StringBuilder filteredStringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (Character.isLetterOrDigit(currentChar) || Character.isSpaceChar(currentChar)) {
                filteredStringBuilder.append(currentChar);
            }
        }
        return filteredStringBuilder.toString();
    }

    /**
     * This function returns a byte of string.
     */
    public static byte[] toByte(String value) {
        return value.getBytes();
    }

    /**
     * This function returns a beautiful format for byte size.
     */
    public static String toFormatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * This function returns boolean value. True if your text contains number character
     * and false if not contains number character
     */
    public static boolean isNumber(String text) {
        boolean result = true;
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (Character.isLetter(currentChar)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * This function returns boolean value. True if your text contains alphabet character
     * and false if not contains alphabet character
     */
    public static boolean isAlphabet(String text) {
        boolean result = true;
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (Character.isDigit(currentChar)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * This function returns empty string if your text is null and return your text
     * if your text is not null
     */
    public static String removeNull(String value) {
        String result = "";
        if(value != null) {
            result = value;
        }
        return result;
    }
}
