package lbs.goodplace.com.obj.parser;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CityModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.SessionModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 签到解析类
 * @author lenovo123
 *
 */
public class SessionParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				SessionModule mSessionModule = null;
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					mSessionModule = new SessionModule();
					mSessionModule.setSessionkey(json.getString("sessionkey"));
					
					JSONObject cityjson = json.getJSONObject("owncity");
					CityModule city = new CityModule();
					city.setRegion(cityjson.getString("region"));
					city.setFirstchar(cityjson.getString("firstchar"));
					city.setTopcity(cityjson.getInt("topcity") == 1?true:false);
					city.setPromo(cityjson.getInt("ispromo") == 1?true:false);
					city.setCityareacode(cityjson.getString("cityareacode"));
					city.setLng(Float.valueOf(cityjson.getString("lng")));
					city.setLat(Float.valueOf(cityjson.getString("lat")));
					city.setName(cityjson.getString("cityname"));
					city.setHot(cityjson.getInt("ishot") == 1?true:false);
					city.setId(cityjson.getInt("cityid"));
					
					mSessionModule.setCity(city);
				}
				//解析json
				return mSessionModule;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
