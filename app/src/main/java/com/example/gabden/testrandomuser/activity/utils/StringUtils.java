package com.example.gabden.testrandomuser.activity.utils;

public class StringUtils {

    public static String firstCharToUpperCase(String s) {
        char first = Character.toUpperCase(s.charAt(0));
        return first + s.substring(1);
    }
}
