package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.Commentsituation;
import lbs.goodplace.com.obj.Recommendtags;
import lbs.goodplace.com.obj.ShopInfosituationInfo;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.ShopOtherbranchsInfo;
import lbs.goodplace.com.obj.Signsituation;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 商家排行榜类型解析类
 * 
 * @author shazhuzhu
 * 
 */
public class ShopInfoParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				// 判断是成功
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag != 1) {
					return null;
				}

				// 解析内容
				JSONObject shopJsonObject = (JSONObject) json.getJSONObject("shopinfo");
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

				if (shopJsonObject.optInt("ifdiscount") == 1) {
					shopModule.isdiscount = true;
				}

				if (shopJsonObject.optInt("ifpromo") == 1) {
					shopModule.ispromo = true;
				}

				if (shopJsonObject.optInt("ifgift") == 1) {
					shopModule.isgift = true;
				}

				if (shopJsonObject.optInt("ifcard") == 1) {
					shopModule.iscard = true;
				}

				if (shopJsonObject.optInt("ifhyf") == 1) {
					shopModule.ishyf = true;
				}

				if (shopJsonObject.optInt("ifdsf") == 1) {
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
				shopModule.trafficinfo = shopJsonObject.optString("trafficinfo"); //交通信息
				
				//商家发布资讯的情况
				ShopInfosituationInfo infosituation = new ShopInfosituationInfo();
				JSONObject infosituationJsonObject = shopJsonObject.optJSONObject("infosituation");
				infosituation.infototal = infosituationJsonObject.optInt("infototal");
				infosituation.curinfotitle = infosituationJsonObject.optString("curinfotitle");
				shopModule.infosituation = infosituation;
				
				
				//解析推荐
				JSONArray recommendJsonArray = shopJsonObject.getJSONArray("recommendtags");
				ArrayList<Recommendtags> recommendList = new ArrayList<Recommendtags>();
				int recommendSize = recommendJsonArray.length();
				for (int j = 0; j < recommendSize; j++) {
					JSONObject recommendJsonObject = (JSONObject) recommendJsonArray.get(j);
					Recommendtags recommendtags = new Recommendtags();
					recommendtags.tag = recommendJsonObject.optString("recommendtag");
					recommendtags.id = recommendJsonObject.optInt("recommendid");
					recommendList.add(recommendtags);
				}
				shopModule.recommendtags = recommendList;
				
				
				//解析评论
				JSONObject commentJsonObject = shopJsonObject.getJSONObject("commentsituation");
				Commentsituation commentsituation = new Commentsituation(); //评论
				commentsituation.total = commentJsonObject.optInt("commenttotal");
				commentsituation.curuser = commentJsonObject.optString("curuser");
				commentsituation.star = commentJsonObject.optInt("curcommentstar");
				commentsituation.curcomment = commentJsonObject.optString("curcomment");
				commentsituation.time = commentJsonObject.optLong("curcommenttime");
				shopModule.commentsituation = commentsituation;
				
				//解析签到
				JSONObject signsJsonObject = shopJsonObject.getJSONObject("signsituation");
				Signsituation signsituation = new Signsituation();
				signsituation.total = signsJsonObject.optInt("signtotal");
				signsituation.curuser = signsJsonObject.optString("curuser");
				signsituation.detail = signsJsonObject.optString("cursigndetail");
				signsituation.time = signsJsonObject.optLong("cursigntime");
				shopModule.signsituation = signsituation;
				
				//解析其它分店
				JSONArray otherbranchsJsonArray = shopJsonObject.getJSONArray("otherbranchs");
				ArrayList<ShopOtherbranchsInfo> otherbranchsList = new ArrayList<ShopOtherbranchsInfo>();
				int otherbranchsSize = otherbranchsJsonArray.length();
				for (int j = 0; j < otherbranchsSize; j++) {
					JSONObject otherbranchsJsonObject = (JSONObject) otherbranchsJsonArray.get(j);
					ShopOtherbranchsInfo otherbranchsInfo = new ShopOtherbranchsInfo();
					otherbranchsInfo.shopid = otherbranchsJsonObject.optInt("shopid");
					otherbranchsInfo.branchname = otherbranchsJsonObject.optString("branchname");
					otherbranchsList.add(otherbranchsInfo);
				}
				shopModule.otherbranchsList = otherbranchsList;
				
				
				return shopModule;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
