package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.PageInfo;
import lbs.goodplace.com.obj.ShopProductInfo;
import lbs.goodplace.com.obj.ShopProductListInfo;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 商家产品解析类
 * @author shazhuzhu
 *
 */
public class ShopProductListParser implements IParser{
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
				
				ShopProductListInfo shopProductListInfo = new ShopProductListInfo();

				//解析分页
				PageInfo pageInfo = new PageInfo();
				pageInfo.mCurpag = json.optInt("curpage");
				pageInfo.mPagecount = json.optInt("pagecount");
				pageInfo.mPagemaxrow = json.optInt("pagemaxrow");
				pageInfo.mTotalrecordcount = json.optInt("totalrecordcount");
				shopProductListInfo.mPageInfo = pageInfo;
				
				ArrayList<ShopProductInfo> shopProductList = new ArrayList<ShopProductInfo>();

				//解析评论
				JSONArray shopProductListJsonArray = json.getJSONArray("products");
				int size = shopProductListJsonArray.length();
				for (int i = 0; i < size; i++) {
					ShopProductInfo shopProductInfo = new ShopProductInfo();
					JSONObject shopProductJSONObject = (JSONObject) shopProductListJsonArray.get(i);
					shopProductInfo.mProid = shopProductJSONObject.optInt("proid");
					shopProductInfo.mProname = shopProductJSONObject.optString("proname");
					shopProductInfo.mDefaultpic = shopProductJSONObject.optString("defaultpic");
					shopProductList.add(shopProductInfo);
				}
				shopProductListInfo.mShopProductList = shopProductList;
				
				return shopProductListInfo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}