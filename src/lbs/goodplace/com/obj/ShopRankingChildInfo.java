package lbs.goodplace.com.obj;


/**
 * 商家排行榜-子类对象
 * (2.3.1.8 )
 * 
 * @author  licanhui
 * @date  [2013-4-13]
 */
public class ShopRankingChildInfo {
	public int mId; //排行榜ID（整形）
	public int mParentid; //父id
	public String mName; //排行榜名称（字符串类型）
	public String mIconUrl; //排行榜的图标URL地址（字符串类型）
	
	public boolean mIsFarter; //是否父亲类
}
