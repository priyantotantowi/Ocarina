package me.pete.ocarinalibrary.helper;

import android.content.Context;
import android.text.format.Formatter;

public class StringHelper {
    public static String extensionWithSpace(String message, int maxLenght) {
        String result = "";
        String space = "";
        for(int i = 0; i < maxLenght - message.length(); i++) {
            space += " ";
        }

        result = message + space;
        return result;
    }

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

    public static String removeAlphabethics(String text) {
        return text.replaceAll("[^\\d.]", "");
    }

    public static String removeNewline(String text) {
        return text.replace("\n", " ");
    }

    public static String removeNumerics(String text) {
        return text.replaceAll("[^A-Za-z]", "");
    }

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

    public static byte[] toByte(String value) {
        return value.getBytes();
    }

    public static String toFormatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

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

    public static String removeNull(String value) {
        String result = "";
        if(value != null) {
            result = value;
        }
        return result;
    }
}
