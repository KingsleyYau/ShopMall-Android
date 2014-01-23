package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 积分类型解析类
 * @author lenovo123
 *
 */
public class CredittypesParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				List<CredittypeModule> credittypelist = new ArrayList<CredittypeModule>();
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					
					JSONArray gwArray = json.getJSONArray("credittypes");
					for (int i = 0; i < gwArray.length(); i++) {
						JSONObject shopJsonObject = (JSONObject) gwArray.get(i);
						CredittypeModule ctModule = new CredittypeModule();
						ctModule.setName(shopJsonObject.getString("credittypename"));
						ctModule.setIconurl(shopJsonObject.getString("credittypeicon"));
						ctModule.setId(String.valueOf(shopJsonObject.getInt("credittypeid")));
						
						credittypelist.add(ctModule);
					}
				}
				//解析json
				return credittypelist;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
