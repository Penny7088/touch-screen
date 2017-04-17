package com.yysp.ecandroid.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2016/7/5.
 */
public class ECLoginModel implements Parcelable {

    public String tAccount;
    public String tPassword;
    public String tResult;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tAccount);
        dest.writeString(this.tPassword);
        dest.writeString(this.tResult);
    }

    public ECLoginModel() {
    }

    protected ECLoginModel(Parcel in) {
        this.tAccount = in.readString();
        this.tPassword = in.readString();
        this.tResult = in.readString();
    }

    public static final Parcelable.Creator<ECLoginModel> CREATOR = new Parcelable.Creator<ECLoginModel>() {
        @Override
        public ECLoginModel createFromParcel(Parcel source) {
            return new ECLoginModel(source);
        }

        @Override
        public ECLoginModel[] newArray(int size) {
            return new ECLoginModel[size];
        }
    };
}
