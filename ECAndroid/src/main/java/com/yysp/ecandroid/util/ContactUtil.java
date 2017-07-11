package com.yysp.ecandroid.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


/**
 * Created by Administrator on 2017/4/18.
 */

public class ContactUtil {
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


}

