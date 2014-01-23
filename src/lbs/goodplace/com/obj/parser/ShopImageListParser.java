package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.PageInfo;
import lbs.goodplace.com.obj.ShopImageInfo;
import lbs.goodplace.com.obj.ShopImageListInfo;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 商家排行榜类型解析类
 * @author shazhuzhu
 *
 */
public class ShopImageListParser implements IParser {

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
				
				ShopImageListInfo shopImageListInfo = new ShopImageListInfo();
				
				//解析分页
				PageInfo pageInfo = new PageInfo();
				pageInfo.mCurpag = json.optInt("curpage");
				pageInfo.mPagecount = json.optInt("pagecount");
				pageInfo.mPagemaxrow = json.optInt("pagemaxrow");
				pageInfo.mTotalrecordcount = json.optInt("totalrecordcount");
				shopImageListInfo.mPageInfo = pageInfo;
				
				
				ArrayList<ShopImageInfo> shopImageList = new ArrayList<ShopImageInfo>();
				//解析内容
				JSONArray shopimgsArray = json.getJSONArray("shopimgs");
				for (int i = 0; i < shopimgsArray.length(); i++) {
					JSONObject shopimgsObject = (JSONObject) shopimgsArray.get(i);
					ShopImageInfo shopImageInfo = new ShopImageInfo();
					shopImageInfo.mImgid = shopimgsObject.optInt("imgid");
					shopImageInfo.mImgname = shopimgsObject.optString("imgname");
					shopImageInfo.mImgtype = shopimgsObject.optString("imgtype");
					shopImageInfo.mThumburl = shopimgsObject.optString("thumburl");
					shopImageInfo.mFullurl = shopimgsObject.optString("fullurl");
					shopImageInfo.mUploaduser = shopimgsObject.optString("uploaduser");
					shopImageInfo.mUploadtime = shopimgsObject.optLong("uploadtime");
					shopImageInfo.mStar = shopimgsObject.optInt("star");
					shopImageInfo.mPrice = shopimgsObject.optInt("price");
					shopImageInfo.mTag = shopimgsObject.optString("tag");
					
					shopImageList.add(shopImageInfo);
				}
				shopImageListInfo.mShopImageList = shopImageList;
				
				return shopImageListInfo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
