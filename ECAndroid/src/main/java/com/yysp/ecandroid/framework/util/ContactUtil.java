package com.yysp.ecandroid.framework.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.AddErrorMsgResponse;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.framework.net.BaseSubscriber;
import com.yysp.ecandroid.framework.net.ECRequest;
import com.yysp.ecandroid.service.HelpService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/4/18.
 */

public class ContactUtil {

    public static int noReadCount;

    public final static String TAG = "RT";

    public static HelpService mHelpServic = null;

    //任务是否进行开关
    public static boolean isTasking = false;
    public static String ActivityName = "";
    public static int taskType;
    public static String TaskId = "";
    public static DisGetTaskBean TaskBean;

    //单个添加好友
    public static Boolean addContact(Context context, String phoneNumber) {
        ContentValues values = new ContentValues();
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        if (rawContactUri != null) {
            long rawContactId = ContentUris.parseId(rawContactUri);
            values.clear();


            values.clear();
            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE); //内容类型
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, phoneNumber);
            context.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);

            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            // 联系人的电话号码
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
            // 电话类型
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            // 向联系人电话号码URI添加电话号码
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            values.clear();
            return true;
        } else {
            return false;
        }

    }


    public static void deleteContact(Context context, String phoneNumber) throws Exception {
        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts.Data._ID}, "display_name=?", new String[]{phoneNumber}, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{phoneNumber});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
        }
    }

    /**
     * 清空系统通信录数据
     */
    public static void clearContact(Context context) {
        ContentResolver cr = context.getContentResolver();
        if (cr != null) {
            // 查询contacts表的所有记录
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                    null, null, null);
            // 如果记录不为空
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    // 游标初始指向查询结果的第一条记录的上方，执行moveToNext函数会判断
                    // 下一条记录是否存在，如果存在，指向下一条记录。否则，返回false。
                    while (cursor.moveToNext()) {


                        String name = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                        //根据姓名求id
                        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
                        if (uri != null && name != null) {
                            if (!name.equals("")) {
                                Cursor cursor1 = cr.query(uri, new String[]{ContactsContract.Contacts.Data._ID}, "display_name=?", new String[]{name}, null);
                                if (cursor1 != null) {
                                    if (cursor1.moveToFirst()) {
                                        int id = cursor1.getInt(0);
                                        //根据id删除data中的相应数据
                                        cr.delete(uri, "display_name=?", new String[]{name});
                                        uri = Uri.parse("content://com.android.contacts/data");
                                        cr.delete(uri, "raw_contact_id=?", new String[]{id + ""});
                                    }
                                    cursor1.close();
                                }
                            }
                        }
                    }
                }
                cursor.close();
            }

        }
    }

    /*
    通过服务端发送的json将手机号加入通讯录
     */
    public static void AddToContact(Context context, String custom) {
        Gson gson = new Gson();
        List<DisGetTaskBean.DataBean.TargetAccountsBean> list = gson.fromJson(custom, DisGetTaskBean.DataBean.class).getTargetAccounts();
        List<String> phoneList = new ArrayList<>();
        Logger.i(TAG, "phone_size:" + list.size());
        for (int i = 0; i < list.size(); i++) {
            if (ContactUtil.addContact(context, list.get(i).getMobile())) {
                Logger.i(TAG, "phone:" + list.get(i).getMobile());
                phoneList.add(list.get(i).getMobile());
            }

        }
//        JKPreferences.SaveSharePersistent("phoneList", (ArrayList<String>) phoneList);
    }

    public static void doOfTaskEnd(ECTaskResultResponse resultResponse, Context context) {
        ECRequest.taskStatus(resultResponse, context)
                .subscribe(new BaseSubscriber<DisBean>() {
                    @Override
                    public void onNext(DisBean pDisBean) {
                        PerformClickUtils.performHome(mHelpServic);//任务完成进入home
                        isTasking = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        PerformClickUtils.performHome(mHelpServic);//任务完成进入home
                        isTasking = false;
                        AddErrorMsgUtil("taskStatus" + e.getMessage());
                    }
                });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void AddErrorMsgUtil(String msg) {
        AddErrorMsgResponse errorMsgResponse = new AddErrorMsgResponse(msg);
//        ECRequest.addErrorMsg(errorMsgResponse).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(DisBean disBean) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }
}

