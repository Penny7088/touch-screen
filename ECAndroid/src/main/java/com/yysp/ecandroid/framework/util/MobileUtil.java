package com.yysp.ecandroid.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2017/9/27 0027.
 * by penny
 */

public class MobileUtil {

    public static boolean isMobileNum(String num) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher matcher = p.matcher(num);
        return matcher.matches();
    }
}
