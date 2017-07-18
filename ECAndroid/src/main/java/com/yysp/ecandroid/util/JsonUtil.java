package com.yysp.ecandroid.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.jkframework.algorithm.JKFile;
import com.jkframework.debug.JKLog;

import cn.trinea.android.common.util.StringUtils;

/**
 * Created by Administrator on 2017/7/18.
 */

public class JsonUtil {
    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    public static boolean isGoodJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            JKLog.i("RT", "JPush_erros" + e.getLocalizedMessage());
            return false;
        }
    }
}
