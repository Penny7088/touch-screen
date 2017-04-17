package com.jkframework.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class JKCutPictureData implements Parcelable{
	
	/**缩放比例X*/
	public int nScaleX;
	/**缩放比例Y*/
	public int nScaleY;
	/**期望图片宽度(不期望可填-1)*/
	public int nTargetWidth = -1;
	/**期望图片高度(不期望可填-1)*/
	public int nTargetHeight = -1;
	
	public JKCutPictureData() {
	
	}
	
	public JKCutPictureData(Parcel in) {
		nScaleX = in.readInt();
		nScaleY = in.readInt();
		nTargetWidth = in.readInt();
		nTargetHeight = in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(nScaleX);
		out.writeInt(nScaleY);
		out.writeInt(nTargetWidth);
		out.writeInt(nTargetHeight);
	}
	
	public static final Parcelable.Creator<JKCutPictureData> CREATOR = new Parcelable.Creator<JKCutPictureData>() {
		public JKCutPictureData createFromParcel(Parcel in) {
			return new JKCutPictureData(in);
		}

		public JKCutPictureData[] newArray(int size) {
			return new JKCutPictureData[size];
		}
	};
	
	
}