package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.InfoListModule;
import lbs.goodplace.com.obj.InfoModule;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 2.3.7.7 我的卷卷详细
 * 
 * @author lenovo123
 * 
 */
public class MyinfoDetailParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				InfoModule itModule = new InfoModule();

				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {

					JSONObject infojson = json.getJSONObject("myinfo");
					if(infojson.has("buytips"))
						itModule.setBuytips(infojson.getString("buytips"));
					itModule.setCanceltime(infojson.getLong("canceltime"));
					itModule.setGettime(infojson.getLong("gettime"));
					itModule.setId(infojson.getString("infoid"));
					itModule.setMemberid(infojson.getString("memberid"));
					itModule.setStatus(infojson.getInt("status"));
					itModule.setInfoenddate(infojson.getLong("infoenddate"));
					itModule.setTitle(infojson.getString("infotitle"));
					itModule.setDefaultpicurl(infojson.getString("infophoto"));
					itModule.setShowtips(infojson.getString("showtips"));
					itModule.setShopname(infojson.getString("shopname"));
					itModule.setShowtype(infojson.getString("showtype"));
					itModule.setGetprice(infojson.getString("getprice"));
					itModule.setShopid(infojson.getString("shopid"));
					itModule.setInfobegindate(infojson.getLong("infobegindate"));
					itModule.setDesc(infojson.getString("infodesc"));
					itModule.setUsetime(infojson.getLong("usetime"));
					itModule.setMyinfoid(infojson.getString("myinfoid"));
					itModule.setGettype(infojson.getString("gettype"));
					itModule.setBarimgurl(infojson.getString("barimgurl"));
					itModule.setVericode(infojson.getString("vericode"));
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
