package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CategoryModule;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.InfotypeModule;
import lbs.goodplace.com.obj.RegionModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 商圈信息解析类
 * @author lenovo123
 *
 */
public class RegionsParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				List<RegionModule> regionslist = new ArrayList<RegionModule>();
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					
					JSONArray gwArray = json.getJSONArray("regions");
					for (int i = 0; i < gwArray.length(); i++) {
						JSONObject infoypeJsonObject = (JSONObject) gwArray.get(i);
						RegionModule rModule = new RegionModule();
						rModule.setId(infoypeJsonObject.getString("regionid"));
						rModule.setParentid(infoypeJsonObject.getString("parentid"));
						rModule.setName(infoypeJsonObject.getString("regionname"));
						rModule.setLat(infoypeJsonObject.getString("lat"));
						rModule.setLng(infoypeJsonObject.getString("lng"));
						regionslist.add(rModule);
					}
				}
				//解析json
				return regionslist;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
