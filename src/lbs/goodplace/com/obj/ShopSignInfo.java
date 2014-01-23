package lbs.goodplace.com.obj;

/**
 * 商家留言签到对象
 * 
 * @author shazhuzhu
 * 
 */
public class ShopSignInfo {
	public UserInfo mUserInfo; //用户基本信息
	public int mSignid; // 签到ID（整形）
	public int mShopid; // 商家ID（整形）
	public String mShopname; // 商家名称（字符串）
	public String mShopbranchname; // 分店名称（字符串）

	public long mSigntime;// 发布时间（自1970年1月1日起的秒数）（整形）
	public double mLat; //经度
	public double mLng; //维度
	public int mScore;// 总体评分（整形）
	public String mSignbody;// 点评内容（字符串）
	public String mAttachedimg;// 附件图片URL（字符串）
}
