package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.InfotypeModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 资讯类型解析类
 * @author lenovo123
 *
 */
public class InfotypesParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				List<InfotypeModule> infotypelist = new ArrayList<InfotypeModule>();
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					
					JSONArray gwArray = json.getJSONArray("infotypes");
					for (int i = 0; i < gwArray.length(); i++) {
						JSONObject infoypeJsonObject = (JSONObject) gwArray.get(i);
						InfotypeModule itModule = new InfotypeModule();
						itModule.setId(infoypeJsonObject.getString("infotypeid"));
						itModule.setName(infoypeJsonObject.getString("infotypename"));
						itModule.setIconurl(infoypeJsonObject.getString("infotypeicon"));
						
						infotypelist.add(itModule);
					}
				}
				//解析json
				return infotypelist;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
