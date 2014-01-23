package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.InfoListModule;
import lbs.goodplace.com.obj.InfoModule;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 资讯列表解析类
 * 
 * @author lenovo123
 * 
 */
public class infoDetailParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				InfoModule itModule = new InfoModule();

				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {

					JSONObject infojson = json.getJSONObject("infodetail");
					itModule.setId(infojson.getString("infoid"));
					itModule.setTypeid(infojson.getString("infotypeid"));
					itModule.setTypename(infojson.getString("infotypename"));
					itModule.setBegindate(infojson.getLong("begindate"));
					itModule.setEnddate(infojson.getLong("enddate"));
					itModule.setInfobegindate(infojson.getLong("infobegindate"));
					itModule.setInfoenddate(infojson.getLong("infoenddate"));
					itModule.setTitle(infojson.getString("infotitle"));
					itModule.setDesc(infojson.getString("infodesc"));
					itModule.setDefaultpicurl(infojson.getString("defaultpic"));
					itModule.setShopid(infojson.getString("shopid"));
					itModule.setShopname(infojson.getString("shopname"));
					itModule.setHassms(infojson.getInt("hassms")==1?true:false);
					itModule.setSmsinfo(infojson.getString("smsinfo"));
					itModule.setTips(infojson.getString("gettips"));
					itModule.setShowtips(infojson.getString("showtips"));
					itModule.setGettype(infojson.getString("gettype"));
					itModule.setShowtype(infojson.getString("showtype"));
					if(infojson.has("buytips"))
						itModule.setBuytips(infojson.getString("buytips"));
					
//					JSONArray gwArray = json.getJSONArray("branchlist");
//					for (int i = 0; i < gwArray.length(); i++) {
//						JSONObject infoypeJsonObject = (JSONObject) gwArray.get(i);
//						InfoModule branchModule = new InfoModule();
//						branchModule.setShopid(infoypeJsonObject.getString("branchshopid"));
//						branchModule.setShopname(infoypeJsonObject.getString("branchname"));
//						
//						itModule.mBranchlist.add(branchModule);
//					}
				}else{
					itModule.setResult(false);
					itModule.setErrorcode(opret.getInt("code"));
				}
				// 解析json
				return itModule;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
