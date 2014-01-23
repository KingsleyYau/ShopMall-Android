package lbs.goodplace.com.obj;

import java.util.ArrayList;

/**
 * 商家排行榜-父类
 * (2.3.1.8 )
 * 
 * @author  licanhui
 * @date  [2013-4-13]
 */
public class ShopRankingInfo {
	public ArrayList<ShopRankingChildInfo> mChildList = new ArrayList<ShopRankingChildInfo>();
	public int mId; //id
	public int mParentid; //父id
	public String mName; //名称
	public String mIconUrl; //图标url
	public boolean mIsCheck;
}
