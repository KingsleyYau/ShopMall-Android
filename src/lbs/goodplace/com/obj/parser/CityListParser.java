package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CityModule;
import lbs.goodplace.com.obj.ProvinceModule;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class CityListParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				List<ProvinceModule> provincelist = new ArrayList<ProvinceModule>();
//				List<CityModule> citylist = new ArrayList<CityModule>();
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					JSONArray gwArray = json.getJSONArray("cities");
					for (int i = 0; i < gwArray.length(); i++) {
						JSONObject shopJsonObject = (JSONObject) gwArray.get(i);
						CityModule cm = new CityModule();
						cm.setCityareacode(shopJsonObject.getString("cityareacode"));
						cm.setFirstchar(shopJsonObject.getString("firstchar"));
						cm.setHot(shopJsonObject.getInt("ishot") == 1?true:false);
						cm.setId(shopJsonObject.getInt("cityid"));
						cm.setLat(Float.valueOf(shopJsonObject.getString("lat")));
						cm.setLng(Float.valueOf(shopJsonObject.getString("lng")));
						cm.setName(shopJsonObject.getString("cityname"));
						cm.setPromo(shopJsonObject.getInt("ispromo") == 1?true:false);
						cm.setRegion(shopJsonObject.getString("region"));
						cm.setTopcity(shopJsonObject.getInt("topcity") == 1?true:false);
						
						if(provincelist.size() > 0){
							boolean isNewRegion = true;
							for(int j = 0 ; j < provincelist.size(); j++){
								if(provincelist.get(j).getName().trim().equals(cm.getRegion().trim())){
									isNewRegion = false;
									provincelist.get(j).getCityList().add(cm);
								}
							}
							if(isNewRegion){
								ProvinceModule p = new ProvinceModule();
								p.setName(cm.getRegion());
								p.getCityList().add(cm);
								
								provincelist.add(p);
							}
						}else{
							ProvinceModule p = new ProvinceModule();
							p.setName(cm.getRegion());
							p.getCityList().add(cm);
							
							provincelist.add(p);
						}
						
					}
				}
				//解析json
				return provincelist;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
