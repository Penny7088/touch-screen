package com.yysp.ecandroid.task;

import com.yysp.ecandroid.data.TestBeautifyModel;
import com.yysp.ecandroid.data.test;
import com.yysp.ecandroid.framework.distribute.IStrategy;
import com.yysp.ecandroid.framework.distribute.SuperTask;
import com.yysp.ecandroid.framework.util.ContactUtil;
import com.yysp.ecandroid.framework.util.Logger;

import java.util.ArrayList;

/**
 * Created on 2017/9/20 0020.
 * by penny
 */

public class InsertContactTask extends SuperTask implements IStrategy {
    @Override
    public void running() {
        insertContact();
    }

    private void insertContact() {
        String lJson = test.getJson(mSuper.getApplicationContext(), "beautify.json");
        ArrayList<TestBeautifyModel> lBeautifyModels = test.parseData(lJson);
        Logger.d("onResume", "================" + lBeautifyModels.size());
        if (lBeautifyModels != null && lBeautifyModels.size() != 0) {
            int lSize = lBeautifyModels.size();
            for (int i = 0; i < lSize; i++) {
                ContactUtil.addContact(mContext,
                        lBeautifyModels.get(i).getPhone(),
                        lBeautifyModels.get(i).getCompany());
            }

            //返回结果

        }
    }

    @Override
    public void running(ArrayList pArrayList) {

    }
}
