package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.PageInfo;
import lbs.goodplace.com.obj.ShopSignInfo;
import lbs.goodplace.com.obj.ShopSignListInfo;
import lbs.goodplace.com.obj.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 签到留言解析类
 * @author shazhuzhu
 *
 */
public class ShopSignParser implements IParser{
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
				
				ShopSignListInfo shopSignListInfo = new ShopSignListInfo();

				//解析分页
				PageInfo pageInfo = new PageInfo();
				pageInfo.mCurpag = json.optInt("curpage");
				pageInfo.mPagecount = json.optInt("pagecount");
				pageInfo.mPagemaxrow = json.optInt("pagemaxrow");
				pageInfo.mTotalrecordcount = json.optInt("totalrecordcount");
				shopSignListInfo.mPageInfo = pageInfo;
				
				
				ArrayList<ShopSignInfo> shoSignList = new ArrayList<ShopSignInfo>();
				//解析评论
				JSONArray shopSignJsonArray = json.getJSONArray("signins");
				int size = shopSignJsonArray.length();
				for (int i = 0; i < size; i++) {
					ShopSignInfo shopSignInfo = new ShopSignInfo();
					JSONObject shopSignJSONObject = (JSONObject) shopSignJsonArray.get(i);
					
					shopSignInfo.mSignid = shopSignJSONObject.optInt("signid");
					shopSignInfo.mShopid = shopSignJSONObject.optInt("shopid");
					shopSignInfo.mShopname = shopSignJSONObject.optString("shopname");
					shopSignInfo.mShopbranchname = shopSignJSONObject.optString("shopbranchname");
					
					UserInfo userInfo = new UserInfo();
					userInfo.mUserid = shopSignJSONObject.optString("userid");
					userInfo.mUsernickname = shopSignJSONObject.optString("usernickname");
					userInfo.mUserface = shopSignJSONObject.optString("userface");
					shopSignInfo.mUserInfo = userInfo;
					
					shopSignInfo.mSigntime = shopSignJSONObject.optLong("signtime");
					
					shopSignInfo.mLat = shopSignJSONObject.optDouble("lat");
					shopSignInfo.mLng = shopSignJSONObject.optDouble("lng");
					shopSignInfo.mScore = shopSignJSONObject.optInt("score");

					shopSignInfo.mSignbody = shopSignJSONObject.optString("signbody");
					shopSignInfo.mAttachedimg = shopSignJSONObject.optString("attachedimg");
					shoSignList.add(shopSignInfo);
				}

				shopSignListInfo.mShopSignInfoList = shoSignList;
				return shopSignListInfo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}