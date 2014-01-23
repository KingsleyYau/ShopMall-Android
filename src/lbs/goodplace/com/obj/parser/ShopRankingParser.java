package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.ShopRankingChildInfo;
import lbs.goodplace.com.obj.ShopRankingInfo;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * <br>类描述:商家排行榜 解析类
 * <br>功能详细描述:(2.3.1.8 )
 * 
 * @author  licanhui
 * @date  [2013-4-13]
 */
public class ShopRankingParser implements IParser{
	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				//判断是成功
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag != 1) {
					return null;
				}
				
				
				ArrayList<ShopRankingInfo> shopRankingList = new ArrayList<ShopRankingInfo>();
				
				//解析排行版总分类
				JSONArray catranksJsonArray = json.getJSONArray("catranks");
				int catranksSize = catranksJsonArray.length();
				for (int i = 0; i < catranksSize; i++) {
					ShopRankingInfo rankingInfo = new ShopRankingInfo();
					JSONObject rankJSONObject = (JSONObject) catranksJsonArray.get(i);
					rankingInfo.mId = rankJSONObject.optInt("categoryid");
					rankingInfo.mParentid = rankJSONObject.optInt("parentid");
					rankingInfo.mName = rankJSONObject.optString("categoryname");
					rankingInfo.mIconUrl = rankJSONObject.optString("categoryfavicon");
					
					//解析包含的子类
					ArrayList<ShopRankingChildInfo> childList = new ArrayList<ShopRankingChildInfo>();
					JSONArray childJsonArray = rankJSONObject.getJSONArray("ranks");
					int ranksSize = childJsonArray.length();
					for (int j = 0; j < ranksSize; j++) {
						ShopRankingChildInfo childInfo = new ShopRankingChildInfo();
						JSONObject childJSONObject = (JSONObject) childJsonArray.get(j);
						childInfo.mId = childJSONObject.optInt("catrankid");
						childInfo.mName = childJSONObject.optString("catrankname");
						childInfo.mIconUrl = childJSONObject.optString("catrankfavicon");
						childList.add(childInfo);
					}
					rankingInfo.mChildList = childList;
					
					shopRankingList.add(rankingInfo);	
				}
				return shopRankingList;
				
//				return parseRankListData(shopRankingList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public ArrayList<ShopRankingChildInfo>  parseRankListData(ArrayList<ShopRankingInfo> shopRankingList){
		ArrayList<ShopRankingChildInfo> mShopRankingChildList = new ArrayList<ShopRankingChildInfo>();
		
		int shopRankingListSize = shopRankingList.size();
		for (int i = 0; i < shopRankingListSize; i++) {
			ShopRankingInfo rankingInfo = shopRankingList.get(i);
			
			ShopRankingChildInfo fartherInfo = new ShopRankingChildInfo();
			fartherInfo.mIsFarter = true;
			fartherInfo.mId = rankingInfo.mId;
			fartherInfo.mName = rankingInfo.mName;
			mShopRankingChildList.add(fartherInfo); //添加父类标题
			mShopRankingChildList.addAll(rankingInfo.mChildList); //添加子类
		}
		
		return mShopRankingChildList;
	}
	
}