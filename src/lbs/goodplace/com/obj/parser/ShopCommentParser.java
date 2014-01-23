package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.PageInfo;
import lbs.goodplace.com.obj.ShopCommentListInfo;
import lbs.goodplace.com.obj.ShopCommontInfo;
import lbs.goodplace.com.obj.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShopCommentParser implements IParser{
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
				
				ShopCommentListInfo shopCommentListInfo = new ShopCommentListInfo();

				//解析分页
				PageInfo pageInfo = new PageInfo();
				pageInfo.mCurpag = json.optInt("curpage");
				pageInfo.mPagecount = json.optInt("pagecount");
				pageInfo.mPagemaxrow = json.optInt("pagemaxrow");
				pageInfo.mTotalrecordcount = json.optInt("totalrecordcount");
				shopCommentListInfo.mPageInfo = pageInfo;
				
				
				ArrayList<ShopCommontInfo> shoCommontList = new ArrayList<ShopCommontInfo>();
				//解析评论
				JSONArray shopCommentJsonArray = json.getJSONArray("reviews");
				int size = shopCommentJsonArray.length();
				for (int i = 0; i < size; i++) {
					ShopCommontInfo shopCommontInfo = new ShopCommontInfo();
					JSONObject shopCommentJSONObject = (JSONObject) shopCommentJsonArray.get(i);
					
					shopCommontInfo.mReviewid = shopCommentJSONObject.optInt("reviewid");
					shopCommontInfo.mShopid = shopCommentJSONObject.optInt("shopid");
					shopCommontInfo.mShopname = shopCommentJSONObject.optString("shopname");
					shopCommontInfo.mShopbranchname = shopCommentJSONObject.optString("shopbranchname");

					UserInfo userInfo = new UserInfo();
					userInfo.mUserid = shopCommentJSONObject.optString("userid");
					userInfo.mUsernickname = shopCommentJSONObject.optString("usernickname");
					userInfo.mUserface = shopCommentJSONObject.optString("userface");
					shopCommontInfo.mUserInfo = userInfo;
					
					shopCommontInfo.mPosttime = shopCommentJSONObject.optLong("posttime");
					shopCommontInfo.mScore = shopCommentJSONObject.optInt("score");
					shopCommontInfo.mScore1 = shopCommentJSONObject.optInt("score1");
					shopCommontInfo.mScore2 = shopCommentJSONObject.optInt("score2");
					shopCommontInfo.mScore3 = shopCommentJSONObject.optInt("score3");
					shopCommontInfo.mScore4 = shopCommentJSONObject.optInt("score4");
					shopCommontInfo.mReviewbody = shopCommentJSONObject.optString("reviewbody");
					shoCommontList.add(shopCommontInfo);
				}
				
				shopCommentListInfo.mShoCommontList = shoCommontList;
				return shopCommentListInfo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}