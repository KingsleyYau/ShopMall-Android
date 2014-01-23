package lbs.goodplace.com.obj;

/**
 * 商家评论
 * 
 * @author shazhuzhu
 * 
 */
public class ShopCommontInfo {
	public UserInfo mUserInfo; //用户基本信息
	public int mReviewid; // 评论ID（整形）
	public int mShopid; // 商家ID（整形）
	public String mShopname; // 商家名称（字符串）
	public String mShopbranchname; // 分店名称（字符串）

	public long mPosttime;// 发布时间（自1970年1月1日起的秒数）（整形）
	public int mScore;// 总体评分（整形）
	public int mScore1;// 产品评分（整形）
	public int mScore2;// 氛围评分（整形）
	public int mScore3;// 客服评分（整形）
	public int mScore4;// 其它评分（整形）
	public String mReviewbody;// 点评内容（字符串）
	public boolean mIsShowAllBody; //是否显示所有点评内容
}
