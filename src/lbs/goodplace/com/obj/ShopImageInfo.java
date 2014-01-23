package lbs.goodplace.com.obj;


import android.os.Parcel;
import android.os.Parcelable;

public class ShopImageInfo implements Parcelable{
	public int mImgid; // 图片ID（整形）
	public String mImgname; // 图片名称（字符串）
	public String mImgtype; // 图片类型（字符串，”产品”、”氛围”、”其它”三种类型）
	public String mThumburl; // 拇指图片URL（字符串）
	public String mFullurl; // 原始图片的URL（字符串）
	public String mUploaduser; // 用户（字符串）
	public long mUploadtime; // 时间（自1970年1月1日起的秒数）（整形）
	public int mStar; // 总体评价（整形）
	public int mPrice; // 价格（整形，可能为空，空则客户端无需显示）
	public String mTag; // 标签（字符串）
	public int mIsProgressBar = 0; //是否最后一个等待界面
	
	public ShopImageInfo() {
	}
	
	
	private ShopImageInfo(Parcel in) {
		mImgid = in.readInt();
		mImgname = in.readString();
		mImgtype = in.readString();
		mThumburl = in.readString();
		mFullurl = in.readString();
		mUploaduser = in.readString();
		mUploadtime = in.readLong();
		mStar = in.readInt();
		mPrice = in.readInt();
		mTag = in.readString();
		mIsProgressBar = in.readInt();
	}
	
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mImgid);
		dest.writeString(mImgname);
		dest.writeString(mImgtype);
		dest.writeString(mThumburl);
		dest.writeString(mFullurl);
		dest.writeString(mUploaduser);
		dest.writeLong(mUploadtime);
		dest.writeInt(mStar);
		dest.writeInt(mPrice);
		dest.writeString(mTag);
		dest.writeInt(mIsProgressBar);
	}
	
	
	public static final Parcelable.Creator<ShopImageInfo> CREATOR = new Parcelable.Creator<ShopImageInfo>() {
		public ShopImageInfo createFromParcel(Parcel in) {
			return new ShopImageInfo(in);
		}

		public ShopImageInfo[] newArray(int size) {
			return new ShopImageInfo[size];
		}
	};
}
