package lbs.goodplace.com.obj.parser;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CityModule;

import org.json.JSONObject;

public class CityParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				CityModule cm = new CityModule();
				
//				JSONObject opret = json.getJSONObject("opret");
//				int opflag = opret.getInt("opflag");
//				if (opflag == 1) {
					JSONObject shopJsonObject = json.getJSONObject("owncity");
						cm.setCityareacode(shopJsonObject.getString("cityareacode"));
						cm.setFirstchar(shopJsonObject.getString("firstchar"));
						cm.setHot(shopJsonObject.getInt("ishot") == 1?true:false);
						cm.setId(shopJsonObject.getInt("cityid"));
						cm.setLat(shopJsonObject.getDouble("lat"));
						cm.setLng(shopJsonObject.getDouble("lng"));
						cm.setName(shopJsonObject.getString("cityname"));
						cm.setPromo(shopJsonObject.getInt("ispromo") == 1?true:false);
						cm.setRegion(shopJsonObject.getString("region"));
						cm.setTopcity(shopJsonObject.getInt("topcity") == 1?true:false);
						
						
//				}
				//解析json
				return cm;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
