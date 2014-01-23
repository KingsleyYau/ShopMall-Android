package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.PageInfo;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 商家排行榜类型解析类
 * @author shazhuzhu
 *
 */
public class ShopListParser implements IParser {

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
				
				ShopListModule shopListModule = new ShopListModule();
				
				//解析分页
				PageInfo pageInfo = new PageInfo();
				pageInfo.mCurpag = json.optInt("curpage");
				pageInfo.mPagecount = json.optInt("pagecount");
				pageInfo.mPagemaxrow = json.optInt("pagemaxrow");
				pageInfo.mTotalrecordcount = json.optInt("totalrecordcount");
				shopListModule.mPageInfo = pageInfo;
				
				
				ArrayList<ShopModule> shopList = new ArrayList<ShopModule>();
				//解析内容
				JSONArray shopArray = json.getJSONArray("shops");
				for (int i = 0; i < shopArray.length(); i++) {
					JSONObject shopJsonObject = (JSONObject) shopArray.get(i);
					ShopModule shopModule = new ShopModule();
					shopModule.id = shopJsonObject.optInt("shopid");
					shopModule.name = shopJsonObject.optString("shopname");
					shopModule.ename = shopJsonObject.optString("ename");
					
					shopModule.branchname = shopJsonObject.optString("branchname");
					shopModule.adddate = shopJsonObject.optInt("adddate");
					shopModule.lastdate = shopJsonObject.optInt("lastdate");
					
					shopModule.lat = shopJsonObject.optDouble("lat");
					shopModule.lng = shopJsonObject.optDouble("lng");
				
					shopModule.address = shopJsonObject.optString("address");
					
					if(shopJsonObject.optInt("ifdiscount") == 1){
						shopModule.isdiscount = true;
					}
					
					if(shopJsonObject.optInt("ifpromo") == 1){
						shopModule.ispromo = true;
					}
					
					if(shopJsonObject.optInt("ifgift") == 1){
						shopModule.isgift = true;
					}
					
					if(shopJsonObject.optInt("ifcard") == 1){
						shopModule.iscard = true;
					}
					
					if(shopJsonObject.optInt("ifhyf") == 1){
						shopModule.ishyf = true;
					}
					
					if(shopJsonObject.optInt("ifdsf") == 1){
						shopModule.isdsf = true;
					}
					
					
					shopModule.categoryid = shopJsonObject.optInt("categoryid");
					shopModule.categoryname = shopJsonObject.optString("categoryname");
					
					shopModule.cityid = shopJsonObject.optInt("cityid");
					
					shopModule.defaultpicurl = shopJsonObject.optString("defaultpic");
					
					shopModule.avgprice = shopJsonObject.optString("avgprice");
					
					shopModule.pricetext = shopJsonObject.optString("pricetext");

					shopModule.regionid = shopJsonObject.optInt("regionid");
					shopModule.regionname = shopJsonObject.optString("regionname");
				
					shopModule.score = shopJsonObject.optInt("score");
					shopModule.score1 = shopJsonObject.optInt("score1");
					shopModule.score2 = shopJsonObject.optInt("score2");
					shopModule.score3 = shopJsonObject.optInt("score3");
					shopModule.score4 = shopJsonObject.optInt("score4");
					shopModule.scoretext = shopJsonObject.optString("scoretext");
					
					
					shopModule.writeup = shopJsonObject.optString("writeup");
					shopModule.phoneno = shopJsonObject.optString("phoneno");
					shopModule.phoneno2 = shopJsonObject.optString("phoneno2");
					
					shopList.add(shopModule);
				}
				
				shopListModule.mShopList = shopList;
					
				return shopListModule;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
