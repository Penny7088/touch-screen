package com.yysp.ecandroid.data;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2017/9/5 0005.
 * by penny
 */

public class test {
    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static ArrayList<TestBeautifyModel> parseData(String result) {//Gson 解析
        ArrayList<TestBeautifyModel> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            detail = gson.fromJson(data.toString(), new TypeToken<List<TestBeautifyModel>>(){}.getType());
//            for (int i = 0; i < data.length(); i++) {
//                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
//                detail.add(entity);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }


    public static boolean isMobileNum(String num) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher matcher = p.matcher(num);
        return matcher.matches();
    }
}
