package com.example.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class MD5util {

    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static final String salt = "1a2b3c4d";

    public static String inputPassToFromPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String fromPassToDBPass(String fromPass, String salt) {
        String str = salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        // frontend do this thing
        String fromPass = inputPassToFromPass(inputPass);
        // backend do this thing
        String dbPass = fromPassToDBPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFromPass("123456"));
        System.out.println(fromPassToDBPass("ce21b747de5af71ab5c2e20ff0a60eea", MD5util.salt));
        String t = inputPassToDBPass("123456", "1a2b3c4d");
        System.out.println("t = " + t);
    }

}
