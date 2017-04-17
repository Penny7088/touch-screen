package com.yysp.ecandroid.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jkframework.algorithm.JKFile;
import com.jkframework.control.JKEditText;
import com.jkframework.control.JKToast;
import com.yysp.ecandroid.R;
import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.view.ECBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2017/4/15.
 */
@EActivity(R.layout.ecandroid_taskacticity)
public class ECTaskActivity extends ECBaseActivity {
    /**
     * 页面初始化
     */
    private boolean bInit = false;

    @ViewById(R.id.tv_deviceId)
    JKEditText Et_deviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            bInit = savedInstanceState.getBoolean("Init", false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putBoolean("Init", bInit);
    }

    @AfterViews
    void InitData() {
        if (!bInit) {
            bInit = true;
        }
    }

    @Click(R.id.bt_postId)
    void posId() {
        JKToast.Show(Et_deviceId.getText().toString(), 0);
    }

    @Click(R.id.bt_writeToFile)
    void writeToFile() {
        String path = "/storage/emulated/legacy/KRQ/";
        String filePath = path + "task.txt";
        if (JKFile.IsSDCardAvailable()) {
            if (JKFile.IsExists(path)) {
                //从推送获取数据,写入sdcard文件
                writeDataToFile(filePath, "完成");
            } else {
                //不存在则创建path文件夹
                JKFile.CreateDir(path);
                writeDataToFile(filePath, "未创建jsondata");
            }
        }
    }

    @Click(R.id.bt_task)
    void task() {
        Intent intent = new Intent(this, LongRunningService.class);
        startService(intent);
    }


    public void writeDataToFile(String filePath, String text) {
        JKFile.WriteFile(filePath, text);
    }


}
