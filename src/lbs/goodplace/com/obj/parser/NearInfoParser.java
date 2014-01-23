package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CategoryModule;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.InfotypeModule;
import lbs.goodplace.com.obj.NearinfoModule;
import lbs.goodplace.com.obj.RegionModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 行业分类解析类
 * @author lenovo123
 *
 */
public class NearInfoParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				NearinfoModule nim = new NearinfoModule();
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					JSONArray gwArray = json.getJSONArray("categorynavs");
					for (int i = 0; i < gwArray.length(); i++) {
						JSONObject infoypeJsonObject = (JSONObject) gwArray.get(i);
						CategoryModule itModule = new CategoryModule();
						itModule.setId(infoypeJsonObject.getString("categoryid"));
						itModule.setParentid(infoypeJsonObject.getString("parentid"));
						itModule.setName(infoypeJsonObject.getString("categoryname"));
						itModule.setFaviconurl(infoypeJsonObject.getString("categoryfavicon"));
						if(infoypeJsonObject.has("dsfshopcount"))
							itModule.setDsfshopcount(infoypeJsonObject.getString("dsfshopcount"));
						if(infoypeJsonObject.has("hyfshopcount"))
							itModule.setHyfshopcount(infoypeJsonObject.getString("hyfshopcount"));
						
						nim.mCategoriesList.add(itModule);
					}
					
					JSONArray rgArray = json.getJSONArray("regionnavs");
					for (int i = 0; i < rgArray.length(); i++) {
						JSONObject infoypeJsonObject = (JSONObject) rgArray.get(i);
						RegionModule rModule = new RegionModule();
						rModule.setId(infoypeJsonObject.getString("regionid"));
//						rModule.setParentid(infoypeJsonObject.getString("parentid"));
						rModule.setName(infoypeJsonObject.getString("regionname"));
//						rModule.setLat(infoypeJsonObject.getString("lat"));
//						rModule.setLng(infoypeJsonObject.getString("lng"));
						rModule.setDsfshopcount(infoypeJsonObject.getString("dsfshopcount"));
						rModule.setHyfshopcount(infoypeJsonObject.getString("hyfshopcount"));
						nim.mRegionsList.add(rModule);
					}
				}
				//解析json
				return nim;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
