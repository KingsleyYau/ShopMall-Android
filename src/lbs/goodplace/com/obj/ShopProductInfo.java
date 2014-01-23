package lbs.goodplace.com.obj;

import java.io.Serializable;

//商家产品对象
public class ShopProductInfo implements Serializable{
	public  int mProid; //产品ID（整形）
	public  String mProname; //产品名称（字符串）
	public  String mDefaultpic; //defaultpic：产品图标URL（字符串）
}
